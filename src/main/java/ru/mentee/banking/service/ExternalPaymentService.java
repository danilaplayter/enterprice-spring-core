/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mentee.banking.annotation.Auditable;
import ru.mentee.banking.annotation.RequiresRole;
import ru.mentee.banking.annotation.Retryable;
import ru.mentee.banking.api.dto.PaymentRequest;

@Service
@RequiredArgsConstructor
public class ExternalPaymentService {
    @Auditable(action = "PROCESS_PAYMENT")
    @Retryable(maxAttempts = 3)
    @RequiresRole({"ROLE_ADMIN"})
    public boolean processPayment(PaymentRequest paymentRequest) {
        return true;
    }
}
