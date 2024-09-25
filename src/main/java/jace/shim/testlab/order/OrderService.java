package jace.shim.testlab.order;

import jace.shim.testlab.utils.NumberIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order getOrder(Long orderId) {
        return findOrder(orderId);
    }

    @Transactional
    public Order createCart(Long userId) {
        Order order = new Order(NumberIdGenerator.generate(), userId, emptyList());
        return orderRepository.save(order);
    }

    @Transactional
    public Order addOrderLine(Long orderId, OrderLineParam param) {
        Order order = findOrder(orderId);
        order.addOrderLine(param.toEntity(NumberIdGenerator.generate()));
        return orderRepository.save(order);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found - orderId: " + orderId));
    }
}
