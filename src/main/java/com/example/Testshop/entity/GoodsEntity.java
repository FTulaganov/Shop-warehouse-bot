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
@Table(name = "goods")
public class GoodsEntity {
    @Id
    @Column(name = "code_item", unique = true, nullable = false)
    private String codeItem;
    /*    @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "model_id", nullable = false)
        private ModelEntity model;*/
    @Column(name = "model_id", unique = true, nullable = false)
    private Integer modelId;

}