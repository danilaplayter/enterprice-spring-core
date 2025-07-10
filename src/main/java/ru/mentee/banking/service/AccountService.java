/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.service;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import ru.mentee.banking.annotation.Auditable;
import ru.mentee.banking.annotation.Cacheable;
import ru.mentee.banking.annotation.RequiresRole;
import ru.mentee.banking.api.dto.Balance;

@AllArgsConstructor
public class AccountService {

    @Auditable(action = "GET_BALANCE")
    @Cacheable(cacheName = "balance", ttl = 300)
    @RequiresRole({"USER", "ADMIN"})
    public Balance getBalance(String accountId) {
        return new Balance(accountId, new BigDecimal(1000), "USD");
    }
}
