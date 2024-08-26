package com.example.entity;

import com.example.domain.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    UUID id;
    String fromAccount;
    String toAccount;
    Date timestamp;
    BigDecimal amount;
    @Enumerated(EnumType.STRING)
    TransactionType type;
}
