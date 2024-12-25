package com.felisberto.agregadorinvestimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_billingadress")
public class BillingAddress {

    @Id
    @Column(name = "accountId")
    private UUID Id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "accountId")
    private Account account;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private Integer number;



}
