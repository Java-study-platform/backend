package com.study.user.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/mentor")
    public String test() {
        return "test";
    }

    @GetMapping("/admin-panel")
    public String test1() {
        return "mentor.html";
    }
}
