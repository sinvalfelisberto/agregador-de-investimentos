package com.felisberto.agregadorinvestimentos.service;

import com.felisberto.agregadorinvestimentos.controller.dto.*;
import com.felisberto.agregadorinvestimentos.entity.Account;
import com.felisberto.agregadorinvestimentos.entity.BillingAddress;
import com.felisberto.agregadorinvestimentos.entity.User;
import com.felisberto.agregadorinvestimentos.repository.AccountRepository;
import com.felisberto.agregadorinvestimentos.repository.BillingAddressRepository;
import com.felisberto.agregadorinvestimentos.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository,
            AccountRepository accountRepository,
            BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        var entity = new User(null,
                createUserDto.username(),
                createUserDto.email(),
                // "",
                createUserDto.password(),
                Instant.now(),
                null,
                null);
        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public ListUserDto getUserById(String userId) {
        var user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ListUserDto(user.getUserId().toString(), user.getEmail(), user.getUsername());
    }

    public List<ListUserDto> listUsers() {
        var users = userRepository.findAll();

        return users.stream()
                .map(us -> new ListUserDto(us.getUserId().toString(), us.getEmail(), us.getUsername())).toList();
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto) {
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();
            if (updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
            }
            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }
            userRepository.save(user);
        }
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);
        if (userExists) {
            userRepository.deleteById(id);
        }
    }


    //Dunno why, but this is about Accounts
    public void createAccount(String userId, CreateAccountDto createAccountDto) {

        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // DTO -> Entity
        var account = new Account(
                null,
                createAccountDto.description().toString(),
                user,
                new ArrayList<>(),
                null);

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(null, accountCreated,
                createAccountDto.street(), createAccountDto.number());

        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDto> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccounts()
                .stream()
                .map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();


    }
}
