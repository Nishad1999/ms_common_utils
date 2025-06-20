package org.mystore.exception;

import org.mystore.constant.domain.report.ReportErrorMessages;
import org.springframework.http.HttpStatus;

public class DateTimeParseException extends CustomGlobalException {

    public DateTimeParseException() {
        super(
                ReportErrorMessages.DATETIME_PARSE.getCode(),
                ReportErrorMessages.DATETIME_PARSE.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
