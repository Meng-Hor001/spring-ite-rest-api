package co.istad.productapisimpledemo.dto.order;

public record OrderItemRequest(
        Integer productId,
        Integer qty
) {
}
