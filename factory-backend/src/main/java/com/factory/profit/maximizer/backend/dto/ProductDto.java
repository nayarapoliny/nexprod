package com.factory.profit.maximizer.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Value cannot be null")
    @Positive(message = "Value must be positive")
    private BigDecimal value;

    @Valid // This ensures that the validation rules inside ProductCompositionDto are triggered
    private Set<ProductCompositionDto> composition;
}
