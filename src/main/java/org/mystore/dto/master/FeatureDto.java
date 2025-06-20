package org.mystore.dto.master;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonRootName("Feature")
public class FeatureDto {

    private String featureId;
    private String featureName;
    private Integer featureLevel;
    private String featureParent;
    private List<Integer> pages;
    private Boolean active;
    private Boolean status;
    private String description;
    private String failureDescription;
    private Boolean isLeaf;
    private Set<AccessRightDto> accessRights;
    private Set<String> featureIds;

}
