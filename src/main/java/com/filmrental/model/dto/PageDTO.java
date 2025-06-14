package com.filmrental.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageDTO<T> {
    private List<T> content; // List of items (e.g., CityDTO) for the current page
    private int number;      // Current page number (0-based)
    private int size;        // Number of items per page
    private long totalElements; // Total number of items across all pages
    private int totalPages;  // Total number of pages
}
