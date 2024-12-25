package com.felisberto.agregadorinvestimentos.controller;

import com.felisberto.agregadorinvestimentos.controller.dto.CreateStockDto;
import com.felisberto.agregadorinvestimentos.controller.dto.UpdateStockDto;
import com.felisberto.agregadorinvestimentos.controller.dto.UpdateUserDto;
import com.felisberto.agregadorinvestimentos.entity.Stock;
import com.felisberto.agregadorinvestimentos.service.StockService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/stocks")
public class StockController {

    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Void> createStock(@RequestBody CreateStockDto createStockDto ) {
        stockService.createStock(createStockDto);
        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<List<Stock>> listStocks() {
        var stocks = stockService.listStocks();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<Stock> getStockById(@PathVariable("stockId") String stockId) {
        var stock = stockService.getStockById(stockId);
        if(stock.getStockId() == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock);
    }

    @DeleteMapping("/{stockId}")
    public void deleteStockById(@PathVariable("stockId") String stockId) {
        stockService.deleteById(stockId);
        ResponseEntity.noContent().build();
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<Void> updateUserById(@PathVariable("stockId") String stockId,
                                               @RequestBody UpdateStockDto updateStockDto) {
        stockService.updateUserById(stockId, updateStockDto);
        return ResponseEntity.noContent().build();
    }



}
