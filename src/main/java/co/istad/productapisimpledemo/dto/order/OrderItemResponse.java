package co.istad.productapisimpledemo.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Integer productId,
        String productName,
        String thumbnail,
        Integer qty ,
        BigDecimal unitPrice,
        // unitPrice x qty
        BigDecimal lineTotal
) {
}
