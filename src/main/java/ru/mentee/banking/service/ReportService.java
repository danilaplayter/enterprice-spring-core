/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mentee.banking.annotation.Auditable;
import ru.mentee.banking.annotation.Cacheable;
import ru.mentee.banking.annotation.RequiresRole;
import ru.mentee.banking.api.dto.ReportFormat;

@Service
@RequiredArgsConstructor
public class ReportService {
    @Auditable(action = "GENERATE_REPORT")
    @Cacheable(cacheName = "reports")
    @RequiresRole({"ROLE_ADMIN"})
    public void generateReport(String reportName, ReportFormat reportFormat, Object data) {
        System.out.println("Generating report: " + reportName);
        System.out.println("Format: " + reportFormat);
        System.out.println("Data: " + data);
    }
}
