package com.taskflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController est un contrôleur REST API qui gère les requêtes de salutation basiques.
 * 
 * Ce contrôleur fournit des points de terminaison HTTP simples pour tester la fonctionnalité
 * de base de l'application et la connectivité.
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, TaskFlow!";
    }
    
    
    
}
