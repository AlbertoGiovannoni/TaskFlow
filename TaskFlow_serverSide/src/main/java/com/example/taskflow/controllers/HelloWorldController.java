// package com.example.taskflow.controllers;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/api")
// public class HelloWorldController {

//     @GetMapping("/hello")
//     public String sayHello() {
//         return "FUNGE";
//     }
// }

package com.example.taskflow.controllers;

import com.example.taskflow.models.Message;
import com.example.taskflow.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    @Autowired
    private MessageRepository greetingRepository;

    @GetMapping("/hello")
    public String sayHello() {
        // Crea un documento di esempio
        Message m = new Message("Prova");
        
        
        // Salva il documento nel database
        greetingRepository.save(m);
        
        // Restituisce una risposta
        return "Documento inserito con messaggio: " + m.getMessage();
    }
}