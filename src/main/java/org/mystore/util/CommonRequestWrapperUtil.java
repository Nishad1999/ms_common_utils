package org.mystore.util;

import org.mystore.builder.PageDetailsBuilder;
import org.mystore.dto.RequestWrapperDTO;
import org.mystore.dto.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommonRequestWrapperUtil {

    private CommonRequestWrapperUtil() {
    }

    public static <T, U> RequestWrapperDTO<T, U> buildRequestWrapper(@Nullable T request, @Nullable String requestRootName,
            @Nullable U response, @Nullable String responseRootName, @Nullable List<StatusResponse> status) {
        RequestWrapperDTO<T, U> dto = new RequestWrapperDTO<>();
        if (!CommonUtil.isNullOrEmpty(requestRootName)) {
            dto.setRequest(CommonObjectMapperUtil.buildForObjectMapper(requestRootName, request));
        }
        if (!CommonUtil.isNullOrEmpty(responseRootName)) {
            dto.setResponse(CommonObjectMapperUtil.buildForObjectMapper(responseRootName, response));
        }
        dto.setStatus(status);
        dto.setTimeStamp(LocalDateTime.now());
        return dto;
    }

    public static <T, U> RequestWrapperDTO<T, U> buildRequestWrapper(@Nullable U response, @Nullable String responseRootName, String baseCurrency,
            @Nullable List<StatusResponse> status) {

        RequestWrapperDTO<T, U> dto = new RequestWrapperDTO<>();

        if (!CommonUtil.isNullOrEmpty(responseRootName)) {
            dto.setResponse(CommonObjectMapperUtil.buildForObjectMapper(responseRootName, response));
        }

        dto.setBaseCurrency(baseCurrency);
        dto.setStatus(status);
        dto.setTimeStamp(LocalDateTime.now());
        return dto;
    }

    public static <T, U> RequestWrapperDTO<T, U> buildRequestWrapper(@Nullable T request, @Nullable String requestRootName,
            @Nullable U response, @Nullable String responseRootName, @Nullable List<StatusResponse> status, @Nullable List<StatusResponse> errors) {
        RequestWrapperDTO<T, U> dto = new RequestWrapperDTO<>();
        if (!CommonUtil.isNullOrEmpty(requestRootName)) {
            dto.setRequest(CommonObjectMapperUtil.buildForObjectMapper(requestRootName, request));
        }
        if (!CommonUtil.isNullOrEmpty(responseRootName)) {
            dto.setResponse(CommonObjectMapperUtil.buildForObjectMapper(responseRootName, response));
        }
        dto.setStatus(status);
        dto.setErrors(errors);
        dto.setTimeStamp(LocalDateTime.now());
        return dto;
    }

    public static <T, U> RequestWrapperDTO<T, U> buildRequestWrapper(@Nullable T request, @Nullable String requestRootName,
            @Nullable U response, @Nullable String responseRootName, @Nullable List<StatusResponse> status, @Nullable List<StatusResponse> errors,
            @Nullable List<StatusResponse> warnings) {

        RequestWrapperDTO<T, U> dto = new RequestWrapperDTO<>();
        if (!CommonUtil.isNullOrEmpty(requestRootName)) {
            dto.setRequest(CommonObjectMapperUtil.buildForObjectMapper(requestRootName, request));
        }
        if (!CommonUtil.isNullOrEmpty(responseRootName)) {
            dto.setResponse(CommonObjectMapperUtil.buildForObjectMapper(responseRootName, response));
        }
        dto.setStatus(status);
        if (!CommonUtil.isNullOrEmpty(errors)) {
            dto.setErrors(errors);
        }
        if (!CommonUtil.isNullOrEmpty(warnings)) {
            dto.setWarnings(warnings);
        }
        dto.setTimeStamp(LocalDateTime.now());
        return dto;
    }

    public static <T, U> RequestWrapperDTO<T, U> buildRequestWrapper(@Nullable T request, @Nullable String requestRootName,
            @Nullable U response, @Nullable String responseRootName, @Nullable List<StatusResponse> status,
            @Nullable PageDetailsBuilder pageDetails) {
        RequestWrapperDTO<T, U> dto = buildRequestWrapper(request, requestRootName, response, responseRootName, status);
        dto.setPageDetails(pageDetails);
        return dto;
    }

    public static <T, U> RequestWrapperDTO<T, U> buildRequestWrapper(@Nullable U response, @Nullable String responseRootName,
            @Nullable List<StatusResponse> status, @Nullable List<StatusResponse> errors) {
        RequestWrapperDTO<T, U> dto = new RequestWrapperDTO<>();
        if (!CommonUtil.isNullOrEmpty(responseRootName)) {
            dto.setResponse(CommonObjectMapperUtil.buildForObjectMapper(responseRootName, response));
        }
        dto.setErrors(errors);
        dto.setStatus(status);
        dto.setTimeStamp(LocalDateTime.now());
        return dto;
    }

    public static <T, U> RequestWrapperDTO<T, U> buildRequestWrapper(@Nullable T request, @Nullable String requestRootName) {
        RequestWrapperDTO<T, U> dto = new RequestWrapperDTO<>();
        if (!CommonUtil.isNullOrEmpty(requestRootName)) {
            dto.setRequest(CommonObjectMapperUtil.buildForObjectMapper(requestRootName, request));
        }
        dto.setTimeStamp(LocalDateTime.now());
        return dto;
    }



    public static StatusResponse buildHttpStatusResponse(HttpStatus httpStatus) {
        StatusResponse status = new StatusResponse();
        status.setCode(String.valueOf(httpStatus.value()));
        status.setMessage(httpStatus.name());
        return status;
    }

    public static List<StatusResponse> getStatusWithMessage(HttpStatus httpStatus, String message) {
        List<StatusResponse> statusResponses = new ArrayList<>();
        StatusResponse status = new StatusResponse();
        status.setCode(String.valueOf(httpStatus.value()));
        status.setMessage(message);
        statusResponses.add(status);
        return statusResponses;
    }

    public static StatusResponse buildErrorResponse(String errorCode, String errorMessage) {
        return new StatusResponse(errorCode, errorMessage);
    }
}