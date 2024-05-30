package com.hampus.authenticate_and_authorize.controller;

import com.hampus.authenticate_and_authorize.AuthFiles.QRCode;
import com.hampus.authenticate_and_authorize.models.MyUser;
import com.hampus.authenticate_and_authorize.repo.IUserRepository;
import jakarta.validation.Valid;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MyController
{
    private PasswordEncoder passwordEncoder;
    private IUserRepository repository;
    private QRCode qrCode;

    @Autowired
    public MyController(PasswordEncoder passwordEncoder, IUserRepository repository, QRCode qrCode){
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.qrCode = qrCode;
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new MyUser());
        return "Register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("user") MyUser user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            System.out.println("Found errors!");
            return "Register";
        }else{
            user.setSecret(Base32.random())
                .setRole("USER")
                .setPassword(HtmlUtils.htmlEscape(passwordEncoder.encode(user.getPassword())))
                .setUsername(HtmlUtils.htmlEscape(user.getUsername()));
            repository.save(user);
            model.addAttribute("qrcode", qrCode.dataUrl(user));
            System.out.println("new user ez win");
            return "QRCode";
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

    @GetMapping("/login")
    public String loginPage(Model model)
    {
        model.addAttribute("user", new MyUser());
        return "Login";
    }
}
