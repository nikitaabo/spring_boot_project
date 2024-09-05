package com.example.spring.dto;

public record BookSearchParametersDto(String[] title, String[] author, String[] isbn) {
    // Add more search parameters as needed
}

