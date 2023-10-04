package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account persistAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else
            return null;
    }

    public void deleteAccount(long id){
        accountRepository.deleteById(id);
    }

    public void updateAccount(long id, Account replacement){
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            account.setUsername(replacement.getUsername());
            account.setPassword(replacement.getPassword());
        }
    }
    public Account findByUsername(String username){
        return accountRepository.findByUsername(username);
    }
    
    public Account registerAccount(Account account) {
        if (account.getUsername() == null 
         || account.getPassword().length() < 4) {
            return null;
        }
    
        
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount != null) {
            return null;
        }
    
        Account newAccount = new Account();
        newAccount.setUsername(account.getUsername());
        newAccount.setPassword(account.getPassword());
    
        return accountRepository.save(newAccount);
    }
}
