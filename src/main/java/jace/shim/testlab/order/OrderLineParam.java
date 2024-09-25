package jace.shim.testlab.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderLineParam {
    private Long productId;
    private int quantity;

    public OrderLineParam(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderLine toEntity(Long id) {
        return new OrderLine(id, productId, quantity);
    }
}