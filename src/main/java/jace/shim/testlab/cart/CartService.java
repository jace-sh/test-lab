package jace.shim.testlab.cart;

import jace.shim.testlab.utils.NumberIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart getCart(Long cartId) {
        return findCart(cartId);
    }

    @Transactional
    public Cart createCart(Long userId) {
        Cart cart = new Cart(NumberIdGenerator.generate(), userId, emptyList());
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart addCartLine(Long cartId, CartLineParam param) {
        Cart cart = findCart(cartId);
        cart.addCartLine(param.toEntity(NumberIdGenerator.generate()));
        return cartRepository.save(cart);
    }

    private Cart findCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found - cartId: " + cartId));
    }
}
