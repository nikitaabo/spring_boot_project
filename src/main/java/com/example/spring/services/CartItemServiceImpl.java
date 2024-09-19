package com.example.spring.services;

import com.example.spring.dto.CartItemDto;
import com.example.spring.dto.CartItemUpdateDto;
import com.example.spring.dto.RequestCartItemDto;
import com.example.spring.exception.DataProcessingException;
import com.example.spring.exception.EntityNotFoundException;
import com.example.spring.mapper.CartItemMapper;
import com.example.spring.models.Book;
import com.example.spring.models.CartItem;
import com.example.spring.models.ShoppingCart;
import com.example.spring.models.User;
import com.example.spring.repositories.book.BookRepository;
import com.example.spring.repositories.cart.item.CartItemRepository;
import com.example.spring.repositories.shopping.cart.ShoppingCartRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemDto addCartItem(User user, RequestCartItemDto request) {
        Optional<Book> optionalBook = bookRepository.findById(request.bookId());
        Book book = optionalBook.orElseThrow(
                () -> new EntityNotFoundException("There is not such book"));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser_Email(user.getEmail());
        if (cartItemRepository.existsByBook(book)) {
            throw new DataProcessingException("Cart item with book " + book.getId()
                    + " already exists", null);
        }
        CartItem cartItem = cartItemMapper.toModel(request);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto updateCartItemById(User user, Long id, CartItemUpdateDto request) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is not such car item"));
        cartItem.setQuantity(request.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
