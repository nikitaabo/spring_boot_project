package com.example.spring.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.spring.dto.CartItemDto;
import com.example.spring.dto.CartItemUpdateDto;
import com.example.spring.dto.ShoppingCartDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(
        scripts = {
                "classpath:database/roles/clear-role.sql",
                "classpath:database/users/add-user.sql",
                "classpath:database/shopping.carts/add-cart.sql",
                "classpath:database/roles/add-role.sql",
                "classpath:database/users-roles/add-user-role.sql",
                "classpath:database/books/add-book.sql",
                "classpath:database/cart.items/add-cart-items.sql"
        })
@Sql(
        scripts = {
                "classpath:database/cart.items/clear-cart-items.sql",
                "classpath:database/shopping.carts/clear-cart.sql",
                "classpath:database/books/clear-books.sql",
                "classpath:database/users-roles/clear-users-roles.sql",
                "classpath:database/users/clear-users.sql",
                "classpath:database/roles/clear-role.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("testUser@test.com")
    @DisplayName("Get the shopping cart for a user")
    void getCart_ValidUser_Success() throws Exception {
        // Given

        // When
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartDto cartDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);
        assertNotNull(cartDto);
        assertEquals(1L, cartDto.getId().longValue());
        assertEquals(1L, cartDto.getUserId().longValue());
        assertEquals(Set.of(new CartItemDto(1L, 1L, "Book Title", 2)), cartDto.getCartItemDtos());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete an item from the shopping cart")
    void deleteCartItem_ValidId_Success() throws Exception {
        // Given
        Long cartItemId = 1L;

        // When
        mockMvc.perform(delete("/cart/items/{id}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("testUser@test.com")
    @DisplayName("Update an item in the shopping cart")
    void updateCartItem_ValidRequest_Success() throws Exception {
        // Given
        Long cartItemId = 1L;
        CartItemUpdateDto updatedCartItem = new CartItemUpdateDto(5);

        String jsonRequest = objectMapper.writeValueAsString(updatedCartItem);

        // When
        MvcResult result = mockMvc.perform(put("/cart/items/{id}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CartItemDto actualCartItem = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class);
        assertNotNull(actualCartItem);
        assertEquals(updatedCartItem.quantity(), actualCartItem.quantity());
    }
}
