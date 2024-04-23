package com.kcs3.panda.domain.auction.board.entity;

import com.kcs3.panda.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;


@Entity
@Data
public class AuctionCompleteItem extends BaseEntity {

    private Item item;
    @Column(nullable = false)
    private String ItemTitle;
    @Column(nullable=false)
    private int starPrice;
    private int buyNowPrice;
    private LocalDateTime bidFinishTime;
    @Column(nullable=false)
    private String location;
    @Column(nullable=false)
    private int maxPrice;
    private String maxPersonID;
}
