package com.example.service;

import java.util.ArrayList;
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

    public Message persistMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int id) {
        return messageRepository.findById(id);
    }

    public Message deleteMessageById(int message_id) {
        Optional<Message> optionalMessage = messageRepository.findById(message_id);

        if (optionalMessage.isPresent()) {
            Message deletedMessage = optionalMessage.get();
            messageRepository.deleteById(message_id);
            return deletedMessage;
        } else
            return null;

    }

    public Message updateMessage(int message_id, Message replacement) {
        if (replacement.getMessage_text().trim() == "" || replacement.getMessage_text().length() > 254) {
            return null;
        }
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        if (optionalMessage.isPresent()) {
            Message updatedMessage = optionalMessage.get();
            updatedMessage.setMessage_text(replacement.getMessage_text());
            return 
              messageRepository.save(updatedMessage);
        } else
            return null;
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text().trim().equals("") || message.getMessage_text().length() > 254) {
            return null;
        }
        List<Account> account = accountRepository.findAll();
        for (Account checkedAcount : account) {
            if (checkedAcount.getAccount_id().equals(message.getPosted_by())) {
                return 
                  messageRepository.save(message);
            }
        }

        return null;

    }

    public List<Message> getMessagesByAccountId(int account_id) {
        List<Message> messageList = messageRepository.findAll();
        List<Message> accountMessageList = new ArrayList<>();

        for (Message message : messageList) {
            if (message.getPosted_by().equals(account_id)) {
                accountMessageList.add(message);
            }
        }
        return accountMessageList;
    }

}
