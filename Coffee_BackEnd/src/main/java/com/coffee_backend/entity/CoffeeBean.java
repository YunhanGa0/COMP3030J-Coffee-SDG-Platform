package com.coffee_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "coffee_beans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @Column(nullable = false)
    private String name;  // 如“Geisha Natural 2024 批次”

    private String variety;        // 品种：Typica, Bourbon, etc.
    private String processMethod;  // 处理法：Washed, Natural, Honey
    private String roastLevel;     // 烘焙程度：Light / Medium / Dark

    @Column(length = 300)
    private String flavorNotes;    // 风味描述，如：花香、莓果、坚果

    private Double weightPerBagKg; // 每袋净重（公斤）

    private Integer bagStock;      // 剩余袋数库存

    private Double pricePerBag;    // 每袋价格（例如：200元/袋）

    private Boolean available;     // 是否上架销售
    private Boolean limitedEdition; // 是否为限量豆

    private String imageUrl;       // 图片展示

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
}
