package org.mystore.constant.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportErrorMessages {

    DATE_PARSE("RE01", "Error while parsing the date"),
    DATETIME_PARSE("RE02", "Error while parsing the datetime"),
    NOT_VALID_VALUE("RE03", "Value is not matching with the input constraint");

    private final String code;
    private final String message;
}
