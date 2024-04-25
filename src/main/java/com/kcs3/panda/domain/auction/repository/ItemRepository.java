package com.kcs3.panda.domain.auction.repository;

import com.kcs3.panda.domain.auction.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  ItemRepository extends JpaRepository<Item, Long> {


    /**
     *  친구 목록 조회
     */
    @Query("selectem p from AuctionProgressIt p join p.  u where f.user1.id = :userId and f.status = 1")
    List<Long> findByItemList(@Param("userId") Long userId);

    @Query("select u.userId from Friend f join f.user1 u where f.user2.id = :userId and f.status = 1")
    List<Long> findByReceiveFriendList1(@Param("userId") Long userId);

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

    public interface AuctionRepository extends JpaRepository<AuctionItem, Long> {


    }


}
