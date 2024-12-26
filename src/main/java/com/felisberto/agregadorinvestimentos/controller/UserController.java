package com.felisberto.agregadorinvestimentos.controller;

import com.felisberto.agregadorinvestimentos.controller.dto.user.CreateUserDto;
import com.felisberto.agregadorinvestimentos.controller.dto.user.ListUserDto;
import com.felisberto.agregadorinvestimentos.controller.dto.user.UpdateUserDto;
import com.felisberto.agregadorinvestimentos.controller.dto.account.AccountResponseDto;
import com.felisberto.agregadorinvestimentos.controller.dto.account.CreateAccountDto;
import com.felisberto.agregadorinvestimentos.entity.User;
import com.felisberto.agregadorinvestimentos.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ListUserDto> getUserById(@PathVariable("userId") String userId) {
        var user = userService.getUserById(userId);
        if (user.userId() == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<ListUserDto>> listUsers() {
        var users = userService.listUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable("userId") String userId,
                                               @RequestBody UpdateUserDto updateUserDto) {
        userService.updateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/account")
    public ResponseEntity<List<AccountResponseDto>> createAccount(@PathVariable("userId") String userId, @RequestBody CreateAccountDto createAccountDto)  {
        userService.createAccount(userId, createAccountDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDto>> getAccountById(@PathVariable("userId") String userId) {
        var accounts = userService.listAccounts(userId);
        return ResponseEntity.ok(accounts);
    }
}
