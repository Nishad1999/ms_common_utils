package org.mystore.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.mystore.exception.CommonErrorCode;
import org.mystore.exception.CustomGlobalException;

@Getter
@Setter
@ToString
public class ContextMetadata {

    private Map<String, Object> additionalParameter = new HashMap<>();
    private String userId;
    private String orgId;
   // private OrganizationType orgType;
    private String roleId;
    private String rootParentOrgId;
    private String token;
    private String baseUrl;
    private String pcc;
    private String clientId;
    private String secret;
    private String apiKey;
    private String signature;
    private String appType;

    public String getValidatedRootParentOrgId() {
        if (ObjectUtils.isEmpty(rootParentOrgId)) {
            throw new CustomGlobalException(CommonErrorCode.ERR_AR_CMN_09.name(), CommonErrorCode.ERR_AR_CMN_09.getMessage());
        }
        return rootParentOrgId;
    }

    public String getValidatedOrgId() {
        if (ObjectUtils.isEmpty(orgId)) {
            throw new CustomGlobalException(CommonErrorCode.ERR_AR_CMN_13.name(), CommonErrorCode.ERR_AR_CMN_13.getMessage());
        }
        return orgId;
    }

    public String getValidatedUserId() {
        if (ObjectUtils.isEmpty(userId)) {
            throw new CustomGlobalException(CommonErrorCode.ERR_AR_CMN_13.name(), CommonErrorCode.ERR_AR_CMN_13.getMessage());
        }
        return userId;
    }

    public String getValidatedAppType() {
        if (StringUtils.isEmpty(appType)) {
            throw new CustomGlobalException(CommonErrorCode.ERR_AR_CMN_11.name(), CommonErrorCode.ERR_AR_CMN_11.getMessage());
        }
        return appType;
    }
}
