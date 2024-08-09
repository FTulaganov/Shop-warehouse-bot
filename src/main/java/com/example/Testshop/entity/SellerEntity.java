package com.example.Testshop.entity;

import com.example.Testshop.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seller")
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", columnDefinition = "text")
    private String name;
    @Column(name = "phone", columnDefinition = "text")
    private String phone;
    @Column(name = "shopName", columnDefinition = "text")
    private String shopName;
    @Column(name = "region", columnDefinition = "text")
    private String region;
    @Column(name = "location_latitude")
    private double location_latitude;
    @Column(name = "location_longitude")
    private double location_longitude ;
    @Column(name = "card", columnDefinition = "text", length = 16)
    private String card;
    @Column(name = "visible")
    private Boolean visible = Boolean.FALSE;
    @Column(name = "step")
    private Status step ;
    @Column(name = "chatId")
    private Long chatId ;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();
}
