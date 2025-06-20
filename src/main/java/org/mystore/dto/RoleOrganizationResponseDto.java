package org.mystore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleOrganizationResponseDto {

    private String roleOrganizationId;
    private String organizationId;
    private String associatedOrganizationId;
    private Boolean isDefault;
}
