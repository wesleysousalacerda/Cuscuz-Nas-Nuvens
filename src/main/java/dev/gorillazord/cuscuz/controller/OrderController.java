package dev.gorillazord.cuscuz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import dev.gorillazord.cuscuz.model.CuscuzOrder;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("cuscuzOrder")
public class OrderController {
    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

}