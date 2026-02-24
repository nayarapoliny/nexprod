package com.factory.profit.maximizer.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialDto {

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Stock quantity cannot be null")
    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private Integer stockQuantity;
}
