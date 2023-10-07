package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
@RequestMapping(value = "")
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;
    private AccountRepository accountRepository;
    private MessageRepository messageRepository;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService, AccountRepository accountRepository){
        this.accountService = accountService;
        this.messageService = messageService;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        Account createdAccount = accountService.registerAccount(account);
    
        if (createdAccount != null) {
            return 
                ResponseEntity.status(HttpStatus.OK)
                              .body(createdAccount);
        } else {
            Account existingAccount = accountRepository.findByUsername(account.getUsername());
            
            if (existingAccount != null) {
                return 
                    ResponseEntity.status(HttpStatus.CONFLICT)
                                  .body("Username already exists.");
            } else {
                return 
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
    
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return 
                ResponseEntity.status(HttpStatus.OK)
                              .body(existingAccount);
        } else {
            return 
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                              .body("Invalid username or password.");
        }
    }
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> retrieveAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        // Check if message_text is not blank and under 255 characters
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid message text.");
        }
    
        // Check if posted_by is null
        if (message.getPosted_by() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user.");
        }
    
        // Check if user with posted_by ID exists
        Optional<Account> userOptional = accountRepository.findById(message.getPosted_by().longValue());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist.");
        }
    
        // Create a new message
        Message newMessage = new Message();
        newMessage.setPosted_by(message.getPosted_by());
        newMessage.setMessage_text(message.getMessage_text());
        newMessage.setTime_posted_epoch(System.currentTimeMillis()); // You might need logic to set this value
    
        // Save the new message to the repository
        Message savedMessage = messageRepository.save(newMessage);
    
        // Respond with the created message and HTTP status 200
        return ResponseEntity.status(HttpStatus.OK).body(savedMessage);
    }
}
