/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditEntry {
    private String action;
    private String user;
    private LocalDateTime timeStamp;
    private String parameters;
    private Status status;
    private String result;
    private String error;
}
