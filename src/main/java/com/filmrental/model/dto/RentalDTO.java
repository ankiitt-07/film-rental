package com.filmrental.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RentalDTO {
    private Integer rentalId;
    private LocalDateTime rentalDate;
    private Integer inventoryId;
    private Integer customerId;
    private LocalDateTime returnDate;
    private Integer staffId;
}