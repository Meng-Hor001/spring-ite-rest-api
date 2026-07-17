package co.istad.productapisimpledemo.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public record ProductRequest(
        @NotBlank(message = "name is required")
        String name,
        @NotBlank(message = "description is required")
        String description,
        @NotNull(message = "price is required")
        @Positive(message = "price must be positive")
        @DecimalMin(message ="price must be greater than 0", value= "0.0", inclusive = false)
        BigDecimal price,
        Boolean isAvailable, // save as draft
        String thumbnail,
        @Positive
        @NotNull(message = "Qty is required ")
        Integer qty,
        // latest update
        Integer categoryId,
        Set<Long> tagIds

) {
}
