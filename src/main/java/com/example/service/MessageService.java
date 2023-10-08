package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message persistMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int id){
        return messageRepository.findById(id);
    }

    public void deleteStore(int id){
        messageRepository.deleteById(id);
    }

    public Message updateMessage(int id, Message replacement){
        Optional<Message> optionalMessage = messageRepository.findById(id);
        Message message = optionalMessage.get();

        if(optionalMessage.isPresent()){
            message.setMessage_text(replacement.getMessage_text());
            message.setPosted_by(replacement.getPosted_by());
            message.setTime_posted_epoch(replacement.getTime_posted_epoch());
        }
        return message;
    }

    public Message createMessage(Message message){
    if (message.getMessage_text().trim().equals("") || message.getMessage_text().length()>254){
            return null;
        }
        List<Account> account = accountRepository.findAll();
        for (Account checkedAcount : account){
            if (checkedAcount.getAccount_id().equals(message.getPosted_by())){
                return messageRepository.save(message);
            }
        }
        
            return null;
        
    }
    
}
