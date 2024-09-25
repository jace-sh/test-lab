package jace.shim.testlab.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class CartLineTest {
    @Test
    @DisplayName("동일한 id를 가진 CartLine은 동일한 객체이다")
    void idEqualsSameObject() {
        // given
        final Long givenId = 10L;

        // when
        CartLine cartLine1 = new CartLine(givenId, 100L, 1);
        CartLine cartLine2 = new CartLine(givenId, 200L, 2);

        // then
        assertThat(cartLine1).isEqualTo(cartLine2);
    }
}