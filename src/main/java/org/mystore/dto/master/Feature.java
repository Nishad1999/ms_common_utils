package org.mystore.dto.master;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Feature {

  private String featureId;
  private String featureName;
  private Integer featureLevel;
  private String featureParent;
  private List<Integer> pages;
  private boolean active = Boolean.TRUE;
  private boolean status;
  private String description;
  private Set<AccessRight> accessRights;
  private Set<Feature> children = new HashSet<>();
  private Set<String> featureIds;
}