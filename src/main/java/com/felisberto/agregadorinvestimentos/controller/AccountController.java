package com.felisberto.agregadorinvestimentos.controller;

import com.felisberto.agregadorinvestimentos.controller.dto.associatedAccountStock.AccountStockResponseDto;
import com.felisberto.agregadorinvestimentos.controller.dto.associatedAccountStock.AssociateAccountStockDto;
import com.felisberto.agregadorinvestimentos.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private AccountService accountService;


    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId, @RequestBody AssociateAccountStockDto associateAccountStockDto)  {
        accountService.associateStock(accountId, associateAccountStockDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> associateStock(@PathVariable("accountId") String accountId)  {
        var stocks = accountService.listStockAssociated(accountId);
        return ResponseEntity.ok(stocks);
    }
}
