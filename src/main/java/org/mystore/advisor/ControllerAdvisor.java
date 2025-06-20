package org.mystore.advisor;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.mystore.dto.RequestWrapperDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.mystore.constant.CommonConstant;
import org.mystore.dto.StatusResponse;
import org.mystore.exception.CustomGlobalException;
import org.mystore.util.CommonRequestWrapperUtil;
import org.mystore.util.CommonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<Object> handleCustomGlobalException(CustomGlobalException e, WebRequest request) {
        log.error("Error {}/ {} ", e.getCode(), e.getMessage());
        StatusResponse statusResponse = CommonRequestWrapperUtil.buildErrorResponse(e.getCode(), e.getMessage());

        if (!CommonUtil.isNullOrEmpty(e.getHttpStatus())) {
            return new ResponseEntity<>(getRequestWrapperDto(e.getHttpStatus(), Collections.singletonList(statusResponse)),
                    e.getHttpStatus());
        }

        return new ResponseEntity<>(getRequestWrapperDto(HttpStatus.BAD_REQUEST, Collections.singletonList(statusResponse)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e, WebRequest request) {
        log.error("Error occurred at due to : ()", e);

        StatusResponse statusResponse = CommonRequestWrapperUtil.buildErrorResponse(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + " from " + request);

        return new ResponseEntity<>(getRequestWrapperDto(HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singletonList(statusResponse)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<StatusResponse> errorResponse = buildValidationErrors(ex.getConstraintViolations());
        return new ResponseEntity<>(getRequestWrapperDto(HttpStatus.BAD_REQUEST, errorResponse), HttpStatus.BAD_REQUEST);
    }

    public static RequestWrapperDTO getRequestWrapperDto(HttpStatus httpStatus,
                                                         List<StatusResponse> errorResponse) {
        StatusResponse statusResponse = CommonRequestWrapperUtil.buildHttpStatusResponse(httpStatus);
        return CommonRequestWrapperUtil.buildRequestWrapper(null, null, Collections.singletonList(statusResponse),
                errorResponse);
    }

    @ExceptionHandler(feign.RetryableException.class)
    public ResponseEntity<Object> handleFeignRetryableException(Exception e, WebRequest request) {

        if (ObjectUtils.isNotEmpty(request) && ObjectUtils.isNotEmpty(request.getParameterMap())) {
            log.error("Error occurred at due to : {} and Request {}", e, request.getParameterMap());
        } else {
            log.error("Error occurred at due to : ", e);
        }

        StatusResponse statusResponse = CommonRequestWrapperUtil.buildErrorResponse(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "Service call exception occurred.");
        return new ResponseEntity<>(getRequestWrapperDto(HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singletonList(statusResponse)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<StatusResponse> buildValidationErrors(Set<ConstraintViolation<?>> violations) {
        return violations.stream().
                map(violation -> StatusResponse.builder().
                        message(constructMessage(violation))
                        .code(CommonConstant.ERR_AR_VE_400).
                        build()).
                toList();
    }

    private String constructMessage(ConstraintViolation<?> violation) {

        if (violation.getMessageTemplate().startsWith("{") && violation.getMessageTemplate().endsWith("}")) {
            return StringUtils.capitalize(Objects.requireNonNull(StreamSupport.stream(
                                    violation.getPropertyPath().spliterator(), false).
                            reduce((first, second) -> second).
                            orElse(null))
                    .toString()) + " " + violation.getMessage();
        }

        return violation.getMessage();
    }
}
