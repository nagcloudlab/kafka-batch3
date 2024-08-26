package com.example.util;


import com.example.domain.Transaction;
import com.example.domain.TransactionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionLoader {

    private static final CSVFormat CSV_FORMAT = CSVFormat.RFC4180.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();
    @SneakyThrows
    public static List<Transaction> loadTransactions(String filename){
        try (Reader in = new FileReader(new ClassPathResource(filename).getFile())) {
            return CSV_FORMAT.parse(in).stream()
                    .map(csvRecord -> Transaction.builder()
                            .id(UUID.randomUUID())
                            .timestamp(parseDateTime(csvRecord.get(0)))
                            .type(TransactionType.valueOf(csvRecord.get(1)))
                            .fromAccount(extractAccount(csvRecord.get(2)))
                            .toAccount(extractAccount(csvRecord.get(3)))
                            .amount(new BigDecimal(csvRecord.get(4)))
                            .build())
                    .toList();
        }
    }

    private static String extractAccount(String account) {
        return (Objects.isNull(account) || account.isBlank())? null: account;
    }

    private static LocalDateTime parseDateTime(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
