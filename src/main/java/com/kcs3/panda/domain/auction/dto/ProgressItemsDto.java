package com.kcs3.panda.domain.auction.dto;


import com.kcs3.panda.domain.auction.entity.AuctionCompleteItem;
import com.kcs3.panda.domain.auction.entity.AuctionProgressItem;
import com.kcs3.panda.domain.auction.entity.Item;
import lombok.Builder;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

@Builder
public record ProgressItemsDto(
        String itemTitle,
        String category,
        int tradingMethod,
        String thumbnail,
        int startPrice,
        int currentPrice,
        boolean isAuctionComplete
) {
    public static ProgressItemsDto fromProgressEntity(AuctionProgressItem progressItem) {
        return ProgressItemsDto.builder()
                .itemTitle(progressItem.getItemTitle())
                .category(progressItem.getItem().getCategory().getCategory())
                .tradingMethod(progressItem.getItem().getTradingMethod().getTradingMethod())
                .thumbnail(progressItem.getThumbnail())
                .startPrice(progressItem.getStarPrice())
                .currentPrice(progressItem.getMaxPrice())
                .isAuctionComplete(progressItem.getItem().isAuctionComplete())
                .build();
    }

    public static ProgressItemsDto fromCompletionEntity(AuctionCompleteItem completeItem) {
        return ProgressItemsDto.builder()
                .itemTitle(completeItem.getItemTitle())
                .category(completeItem.getItem().getCategory().getCategory())
                .tradingMethod(completeItem.getItem().getTradingMethod().getTradingMethod())
                .thumbnail(completeItem.getThumbnail())
                .startPrice(completeItem.getStarPrice())
                .currentPrice(completeItem.getMaxPrice())
                .isAuctionComplete(completeItem.getItem().isAuctionComplete())
                .build();
    }



}