package jace.shim.testlab.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders/{orderId}")
    public Order getCart(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping("/orders")
    public Order createOrder() {
        final Long userId = 1000L;
        return orderService.createCart(userId);
    }

    @PostMapping("/orders/{orderId}/order-lines")
    public Order addCartLine(@PathVariable Long orderId, @RequestBody OrderLineParam orderLine) {
        return orderService.addOrderLine(orderId, orderLine);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity exceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
