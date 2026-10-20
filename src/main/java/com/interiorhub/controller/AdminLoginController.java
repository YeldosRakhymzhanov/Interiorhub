package com.interiorhub.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminLoginController {

    @GetMapping("/admin/login")
    public String loginPage() {
        return "forward:/admin-login.html";
    }

    @GetMapping("/csrf")
    @ResponseBody
    public CsrfToken csrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }
}