package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.ConfirmationToken;
import com.internet_application.backend.Entities.RecoverToken;
import com.internet_application.backend.Entities.RoleEntity;
import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.Repositories.ConfirmationTokenRepository;
import com.internet_application.backend.Repositories.RecoverTokenRepository;
import com.internet_application.backend.Repositories.RoleRepository;
import com.internet_application.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private RecoverTokenRepository recoverTokenRepository;

    private final String USER_ROLE = "ROLE_USER";

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }



    // NOT USED CURRENTLY
    @Override
    public String login(String email, String password) {
        UserEntity user = findByEmail(email);
        if(!bCryptPasswordEncoder.matches(password, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return "JWT";
    }

    @Override
    public void register(String email, String password, String confirmPassword, String firstName, String lastName) {

        //Annotations do must of the work. I just check password matching and that the email is not used
        if(!password.matches(confirmPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must match");
        }

        if(userRepository.findByEmail(email) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mail already exists");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEnabled(false);
        user.setPassword(password); //This will be encoded in the save method
        save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        confirmationToken.setCreationDate(new Date());
        confirmationToken.createToken();

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("prova123@test.it");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

    }

    // TODO we may want to change the response if the token is invalid or the user was already confirmed
    @Override
    public void confirmAccount(String token) {
        ConfirmationToken ct = confirmationTokenRepository.findByConfirmationToken(token);
        if (ct == null ||
            !ct.getConfirmationToken().equals(token) ||
            !isStillValid(ct.getCreationDate()))
            throw new BadCredentialsException("Bad token");
        UserEntity user = ct.getUser();
        if (user.getEnabled() == true) {
            throw new BadCredentialsException("User already confirmed");
        } else {
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    @Override
    public void recoverAccount(String email) {
        UserEntity currentUser = userRepository.findByEmail(email);
        if(currentUser == null){
            return;
        }

        RecoverToken recoverToken = new RecoverToken();
        recoverToken.setUser(currentUser);
        recoverToken.setCreationDate(new Date());
        recoverToken.setIsValid(true);
        recoverToken.createToken();

        recoverTokenRepository.save(recoverToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(currentUser.getEmail());
        mailMessage.setSubject("Recover Password!");
        mailMessage.setFrom("prova123@test.it");
        mailMessage.setText("To recover your password, please click here : "
                +"http://localhost:8080/recover/"+recoverToken.getRecoverToken());

        emailSenderService.sendEmail(mailMessage);

    }

    @Override
    /*
     * For now we suppose that the recoverToken has the same expiry duration of the confirmationToken
     */
    public void restorePassword(String password, String confirmPassword, String randomUUID) {
        RecoverToken recoverToken = recoverTokenRepository.findByRecoverToken(randomUUID);

        if (recoverToken == null ||
                !isStillValid(recoverToken.getCreationDate()) ||
                !recoverToken.getIsValid() ||
                !password.equals(confirmPassword))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        UserEntity user = recoverToken.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        // Before returning, make the recoverToken no longer usable.
        recoverToken.setIsValid(false);
        recoverTokenRepository.save(recoverToken);
    }

    @Override
    public void save(UserEntity user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //TODO: Come mai qua?
        user.setRole(roleRepository.findByName(USER_ROLE)); //TODO: Anche questo come mai qua?
        userRepository.save(user);
    }

    @Override
    public void saveAll(List<UserEntity> users) {
        for(UserEntity user: users) {
            user.setEnabled(true);
            save(user);
        }
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    private boolean isStillValid(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        return (d.compareTo(cal.getTime()) <= 0);
    }

    @Override
    public void modifyRole(Long userId, String line, String role) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user does not exist");
        RoleEntity re = roleRepository.findByName(role);
        if (re == null || re.getName().equals("ROLE_SYS_ADMIN"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "role does not exist");
        user.setRole(re);
        userRepository.save(user);
    }
}
