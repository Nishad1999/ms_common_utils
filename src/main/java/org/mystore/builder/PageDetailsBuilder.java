package org.mystore.builder;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageDetailsBuilder {

    private Long totalElements;
    private Integer totalPages;
    private Integer numberOfElements;
    private int pageSize;
    private int pageNumber;

    /**
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    private List<Sort.Order> sort;
    private Set<PropertyDirection> propertyDirectionSort;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PropertyDirection {
        private String property;
        private Direction direction;

        public enum Direction {
            ASC,
            DESC;
        }
    }
}