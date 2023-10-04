package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.entity.Account;
import com.example.repository.AccountRepository;
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
}
