package com.progsoftaplic.TrabalhoFinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/operador")
    public String operador() {
        return "operador";
    }

    @GetMapping("/gerencial")
    public String gerencial() {
        return "gerencial";
    }

    @GetMapping("/cancela")
    public String cancela() {
        return "cancela";
    }
}