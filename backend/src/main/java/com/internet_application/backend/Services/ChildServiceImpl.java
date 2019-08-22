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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ChildServiceImpl implements ChildService {
    @Autowired
    private ChildRepository childRepository;

    public List<ChildEntity> getAllChildren() {
        return childRepository.findAll();
    }

    @Override
    public void save(ChildEntity child) {
        childRepository.save(child);
    }

    @Override
    public void saveAll(List<ChildEntity> children) {
        for(ChildEntity child: children) {
            save(child);
        }
    }
}
