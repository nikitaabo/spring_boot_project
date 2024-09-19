package com.example.spring.dto;

public record CartItemDto(Long id, Long bookId, String bookTitle, int quantity) {
}
