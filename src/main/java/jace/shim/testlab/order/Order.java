package jace.shim.testlab.order;

import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    private Long id;
    private Long userId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines;

    public Order(Long id, Long userId, List<OrderLine> orderLines) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(orderLines, "orderLines must not be null");

        this.id = id;
        this.userId = userId;
        this.orderLines = orderLines;
    }

    public void addOrderLine(OrderLine orderLine) {
        Assert.notNull(orderLine, "OrderLine must not be null");
        if (checkProductIdExist(orderLine)) {
            throw new IllegalArgumentException("OrderLine already exists - productId: " + orderLine.getProductId());
        }
        this.orderLines.add(orderLine);
    }

    private boolean checkProductIdExist(OrderLine orderLine) {
        return this.orderLines.stream()
                .anyMatch(cl -> cl.getProductId().equals(orderLine.getProductId()));
    }

    public void removeCartLine(OrderLine orderLine) {
        Assert.notNull(orderLine, "OrderLine must not be null");
        this.orderLines.remove(orderLine);
    }
}