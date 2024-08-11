package com.example.Testshop.dto;

import com.example.Testshop.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerDto {
    private String name;
    private Long chatId;
    private String phone;
    private String region;
    private String shopName;
    private Double latitude;
    private Double longitude;
    private String card;
    private Status status;

}
