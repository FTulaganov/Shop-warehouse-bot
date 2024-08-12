package com.example.Testshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "models")
public class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "price")
    private Double price;

    @Column(name = "bonus")
    private Double bonus;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

}
