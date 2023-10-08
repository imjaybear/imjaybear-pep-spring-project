package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    // REGISTER USER HANDLER
    @PostMapping("/register")
    public ResponseEntity<?> registerUserHandler(@RequestBody Account account) {
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

    // LOGIN HANDLER
    @PostMapping("/login")
    public ResponseEntity<?> loginUserHandler(@RequestBody Account account) {
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

    // RETRIEVE ALL MESSAGES HANDLER
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> retrieveAllMessagesHandler() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    // CREATE MESSAGE HANDLER
    @PostMapping("/messages")
public ResponseEntity<?> createMessageHandler(@RequestBody Message message) {
  Message createdMessage = messageService.createMessage(message);
  if(createdMessage != null){
   return ResponseEntity.status(200).body(createdMessage);
  } else
        return ResponseEntity.status(400).build();

}

    
    // RETRIEVE MESSAGE BY ID HANDLER
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<?> retrieveMessageByIdHandler(@PathVariable("message_id") int messageId) {
        Optional<Message> optionalMessage = messageService.getMessageById(messageId);
        if (optionalMessage.isPresent()) {
            Message foundMessage = optionalMessage.get();
            return ResponseEntity.status(HttpStatus.OK).body(foundMessage);
        } else {
            // Return an empty response with status 200 when no message is found
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    // DELETE MESSAGE BY ID HANDLER
    @DeleteMapping("messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageByIdHandler(@PathVariable int message_id){
        Message deletMessage = messageService.deleteMessageById(message_id);
        if(deletMessage != null){
            String[] lines = deletMessage.getMessage_text().split("\r|\n");
            return ResponseEntity.status(200).body(lines.length);
        } else
            return ResponseEntity.status(200).body(0);
    }

    // RETRIEVE MESSAGE BY ACCOUNT ID HANDLER
    @GetMapping("/accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessageByAccountId(@PathVariable int account_id){
        List<Message> messageList = messageService.getMessagesByAccountId(account_id);
        return ResponseEntity.status(200).body(messageList);
    }
    
    // UPDATE MESSAGE BY MESSAGE ID HANDLER
    @PatchMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> updateMessageHandler(@PathVariable int message_id, @RequestBody Message updatedMessage){
        Message message = messageService.updateMessage(message_id, updatedMessage);
        if(message != null){
            String[] lines = message.getMessage_text().split("\r|\n");
            return ResponseEntity.status(200).body(lines.length);
        } else
            return ResponseEntity.status(400).body(0);
    }
    
}
