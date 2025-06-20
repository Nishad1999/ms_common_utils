package org.mystore.dto.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccessRight {

    @JsonProperty("AccessRightId")
    private String accessRightId;

    @JsonProperty("AccessRightName")
    private String accessRightName;

}
