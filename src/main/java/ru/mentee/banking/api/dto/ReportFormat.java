/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ReportFormat {
    PDF("Portable Document Format", ".pdf"),
    CSV("Comma-Separated Values", ".csv"),
    EXCEL("Microsoft Excel", ".xlsx"),
    HTML("Web Page", ".html");

    @Getter private final String description;
    @Getter private final String fileExtension;
}
