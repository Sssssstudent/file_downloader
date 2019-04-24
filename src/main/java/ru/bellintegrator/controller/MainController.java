package ru.bellintegrator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * Котролер стартовой страницы
 */
@Controller
public class MainController {

    /**
     * Отобразить начальную страницу
     *
     * @return страница приветствия
     */
    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }
}
