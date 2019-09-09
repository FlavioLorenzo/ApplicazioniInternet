package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.*;
import com.internet_application.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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
    @Autowired
    private BusLineRepository busLineRepository;

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
    public UserEntity register(String email, String firstName, String lastName, Long roleId) {
        if(userRepository.findByEmail(email) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mail already exists");
        }
        RoleEntity role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not supported");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEnabled(false);
        user.setRole(role);
        user.setPassword(UUID.randomUUID().toString());
        UserEntity savedUser = userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.createToken();
        confirmationToken.setUser(user);
        confirmationToken.setCreationDate(new Date());
        confirmationTokenRepository.save(confirmationToken);

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("prova123@test.it");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:4200/confirm-account/" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            System.out.println("CONFIRMATION TOKEN = " + confirmationToken.getConfirmationToken());

        }
        catch(Exception ex) {
            confirmationTokenRepository.delete(confirmationToken);
            userRepository.delete(user);
            throw ex;
        }

        return savedUser;
    }

    /* Returns the user give the confirmation token */
    @Override
    public UserEntity getAccountConfirmationInfo(String token) {
        /* Check the confirmation token exists and it is still valid */
        ConfirmationToken ct = confirmationTokenRepository.findByConfirmationToken(token);
        if (ct == null ||
                !ct.getConfirmationToken().equals(token) ||
                !isStillValid(ct.getCreationDate()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        /* Return the user if it is not enabled yet */
        UserEntity user = ct.getUser();
        if (user.getEnabled() == true) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }

    // TODO we may want to change the response if the token is invalid or the user was already confirmed
    @Override
    public void completeAccount(String token, String password, String confirmPassword, String phone) {
        UserEntity user = getAccountConfirmationInfo(token);
        if (password == null ||
                confirmPassword == null ||
                phone == null ||
                !password.matches(confirmPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhone(phone);
        user.setEnabled(true);
        userRepository.save(user);
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
        //user.setRole(roleRepository.findByName(USER_ROLE)); //TODO: Anche questo come mai qua?
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
    public boolean checkEmail(String email) {
        if(userRepository.findByEmail(email) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mail already exists");
        }
        return true;
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

    /* Assign the admin role for a certain line */
    public UserEntity addAdminRoleOfLineToUser(Long lineId, Long userId)
        throws ResponseStatusException {
        /* Check if the user and the line exist */
        UserEntity user = userRepository.findById(userId).orElse(null);
        BusLineEntity busLineEntity = busLineRepository.findById(lineId).orElse(null);
        if (user == null || busLineEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        /* Remove the previous admin */
        UserEntity previousAdmin = busLineEntity.getAdmin();
        if (previousAdmin != null) {
            previousAdmin.removeManagedLine(busLineEntity);
            /* If the previous user has no administered line set its role to escort */
            if (previousAdmin.getAdministeredBuslines().isEmpty() &&
                !previousAdmin.getRole().name.equals("ROLE_SYS_ADMIN")) {
                previousAdmin.setRole(roleRepository.findByName("ROLE_ESCORT"));
                userRepository.save(previousAdmin);
            }
        }
        /* Set the user as admin */
        busLineEntity.setAdmin(user);
        user.addManagedLine(busLineEntity);
        if (!user.getRole().name.equals("ROLE_SYS_ADMIN"))
            user.setRole(roleRepository.findByName("ROLE_ADMIN"));
        busLineRepository.save(busLineEntity);
        userRepository.save(user);
        return user;
    }

    /* Remove an administered line from a user */
    public UserEntity removeAdminRoleOfLineFromUser(Long lineId, Long userId)
        throws ResponseStatusException {
        /* Check if the user and the line exist */
        UserEntity user = userRepository.findById(userId).orElse(null);
        BusLineEntity busLineEntity = busLineRepository.findById(lineId).orElse(null);
        if (user == null || busLineEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        /* Check if the user administers the line */
        if (user != busLineEntity.getAdmin()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        /* Remove the line from the user */
        user.removeManagedLine(busLineEntity);
        busLineEntity.setAdmin(null);
        /* If the user has no administered line drop its role to escort */
        if (user.getAdministeredBuslines().isEmpty() &&
            !user.getRole().name.equals("ROLE_SYS_ADMIN")) {
            user.setRole(roleRepository.findByName("ROLE_ESCORT"));
        }
        userRepository.save(user);
        busLineRepository.save(busLineEntity);
        return user;
    }

    public List<BusLineEntity> getAdministeredLineOfUser(Long userId)
        throws ResponseStatusException {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ArrayList<>(user.getAdministeredBuslines());
    }

    public UserEntity getUserFromEmail(String email)
        throws ResponseStatusException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public UserEntity getUserById(Long userId)
            throws ResponseStatusException {
        UserEntity user = userRepository.getOne(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }
}
