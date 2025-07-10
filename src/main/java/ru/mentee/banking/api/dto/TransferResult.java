/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResult {

    @NotNull private String transactionId;
    private Status status;
    private LocalDateTime timestamp;
}
