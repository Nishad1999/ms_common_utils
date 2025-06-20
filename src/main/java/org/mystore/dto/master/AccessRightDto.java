package org.mystore.dto.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
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
public class AccessRightDto {

    @JsonProperty("AccessRightId")
    private String accessRightId;

    @JsonProperty("AccessRightName")
    private String accessRightName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessRightDto that = (AccessRightDto) o;
        return Objects.equals(accessRightId, that.accessRightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessRightId);
    }

}
