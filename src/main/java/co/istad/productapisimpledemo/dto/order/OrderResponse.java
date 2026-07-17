package co.istad.productapisimpledemo.dto.order;


import co.istad.productapisimpledemo.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        Integer customerId,
        String customerName,
        OrderStatus status ,
        BigDecimal subTotal,
        BigDecimal discount,
        BigDecimal total ,
        LocalDateTime orderedAt,
        List<OrderItemResponse> items
) {
}
