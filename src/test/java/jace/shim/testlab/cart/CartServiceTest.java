package jace.shim.testlab.cart;

import jace.shim.testlab.utils.NumberIdGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Test
    @DisplayName("장바구니 id로 조회시 존재하면 해당 장바구니를 리턴한다")
    void getCart() {
        // given
        final Long givenCartId = 10L;
        final Long givenUserId = 1000L;

        final Cart givenCart = new Cart(givenCartId, givenUserId, Collections.emptyList());

        final CartRepository cartRepository = mock(CartRepository.class);
        when(cartRepository.findById(givenCartId)).thenReturn(Optional.of(givenCart));

        CartService sut = new CartService(cartRepository);

        // when
        Cart actual = sut.getCart(givenCartId);

        // then
        assertThat(actual.getId()).isEqualTo(givenCartId);
        assertThat(actual.getUserId()).isEqualTo(givenUserId);
    }

    @Test
    @DisplayName("장바구니 id로 조회시 존재하지 않으면 에러가 발생한다")
    void getCartNotFound() {
        // given
        final Long givenCartId = 10L;

        final CartRepository cartRepository = mock(CartRepository.class);
        when(cartRepository.findById(givenCartId)).thenReturn(Optional.empty());

        CartService sut = new CartService(cartRepository);

        // verify
        assertThatThrownBy(() -> sut.getCart(givenCartId)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cart not found - cartId: 10");
    }

    @Test
    @DisplayName("장바구니 생성시 정상적으로 처리 되면 생성된 장바구니를 리턴한다")
    void createCart() {
        // given
        final Long givenCartId = 10L;
        final Long givenUserId = 1000L;

        mockStatic(NumberIdGenerator.class);
        when(NumberIdGenerator.generate()).thenReturn(givenCartId);

        final CartRepository cartRepository = mock(CartRepository.class);
        when(cartRepository.save(any())).thenReturn(new Cart(10L, givenUserId, Collections.emptyList()));

        CartService sut = new CartService(cartRepository);

        // when
        Cart actual = sut.createCart(givenUserId);

        // then
        assertThat(actual.getId()).isEqualTo(10L);
        assertThat(actual.getUserId()).isEqualTo(givenUserId);
    }

    @Test
    @DisplayName("장바구니에 CartLine을 추가하면 해당 장바구니에 CartLine이 추가되고 장바구니를 리턴한다")
    void addCartLine() {
        // given
        final Long givenCartId = 10L;
        final Long givenUserId = 1000L;
        final Long givenCartLineId = 1000L;

        final CartLineParam givenCartLineParam = new CartLineParam(10000L, 1);

        final Cart givenCart = new Cart(givenCartId, givenUserId, new ArrayList<>());

        final CartRepository cartRepository = mock(CartRepository.class);
        when(cartRepository.findById(givenCartId)).thenReturn(Optional.of(givenCart));
        when(cartRepository.save(any())).thenReturn(new Cart(givenCartId, givenUserId, Collections.singletonList(new CartLine(givenCartLineId, 10000L, 1))));

        CartService sut = new CartService(cartRepository);

        // when
        Cart actual = sut.addCartLine(givenCartId, givenCartLineParam);

        // then
        assertThat(actual.getCartLines().size()).isEqualTo(1);
        assertThat(actual.getCartLines().get(0).getId()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("장바구니에 CartLine을 추가하려고 할 때 장바구니가 존재하지 않으면 에러가 발생한다")
    void addCartLineNotFoundCart() {
        // given
        final Long givenCartId = 10L;
        final CartLineParam givenCartLineParam = new CartLineParam(10000L, 1);

        final CartRepository cartRepository = mock(CartRepository.class);
        when(cartRepository.findById(givenCartId)).thenReturn(Optional.empty());

        CartService sut = new CartService(cartRepository);

        // verify
        assertThatThrownBy(() -> sut.addCartLine(givenCartId, givenCartLineParam)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cart not found - cartId: 10");
    }
}