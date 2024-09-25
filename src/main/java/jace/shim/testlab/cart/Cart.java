package jace.shim.testlab.cart;

import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity(name = "cart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    private Long id;
    private Long userId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartLine> cartLines;

    public Cart(Long id, Long userId, List<CartLine> cartLines) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(cartLines, "cartLines must not be null");

        this.id = id;
        this.userId = userId;
        this.cartLines = cartLines;
    }

    public void addCartLine(CartLine cartLine) {
        Assert.notNull(cartLine, "CartLine must not be null");
        if (checkProductIdExist(cartLine)) {
            throw new IllegalArgumentException("CartLine already exists - productId: " + cartLine.getProductId());
        }
        this.cartLines.add(cartLine);
    }

    private boolean checkProductIdExist(CartLine cartLine) {
        return this.cartLines.stream()
                .anyMatch(cl -> cl.getProductId().equals(cartLine.getProductId()));
    }

    public void removeCartLine(CartLine cartLine) {
        Assert.notNull(cartLine, "CartLine must not be null");
        this.cartLines.remove(cartLine);
    }
}