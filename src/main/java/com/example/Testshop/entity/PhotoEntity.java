package com.example.Testshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "photo")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token", columnDefinition = "text")
    private String token;
    @Column(name = "file_path", columnDefinition = "text")
    private String filePath;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();
}

