/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.service;

import java.time.LocalDateTime;
import liquibase.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mentee.banking.annotation.Auditable;
import ru.mentee.banking.annotation.Retryable;
import ru.mentee.banking.api.dto.Status;
import ru.mentee.banking.api.dto.TransferRequest;
import ru.mentee.banking.api.dto.TransferResult;
import ru.mentee.banking.aspect.exception.NetworkException;

@Service
@RequiredArgsConstructor
public class TransferService {
    @Auditable(action = "TRANSFER_FUNDS")
    @Retryable(
            maxAttempts = 5,
            retryOn = {NetworkException.class, DatabaseException.class})
    public TransferResult transfer(TransferRequest transferRequest) {
        return new TransferResult(
                "TX" + System.currentTimeMillis(), Status.SUCCESS, LocalDateTime.now());
    }
}
