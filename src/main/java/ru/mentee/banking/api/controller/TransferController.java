/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.banking.api.dto.TransferRequest;
import ru.mentee.banking.api.dto.TransferResult;
import ru.mentee.banking.service.TransferService;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResult> createTransfer(
            @RequestBody TransferRequest transferRequest) {
        TransferResult result = transferService.transfer(transferRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
