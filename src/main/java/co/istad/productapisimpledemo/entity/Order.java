package co.istad.productapisimpledemo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_tbl")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String address;
    private String remarks;
    private BigDecimal discount; // amount of money
    private Boolean isDeleted = false;
    private LocalDateTime orderedAt;
    // PENDING, PAID, SHIPPING, COMPLETED, CANCELED
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    // ManyToOne
    // customer -> user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;
    // OneToMany
    // propagation
    // when you update the parent, what action to do for child
    // ex. when delete the parent(Order), orderLine will also deleted
    // ex. create obj (orderline)
    // order.setOrderLine(orderLine)
    // orderRepository.save(order) -> create OrderLine
    // no need orderLineRepository.save(orderLine)
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<OrderLine> items = new ArrayList<>();
}
