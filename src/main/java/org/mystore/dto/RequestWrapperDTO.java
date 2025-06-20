package org.mystore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mystore.builder.PageDetailsBuilder;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "RequestWrapperDTO")
public class RequestWrapperDTO<T, U> {

    @SchemaProperty(name = "Request")
    private Map<String, T> request;

    @SchemaProperty(name = "Response")
    private Map<String, U> response;

    private PageDetailsBuilder pageDetails;

    /**
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    @SchemaProperty(name = "Sorting")
    private Map<String, String> sorting;

    private List<StatusResponse> errors;

    private List<StatusResponse> warnings;

    private List<StatusResponse> status;

    private LocalDateTime timeStamp;

    private RoleOrganizationResponseDto roleOrganization;

    private UserDto user;

    private String baseCurrency;

    private Map<String, Object> additionalParameter;
}