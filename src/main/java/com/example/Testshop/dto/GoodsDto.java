package com.example.Testshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDto {
    private String codeItem;
    private String name;
    private String model;
    private Double price;
    private Double bonus;
    private LocalDateTime createdDate;
}

