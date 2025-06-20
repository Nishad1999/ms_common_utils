package org.mystore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode {

    ERR_AR_CMN_01("Email Rate Limit exceeds for %s."),
    ERR_AR_CMN_02("Error occurred while validating the file."),
    ERR_AR_CMN_03("Invalid file content for %s."),
    ERR_AR_CMN_04("Invalid file extension for %s."),
    ERR_AR_CMN_05("File size exceeds for %s."),
    ERR_AR_CMN_06("error occurred while wrapping fileS3InfoDto"),
    ERR_AR_CMN_07("No instances available for service: %s"),
    ERR_AR_CMN_08("%s has no % endpoint in configurator"),
    ERR_AR_CMN_09("Root parent org id is missing in the Token/Header"),
    ERR_AR_CMN_10("Limit exceeded for organization %s and action %s"),
    ERR_AR_CMN_11("Application Type is missing in the Token/Header"),
    ERR_AR_CMN_12("Invalid organization timeZone value"),
    ERR_AR_CMN_13("Organization id is missing in the Token/Header"),
    ERR_AR_CMN_14("%s is mandatory"),
    ERR_AR_CMN_15("%s")
    ;

    public final String message;

}
