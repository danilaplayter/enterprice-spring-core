/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.mentee.banking.api.dto.ReportFormat;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GenerateReport {
    String reportName();

    ReportFormat format() default ReportFormat.PDF;
}
