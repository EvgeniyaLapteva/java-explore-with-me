package ru.practicum.ewm.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Укажите имя для категории")
    private String name;
}
