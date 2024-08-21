// // package com.example.taskflow.controllers;

// // import org.springframework.web.bind.annotation.GetMapping;
// // import org.springframework.web.bind.annotation.RequestMapping;
// // import org.springframework.web.bind.annotation.RestController;

// // @RestController
// // @RequestMapping("/api")
// // public class HelloWorldController {

// //     @GetMapping("/hello")
// //     public String sayHello() {
// //         return "FUNGE";
// //     }
// // }

// package com.example.taskflow.controllers;

// import com.example.taskflow.models.Message;
// import com.example.taskflow.repositories.MessageRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/api")
// public class HelloWorldController {

//     @Autowired
//     private MessageRepository greetingRepository;

//     @GetMapping("/hello")
//     public String sayHello() {
//         // Crea un documento di esempio
//         Message m = new Message("Pr nweuhfuon");
        
        
//         // Salva il documento nel database
//         greetingRepository.save(m);
        
//         // Restituisce una risposta
//         return "Documento inserito con messaggio: " + m.getMessage();
//     }
// }

package com.example.taskflow.controllers;

import com.example.taskflow.models.Message;
import com.example.taskflow.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/hello")
    public Map<String, String> sayHello(@RequestBody Map<String, String> requestBody) {
        // Estrai il messaggio dal corpo della richiesta
        String messageContent = requestBody.get("message");

        // Crea un documento con il messaggio ricevuto
        Message m = new Message(messageContent);

        // Salva il documento nel database
        messageRepository.save(m);

        // Crea una risposta JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Documento inserito con messaggio: " + m.getMessage());

        // Restituisce la risposta JSON
        return response;
    }
}