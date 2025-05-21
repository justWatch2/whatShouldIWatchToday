package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class myController {


    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("Hello", "Hello World5");
        return "index";
    }

    @GetMapping(value="/test")
    @ResponseBody
    public String react(Model model) {
        return "demo 9999, react 3000";
    }
}
