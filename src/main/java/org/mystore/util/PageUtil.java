package org.mystore.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.mystore.builder.PageDetailsBuilder;
import org.mystore.constant.CommonConstant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageUtil {

    public static PageRequest getPageRequest(PageDetailsBuilder page) {

        if (ObjectUtils.isEmpty(page)) {
            return PageRequest.of(CommonConstant.DEFAULT_PAGE, CommonConstant.DEFAULT_PAGE_SIZE);
        }

        List<Sort.Order> sort = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getPropertyDirectionSort())){
            sort = page.getPropertyDirectionSort().stream()
                    .map(propertyDirection -> new Sort.Order(Sort.Direction.fromString(propertyDirection.getDirection().name()),
                            propertyDirection.getProperty()))
                    .toList();
        }

        return PageRequest.of(ObjectUtils.isEmpty(page.getPageNumber()) ? CommonConstant.DEFAULT_PAGE : page.getPageNumber(),
                ObjectUtils.isEmpty(page.getPageSize()) ? CommonConstant.DEFAULT_PAGE_SIZE : page.getPageSize(),
                ObjectUtils.isEmpty(sort) ? Sort.unsorted() : Sort.by(sort));
    }

    public static <T> PageDetailsBuilder pagePageResponse(Page<T> page) {
        return PageDetailsBuilder.builder().pageNumber(page.getPageable().getPageNumber()).numberOfElements(page.getNumberOfElements())
                .pageSize(page.getSize()).totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).build();
    }


}
