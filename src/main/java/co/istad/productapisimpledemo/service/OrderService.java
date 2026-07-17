package co.istad.productapisimpledemo.service;

import co.istad.productapisimpledemo.dto.order.CreateOrderRequest;
import co.istad.productapisimpledemo.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getAllOrdersByCustomerId(Integer customerId);

    // updateOrder
    // cancelOrder
    // deleteOrder

}
