/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.api.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
    @NotNull private String accountId;
    private BigDecimal amount;
    @NotNull private String currency;
}
