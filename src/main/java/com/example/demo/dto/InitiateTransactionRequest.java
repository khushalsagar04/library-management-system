package com.example.demo.dto;

import com.example.demo.models.Admin;
import com.example.demo.models.Book;
import com.example.demo.models.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitiateTransactionRequest {

    @NotNull
    private Integer studentId;
    @NotNull
    private Integer bookId;
    @NotNull
    private Integer adminId;
    @NotNull
    private TransactionType transactionType;

}
