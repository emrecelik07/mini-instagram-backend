package com.emrecelik.mini_instagram_backend.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0")
public class CsrfController {
    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) { return token; } // accessing it triggers creation
}
