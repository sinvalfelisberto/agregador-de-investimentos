package com.felisberto.agregadorinvestimentos.controller.dto.user;

import com.felisberto.agregadorinvestimentos.entity.Account;

public record CreateUserDto(String username, String email, String password, Account acounts) {
}
