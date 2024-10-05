
package com.example.spring.controller;

import com.example.spring.dto.CartItemDto;
import com.example.spring.dto.CartItemUpdateDto;
import com.example.spring.dto.RequestCartItemDto;
import com.example.spring.dto.ShoppingCartDto;
import com.example.spring.models.User;
import com.example.spring.services.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "Get an user's cart",
            description = "Receive a certain user's shopping cart")
    public ShoppingCartDto getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCartDto cart = shoppingCartService.getCart(user.getEmail());
        return cart;
    }

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Add an item cart", description = "Add an item cart to the shopping cart")
    public CartItemDto addItemCart(@RequestBody @Valid RequestCartItemDto request,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addCartItem(user, request);
    }

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PutMapping("/items/{id}")
    @Operation(summary = "Update an item cart",
            description = "Update an item cart in the shopping cart")
    public CartItemDto updateCartItem(@RequestBody @Valid CartItemUpdateDto request,
                                      @PathVariable @Positive Long id,
                                      Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItemById(user, id, request);
    }

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete an item cart",
            description = "Delete an item cart in the shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable @Positive Long id) {
        shoppingCartService.deleteById(id);
    }
}
