package com.example.Testshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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

    @Column(name = "model_id", unique = true, nullable = false)
    private Integer modelId;

}