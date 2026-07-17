package co.istad.productapisimpledemo.dto;


import jakarta.validation.constraints.Size;
import lombok.Builder;


// used in order to create a new category
@Builder
public record CategoryRequest(
        @Size(min = 1, max = 100)
        String name,
        @Size(min = 1, max = 255)
        String description,
        // updated fields
        String icon,
        Integer parentCategoryId // NULL

) {
}
