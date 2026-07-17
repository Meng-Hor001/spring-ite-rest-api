package co.istad.productapisimpledemo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// request for filter product
@Data
public class ProductFilter{
    private String name;
    private String code;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean isAvailable;
    private Integer categoryId;

    // manytomany
    private List<String> tagNames; // Filter by a list of tag names

}
