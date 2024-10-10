package com.example.spring.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.spring.dto.CartItemDto;
import com.example.spring.dto.RequestCartItemDto;
import com.example.spring.dto.ShoppingCartDto;
import com.example.spring.exception.EntityNotFoundException;
import com.example.spring.exception.NotUniqueCartItem;
import com.example.spring.mapper.CartItemMapper;
import com.example.spring.mapper.ShoppingCartMapper;
import com.example.spring.models.Book;
import com.example.spring.models.CartItem;
import com.example.spring.models.ShoppingCart;
import com.example.spring.models.User;
import com.example.spring.repositories.book.BookRepository;
import com.example.spring.repositories.cart.item.CartItemRepository;
import com.example.spring.repositories.shopping.cart.ShoppingCartRepository;
import com.example.spring.services.ShoppingCartServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Add cart item to the shopping cart")
    public void addCartItem_ValidData_Success() {
        // Given
        User user = new User();
        user.setEmail("user@example.com");

        Book book = new Book();
        book.setId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        final RequestCartItemDto requestCartItemDto = new RequestCartItemDto(1L, 1);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(1);
        when(bookRepository.findById(requestCartItemDto.bookId())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserEmail(user.getEmail())).thenReturn(shoppingCart);
        when(cartItemRepository.existsByBook(book)).thenReturn(false);
        when(cartItemMapper.toModel(requestCartItemDto)).thenReturn(cartItem);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(
                new CartItemDto(1L, book.getId(), book.getTitle(), 1));

        // When
        CartItemDto result = shoppingCartService.addCartItem(user, requestCartItemDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.bookId().longValue());
        assertEquals(1, result.quantity());
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when not existing book is provided")
    public void addCartItem_NotExistingBook_ShouldEntityNotFoundException() {
        // Given
        User user = new User();
        user.setEmail("user@example.com");

        RequestCartItemDto requestCartItemDto = new RequestCartItemDto(1L, 1);

        when(bookRepository.findById(requestCartItemDto.bookId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.addCartItem(user, requestCartItemDto));
    }

    @Test
    @DisplayName("Should throw DataProcessingException when existing cart item is provided")
    public void addCartItem_ExistingBook_ShouldThrowDataProcessingException() {
        // Given
        User user = new User();
        user.setEmail("user@example.com");

        Book book = new Book();
        book.setId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        RequestCartItemDto requestCartItemDto = new RequestCartItemDto(1L, 1);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);

        when(bookRepository.findById(requestCartItemDto.bookId())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserEmail(user.getEmail())).thenReturn(shoppingCart);
        when(cartItemRepository.existsByBook(book)).thenReturn(true);
        when(cartItemRepository.findByBook(book)).thenReturn(List.of(cartItem));

        // When & Then
        assertThrows(NotUniqueCartItem.class,
                () -> shoppingCartService.addCartItem(user, requestCartItemDto));
    }

    @Test
    @DisplayName("Get shopping shopping.carts")
    void getCart_GivenValidEmail_ShouldReturnShoppingCartDto() {
        // Given
        String email = "user@example.com";
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();

        when(shoppingCartRepository.findByUserEmail(email)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        // When
        ShoppingCartDto result = shoppingCartService.getCart(email);

        // Then
        assertNotNull(result);
        assertEquals(shoppingCartDto, result);
        verify(shoppingCartRepository, times(1)).findByUserEmail(email);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
    }
}
