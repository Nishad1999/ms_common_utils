package org.mystore.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 import org.apache.commons.collections4.map.HashedMap;
import org.mystore.dto.MultiParam;

public class CommonObjectMapperUtil {

    private CommonObjectMapperUtil() {
    }

    public static <V> Map<String, V> buildForObjectMapper(String key, V value) {
        Map<String, V> map = new HashedMap<>();
        map.put(key, value);
        return map;
    }

    public static Map<String, Object> buildListForObjectMapper(List<MultiParam> params) {
        return params.stream().collect(Collectors.toMap(MultiParam::getKey, MultiParam::getValue));
    }

}