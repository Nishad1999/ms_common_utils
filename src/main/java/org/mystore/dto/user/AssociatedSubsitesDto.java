package org.mystore.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociatedSubsitesDto {

    private String subsiteOrgId;
    private String roleId;
    private String roleName;
    private Boolean isDefault;

}
