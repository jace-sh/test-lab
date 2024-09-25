package jace.shim.testlab.cart;

import jace.shim.testlab.utils.JsonUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @AfterEach
    void tearDown() {
        cartRepository.deleteAll();
    }

    @Test
    @DisplayName("장바구니 id로 조회시 존재하면 해당 장바구니를 리턴한다")
    public void getCart() throws Exception {
        final Long givenId = 10L;
        cartRepository.save(new Cart(givenId, 100L, List.of(new CartLine(1000L, 10000L, 1))));

        MvcResult result = mockMvc.perform(get("/carts/{cartId}", givenId))
                .andExpect(status().isOk()).andReturn();


        Cart actual = JsonUtils.fromJson(result.getResponse().getContentAsString(), Cart.class);

        assertThat(actual.getId()).isEqualTo(givenId);
        assertThat(actual.getUserId()).isEqualTo(100L);
        assertThat(actual.getCartLines().get(0).getId()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("장바구니 id로 조회시 존재하지 않으면 에러가 발생한다")
    public void getCartNotFound() throws Exception {
        final Long givenId = 20L;

        mockMvc.perform(get("/carts/{cartId}", givenId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("장바구니 생성시 정상적으로 처리 되면 생성된 장바구니를 리턴한다")
    public void createCart() throws Exception {

        MvcResult result = mockMvc.perform(post("/carts"))
                .andExpect(status().isOk()).andReturn();

        Cart actual = JsonUtils.fromJson(result.getResponse().getContentAsString(), Cart.class);

        Long cartId = actual.getId();

        Cart cart = cartRepository.findById(cartId).get();
        assertThat(cart.getId()).isEqualTo(cartId);
        assertThat(cart.getUserId()).isEqualTo(actual.getUserId());
    }

    @Test
    @DisplayName("장바구니에 CartLine을 추가하면 기존 CartLines에 추가된다")
    public void addCartLine() throws Exception {
        final Long givenId = 10L;
        cartRepository.save(new Cart(givenId, 100L, List.of(new CartLine(1000L, 10000L, 1))));

        CartLineParam cartLineParam = new CartLineParam(20000L, 2);

        mockMvc.perform(post("/carts/{cartId}/cart-lines", givenId)
                .content(JsonUtils.toJson(cartLineParam))
                .contentType("application/json"))
                .andExpect(status().isOk());

        Cart cart = cartRepository.findById(givenId).get();
        assertThat(cart.getCartLines()).hasSize(2);
    }

    @Test
    @DisplayName("장바구니에 CartLine을 추가하려고 할 때 장바구니가 존재하지 않으면 에러가 발생한다")
    public void addCartLineCartNotFound() throws Exception {
        final Long givenId = 10L;
        CartLineParam cartLineParam = new CartLineParam(20000L, 2);

        mockMvc.perform(post("/carts/{cartId}/cart-lines", givenId)
                .content(JsonUtils.toJson(cartLineParam))
                .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }
}