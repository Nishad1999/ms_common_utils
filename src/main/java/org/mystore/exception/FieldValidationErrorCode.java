package org.mystore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldValidationErrorCode {

    ERR_AR_CMN_FV_01("Validation are Overlapping with Each Other");

    public final String message;

}
