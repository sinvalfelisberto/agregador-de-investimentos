package com.felisberto.agregadorinvestimentos.repository;

import com.felisberto.agregadorinvestimentos.entity.AccountStock;
import com.felisberto.agregadorinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
