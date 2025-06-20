package org.mystore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto extends BaseDto {

    private String id;
    private String pageName;
    private String pageKey;
    private Boolean isPredefined;
    private Boolean isExpenseCategoryApplicable;
}
