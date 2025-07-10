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
public class TransferRequest {
    @NotNull private String fromAccount;

    @NotNull private String toAccount;

    private BigDecimal amount;

    @NotNull private String description;

    @NotNull private String userId;
}
