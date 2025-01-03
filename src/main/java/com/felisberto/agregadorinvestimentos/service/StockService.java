package com.felisberto.agregadorinvestimentos.service;

import com.felisberto.agregadorinvestimentos.controller.dto.stock.CreateStockDto;
import com.felisberto.agregadorinvestimentos.controller.dto.stock.UpdateStockDto;
import com.felisberto.agregadorinvestimentos.entity.Stock;
import com.felisberto.agregadorinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> listStocks() {
        return stockRepository.findAll();
    }

    public void createStock(CreateStockDto createStockDto) {
        if(!createStockDto.stockId().isEmpty()){
            var stock = new Stock(createStockDto.stockId(), createStockDto.description());

            stockRepository.save(stock);
        }
    }

    public Stock getStockById(String stockId) {
        var stock = stockRepository.findById(stockId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return stock;
    }

    public void deleteById(String stockId) {
        var stock = stockRepository.findById(stockId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        stockRepository.deleteById(stock.getStockId());
    }

    public void updateUserById(String stockId, UpdateStockDto updateStockDto) {
        var stock = stockRepository.findById(stockId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(updateStockDto.description() != null) {
            stock.setDescription(updateStockDto.description());
        }
        stockRepository.save(stock);
    }
}
