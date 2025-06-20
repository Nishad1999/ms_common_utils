package org.mystore.exception;

 import org.mystore.constant.domain.report.ReportErrorMessages;
 import org.springframework.http.HttpStatus;

public class DateParseException extends CustomGlobalException {

    public DateParseException() {
        super(
                ReportErrorMessages.DATE_PARSE.getCode(),
                ReportErrorMessages.DATE_PARSE.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
