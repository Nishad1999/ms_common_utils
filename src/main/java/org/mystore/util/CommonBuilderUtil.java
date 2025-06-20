package org.mystore.util;

import org.apache.commons.lang3.ObjectUtils;
import org.mystore.builder.PageDetailsBuilder;
import org.mystore.constant.CommonConstant;
import org.mystore.dto.MultiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonBuilderUtil {

    private CommonBuilderUtil() {
    }

    public static <T> PageDetailsBuilder buildPageDetailsBuilder(Page<T> page) {
        return PageDetailsBuilder.builder().pageNumber(page.getPageable().getPageNumber())
                .numberOfElements(page.getNumberOfElements()).pageSize(page.getSize())
                .totalElements(page.getTotalElements()).totalPages(page.getTotalPages())
                .build();
    }

    public static List<MultiParam> buildPaginationRequest(@Nullable Integer page,
            @Nullable Integer size) {
        return Stream.of(new MultiParam(CommonConstant.PAGE_FIELD,
                        CommonUtil.isNullOrEmpty(page) ? CommonConstant.DEFAULT_PAGE : page.intValue()),
                new MultiParam(CommonConstant.SIZE_FIELD,
                        CommonUtil.isNullOrEmpty(size) ? CommonConstant.DEFAULT_PAGE_SIZE : size.intValue())).collect(Collectors.toList());
    }

    public static PageRequest getPageRequest(@Nullable Integer page, @Nullable Integer size) {
        return PageRequest.of(ObjectUtils.isEmpty(page) ? CommonConstant.DEFAULT_PAGE : page,
                ObjectUtils.isEmpty(size) ? CommonConstant.DEFAULT_PAGE_SIZE : size);

    }

    public static PageRequest getPageRequest(@Nullable Integer page, @Nullable Integer size,
            String sortBy, @Nullable Direction sortOrder) {
        return PageRequest.of(ObjectUtils.isEmpty(page) ? CommonConstant.DEFAULT_PAGE : page,
                ObjectUtils.isEmpty(size) ? CommonConstant.DEFAULT_PAGE_SIZE : size,
                ObjectUtils.isEmpty(sortOrder) ? Direction.ASC : sortOrder,
                sortBy);

    }


}