package com.hampus.authenticate_and_authorize.controller;

import com.hampus.authenticate_and_authorize.models.MyUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MyController
{
    private PasswordEncoder passwordEncoder;
    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    public MyController(PasswordEncoder passwordEncoder, InMemoryUserDetailsManager manager){
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = manager;
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new MyUser());
        return "Register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("user") MyUser user, RedirectAttributes redirectAttributes, BindingResult bindingResult){
        System.out.println("inside?");
        if (bindingResult.hasErrors()) {
            System.out.println("inside binding result error");
            return "Register";
        }else{
            System.out.println("inside else sats");
            UserDetails newUser = User
                    .builder()
                    .password(HtmlUtils.htmlEscape(passwordEncoder.encode(user.getPassword())))
                    .username(HtmlUtils.htmlEscape(user.getUsername()))
                    .roles("USER")
                    .build();
            userDetailsManager.createUser(newUser);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/user/home";
        }

    }

    @GetMapping("/home")
    public String homePage(){
        return "Home";
    }

    @GetMapping("/admin/home")
    public String adminHomePage()
    {
        return "AdminPage";
    }

    @GetMapping("/user/home")
    public String userHomePage(@ModelAttribute("user") MyUser user, Model model)
    {
        model.addAttribute("user", user);
        return "UserPage";
    }
}
