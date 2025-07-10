/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.api.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String accountId;
    private String paymentDetails;
    private BigDecimal amount;
}
