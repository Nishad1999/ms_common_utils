package org.mystore.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.mystore.dto.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

public class CommonUtil {

    private CommonUtil() {
    }

    public static boolean isNullOrEmpty(Map<?, ?> obj) {
        return null == obj || CollectionUtils.isEmpty(obj);
    }

    public static boolean isNullOrEmpty(Collection<?> obj) {
        return null == obj || CollectionUtils.isEmpty(obj);
    }

    public static boolean isNullOrEmpty(String string) {
        return null == string || StringUtils.isEmpty(string);
    }

    public static boolean isNullOrEmpty(Object obj) {
        return null == obj || ObjectUtils.isEmpty(obj);
    }

    public static List<StatusResponse> getStatus(HttpStatus httpStatus) {
        return Collections.singletonList(CommonRequestWrapperUtil.buildHttpStatusResponse(httpStatus));
    }

    public static boolean isNotNullOrEmpty(Object obj) {
        return !isNullOrEmpty(obj);
    }
}
