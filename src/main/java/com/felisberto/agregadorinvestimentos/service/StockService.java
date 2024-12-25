package com.felisberto.agregadorinvestimentos.service;

import com.felisberto.agregadorinvestimentos.controller.dto.CreateStockDto;
import com.felisberto.agregadorinvestimentos.controller.dto.UpdateStockDto;
import com.felisberto.agregadorinvestimentos.entity.Stock;
import com.felisberto.agregadorinvestimentos.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StockService {
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

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
