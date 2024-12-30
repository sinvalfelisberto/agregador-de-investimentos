package com.felisberto.agregadorinvestimentos.service;

import com.felisberto.agregadorinvestimentos.client.BrapiClient;
import com.felisberto.agregadorinvestimentos.controller.dto.associatedAccountStock.AccountStockResponseDto;
import com.felisberto.agregadorinvestimentos.controller.dto.associatedAccountStock.AssociateAccountStockDto;
import com.felisberto.agregadorinvestimentos.entity.AccountStock;
import com.felisberto.agregadorinvestimentos.entity.AccountStockId;
import com.felisberto.agregadorinvestimentos.repository.AccountRepository;
import com.felisberto.agregadorinvestimentos.repository.AccountStockRepository;
import com.felisberto.agregadorinvestimentos.repository.StockRepository;
import com.felisberto.agregadorinvestimentos.token.Token;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private AccountStockRepository accountStockRepository;

    @Autowired
    private BrapiClient brapiClient;

    public void associateStock(String accountId, AssociateAccountStockDto associateAccountStockDto) {
        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var stock = stockRepository.findById(associateAccountStockDto.stockId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(id, account, stock, associateAccountStockDto.quantity());
        accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDto> listStockAssociated(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return account.getAccountStocks().stream().map(as -> new AccountStockResponseDto(
                as.getStock().getStockId(),
                as.getQuantity(), //0.0)
                getTotal(as.getQuantity(), as.getStock().getStockId()))
        ).toList();

    }

    private double getTotal(Integer quantity, String stockId) {
        //new feature?!
        String TOKEN = new Token().getToken();
        var response = brapiClient.getQuote(TOKEN, stockId);
        var price = response.results().getFirst().regularMarketPrice();
        return quantity * price;
    }
}
