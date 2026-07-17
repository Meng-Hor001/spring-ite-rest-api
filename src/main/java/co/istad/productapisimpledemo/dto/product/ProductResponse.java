package co.istad.productapisimpledemo.dto.product;

import co.istad.productapisimpledemo.dto.CategoryResponse;

import java.math.BigDecimal;
import java.util.Set;

public record ProductResponse(
        Integer id ,
        String name,
        String description,
        BigDecimal price,
        String slug,
        String thumbnail,
        Integer qty ,
        CategoryResponse category,
      // Set<TagResponse> tags
        Set<String> tags
) {
}
