package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;

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

    public Message getMessageById(long id){
        Optional<Message> optionalMessage = messageRepository.findById(id);

        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        } else  
            return null;
    }

    public void deleteStore(long id){
        messageRepository.deleteById(id);
    }

    public Message updateMessage(long id, Message replacement){
        Optional<Message> optionalMessage = messageRepository.findById(id);
        Message message = optionalMessage.get();

        if(optionalMessage.isPresent()){
            message.setMessage_text(replacement.getMessage_text());
            message.setPosted_by(replacement.getPosted_by());
            message.setTime_posted_epoch(replacement.getTime_posted_epoch());
        }
        return message;
    }
}
