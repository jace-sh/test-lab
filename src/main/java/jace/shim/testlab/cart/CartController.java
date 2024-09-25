package jace.shim.testlab.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/carts/{cartId}")
    public Cart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }

    @PostMapping("/carts")
    public Cart createCart() {
        final Long userId = 1000L;
        return cartService.createCart(userId);
    }

    @PostMapping("/carts/{cartId}/cart-lines")
    public Cart addCartLine(@PathVariable Long cartId, @RequestBody CartLineParam cartLine) {
        return cartService.addCartLine(cartId, cartLine);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity exceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
