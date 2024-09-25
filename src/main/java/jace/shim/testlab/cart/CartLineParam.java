package jace.shim.testlab.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartLineParam {
    private Long productId;
    private int quantity;

    public CartLineParam(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartLine toEntity(Long id) {
        return new CartLine(id, productId, quantity);
    }
}