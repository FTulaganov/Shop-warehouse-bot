package com.example.Testshop.entity;

import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.Status;
import com.example.Testshop.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "password", columnDefinition = "text")
    private String password;

    @Column(name = "role")
    private UserRole role;
    @Column(name = "visible")
    private Boolean visible = Boolean.FALSE;
    @Column(name = "step")
    private AdminStep step ;
    @Column(name = "chatId")
    private Long chatId ;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();


}
