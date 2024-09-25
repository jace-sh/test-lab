package jace.shim.testlab.cart;

import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {
    @Test
    @DisplayName("Cart 생성 검증")
    void createCartInstance() {
        // given
        final Long givenId = 10L;
        final Long givenUserId = 100L;
        final Long givenCartLineId = 1000L;

        // when
        Cart actual = new Cart(givenId, givenUserId, List.of(new CartLine(givenCartLineId, 10000L, 1)));


        // then
        assertThat(actual.getId()).isEqualTo(10L);
        assertThat(actual.getUserId()).isEqualTo(100L);
        assertThat(actual.getCartLines().get(0).getId()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("Cart 생성 검증 - id가 null인 경우 에러가 발생한다")
    void cartIdIsNull() {
        assertThatThrownBy(() -> new Cart(null, 100L, List.of(new CartLine(1000L, 10000L, 1)))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must not be null");
    }

    @Test
    @DisplayName("Cart 생성 검증 - userId가 null인 경우 에러가 발생한다")
    void cartUserIdIsNull() {
        assertThatThrownBy(() -> new Cart(10L, null, List.of(new CartLine(1000L, 10000L, 1)))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("userId must not be null");
    }

    @Test
    @DisplayName("Cart 생성 검증 - cartLines가 null인 경우 에러가 발생한다")
    void cartLinesIsNull() {
        assertThatThrownBy(() -> new Cart(10L, 100L, null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cartLines must not be null");
    }

    @Test
    @DisplayName("장바구니에 CartLine을 추가하면 기존 CartLines에 추가된다")
    void addCartLine() {
        // given
        final Long givenId = 10L;
        final Long givenUserId = 100L;
        final Long givenCartLineId = 1000L;
        final Long givenCartLineId2 = 2000L;

        Cart sut = new Cart(givenId, givenUserId, Lists.list(new CartLine(givenCartLineId, 10000L, 1)));

        // when
        sut.addCartLine(new CartLine(givenCartLineId2, 20000L, 1));

        // then
        assertThat(sut.getCartLines().size()).isEqualTo(2);
        assertThat(sut.getCartLines().get(1).getId()).isEqualTo(2000L);
    }

    // 장바구니에 CartLine 추가시 기 존재하는 경우 에러가 발생하는 테스트 코드를 작성한다


    @Test
    @DisplayName("장바구니에 CartLine 추가시 동일한 상품이 포함된 CartLine이 존재하는 경우 에러가 발생한다")
    void addCartLineExist() {
        // given
        final Long givenId = 10L;
        final Long givenUserId = 100L;
        final Long givenCartLineId = 1000L;

        Cart sut = new Cart(givenId, givenUserId, Lists.list(new CartLine(givenCartLineId, 10000L, 1)));

        // when
        assertThatThrownBy(() -> sut.addCartLine(new CartLine(givenCartLineId, 10000L, 1))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CartLine already exists - productId: 10000");
    }

    @Test
    @DisplayName("장바구니에 null인 CartLine을 추가하면 에러가 발생한다")
    void addNullCartLine() {
        // given
        final Long givenId = 10L;
        final Long givenUserId = 100L;
        final Long givenCartLineId = 1000L;

        Cart sut = new Cart(givenId, givenUserId, Lists.list(new CartLine(givenCartLineId, 10000L, 1)));

        // when
        assertThatThrownBy(() -> sut.addCartLine(null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CartLine must not be null");
    }

    @Test
    @DisplayName("장바구니에 null CartLine을 삭제하면 에러가 발생한다")
    void removeNullCartLine() {
        // given
        final Long givenId = 10L;
        final Long givenUserId = 100L;
        final Long givenCartLineId = 1000L;

        Cart sut = new Cart(givenId, givenUserId, Lists.list(new CartLine(givenCartLineId, 10000L, 1)));

        // when
        assertThatThrownBy(() -> sut.removeCartLine(null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CartLine must not be null");
    }

    // 장바구니에 CartLine을 제거하면 기존 CartLines에 제거되는 테스트 코드를 작성한다
    @Test
    @DisplayName("장바구니에 CartLine을 제거하면 기존 CartLines에서 제거된다")
    void removeCartLine() {
        // given
        final Long givenId = 10L;
        final Long givenUserId = 100L;
        final Long givenCartLineId = 1000L;
        final Long givenCartLineId2 = 2000L;

        Cart sut = new Cart(givenId, givenUserId, Lists.list(new CartLine(givenCartLineId, 10000L, 1), new CartLine(givenCartLineId2, 20000L, 1)));

        // when
        sut.removeCartLine(new CartLine(givenCartLineId, 10000L, 1));

        // then
        assertThat(sut.getCartLines().size()).isEqualTo(1);
        assertThat(sut.getCartLines().get(0).getId()).isEqualTo(2000L);
        assertThat(sut.getCartLines().get(0).getProductId()).isEqualTo(20000L);
    }
}