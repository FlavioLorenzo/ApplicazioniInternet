package com.internet_application.backend.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

/**
 * Since the class needs to be annotated with @Controller to return a page, we create a new class for this purpose
 */
@CrossOrigin()
@Controller
public class UserPageController {
    @GetMapping("/recover/{randomUUID}")
    public String recoverAccountPage(@PathVariable(value="randomUUID") String token, Model m) {
        m.addAttribute("content", "recover");
        m.addAttribute("title", "Recover Password");

        Map<String, String> messages = new HashMap<>();
        m.addAttribute("messages", messages);
        return "index";
    }
}
