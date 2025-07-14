/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.banking.api.dto.Balance;
import ru.mentee.banking.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Balance> getAccountBalance(@PathVariable String accountId) {
        Balance balance = accountService.getBalance(accountId);
        return ResponseEntity.ok(balance);
    }
}
