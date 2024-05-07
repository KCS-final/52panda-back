package com.kcs3.panda.domain.auction.entity;

import com.kcs3.panda.domain.model.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "ItemDetail")
public class ItemDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="itemDetailId", nullable = false)
    private Long itemDetailId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "itemId", nullable = false)
    private Item item;

    @Column(nullable = false)
    private String itemDetailContent;

    @OneToMany(mappedBy = "itemDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemImage> images;

    @Override
    public String toString() {
        return "ItemDetail{" +
                "id=" + getItemDetailId() +  // BaseEntity에서 상속받은 ID
                ", content='" + itemDetailContent + '\'' +
                ", item=" + (item != null ? "Item[id=" + item.getItemId() + "]" : "null") +
                '}';
    }
}
