package com.felisberto.agregadorinvestimentos.service;

import com.felisberto.agregadorinvestimentos.controller.dto.associatedAccountStock.AccountStockResponseDto;
import com.felisberto.agregadorinvestimentos.controller.dto.associatedAccountStock.AssociateAccountStockDto;
import com.felisberto.agregadorinvestimentos.entity.AccountStock;
import com.felisberto.agregadorinvestimentos.entity.AccountStockId;
import com.felisberto.agregadorinvestimentos.repository.AccountRepository;
import com.felisberto.agregadorinvestimentos.repository.AccountStockRepository;
import com.felisberto.agregadorinvestimentos.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AccountService {

    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;

    public void associateStock(String accountId, AssociateAccountStockDto associateAccountStockDto) {
        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var stock = stockRepository.findById(associateAccountStockDto.stockId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(id, account, stock, associateAccountStockDto.quantity());
        accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDto> listStockAssociated(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return account.getAccountStocks().stream().map(as -> new AccountStockResponseDto(as.getStock().getStockId(), as.getQuantity(), as.getQuantity() * 3.57)).toList();

    }
}
