package com.kcs3.panda.domain.auction.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kcs3.panda.domain.auction.dto.AuctionItemRequest;
import com.kcs3.panda.domain.auction.dto.CommentRequest;
import com.kcs3.panda.domain.auction.dto.QnaPostRequest;
import com.kcs3.panda.domain.auction.entity.*;
import com.kcs3.panda.domain.auction.repository.*;
import com.kcs3.panda.domain.user.entity.User;
import com.kcs3.panda.domain.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.AP_NORTHEAST_2) // 서울 리전
            .build();
    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final AuctionProgressItemRepository auctionProgressItemRepository;
    @Autowired
    private final AuctionCompleteItemRepository auctionCompleteItemRepository;
    @Autowired
    private final ItemDetailRepository itemDetailRepository;
    @Autowired
    private final ItemQuestionRepository itemQuestionRepository;
    @Autowired
    private final QnaCommentRepository qnaCommentRepository;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    public void postQna(QnaPostRequest request, Long itemId){

        ItemQuestion itemQuestion = new ItemQuestion();
        itemQuestion.setItemDetail(this.findDetailByItemId(itemId));
        itemQuestion.setQuestionContents(request.getQuestionContents());
        itemQuestion.setQuestionUserId(request.getQuestionUserId());
        this.itemQuestionRepository.save(itemQuestion);
    }
    public void deleteQna(Long questionId)
    {
        ItemQuestion itemQuestion = this.findItemQuestionById(questionId);
        if (itemQuestion!=null) {
            this.itemQuestionRepository.delete(itemQuestion);
        }
    }





    public void postComment(CommentRequest request, Long questionId){

        QnaComment comment = new QnaComment();
        comment.setItemQuestion(this.findItemQuestionById(questionId));
        comment.setComment(request.getComment());
        this.qnaCommentRepository.save(comment);
    }
    public void deleteComment(Long questionId)
    {
        ItemQuestion itemQuestion = this.findItemQuestionById(questionId);
        if (itemQuestion!=null) {
            this.itemQuestionRepository.delete(itemQuestion);
        }
    }







    public void postItem(AuctionItemRequest request) throws IOException {

        //유저 관련해서 수정필요
        User user =userRepository.findByUserId(1L);

        // "전체" 지역 찾기
        Region region = regionRepository.findByRegion("전체");
        if (region == null) {
            throw new RuntimeException("Region '전체' not found");
        }

        Item item = new Item();
        item.setCategory(request.category);
        item.setTradingMethod(request.trading_method);
        item.setAuctionComplete(false);
        item.setSeller(user);
        item.setRegion(region);  // 지역 설정


        AuctionProgressItem auctionProgressItem= new AuctionProgressItem();
        auctionProgressItem.setItemTitle(request.title);
        auctionProgressItem.setBidFinishTime(request.finish_time);
        auctionProgressItem.setStartPrice(request.start_price);
        auctionProgressItem.setBuyNowPrice(request.buy_now_price);
        auctionProgressItem.setLocation("전체");  // 여기에 문자열로 "전체" 지정
        auctionProgressItem.setItem(item);

        //썸네일 저장하기
        ArrayList<String> imageUrls = this.saveFiles(request.images);
        if (!imageUrls.isEmpty()) {
            auctionProgressItem.setThumbnail(imageUrls.get(0));  // 첫 번째 이미지 URL을 썸네일로 설정
        } else {
            throw new IOException("이미지가 제공되지 않았습니다.");
        }


        ItemDetail itemDetail = new ItemDetail();
        itemDetail.setItem(item);
        itemDetail.setItemDetailContent(request.contents);

        ItemImage itemImage = new ItemImage();
        itemImage.setItemDetail(itemDetail);
        itemImage.setImageUrls(this.saveFiles(request.images));

        itemRepository.save(item); // Item 저장
        auctionProgressItemRepository.save(auctionProgressItem); // AuctionProgressItem 저장
        itemDetailRepository.save(itemDetail); // ItemDetail 저장
    }

//    이미지저장하고 url 반환

    private ArrayList<String> saveFiles(List<MultipartFile> multipartFiles) throws IOException {
        ArrayList<String> urls = new ArrayList<>();

        for(MultipartFile file:multipartFiles)
        {
            String fileName = file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
            urls.add(amazonS3Client.getUrl(bucket,fileName).toString());
        }
        return urls;
    }
    private ItemDetail findDetailByItemId(Long itemId){
        Optional<ItemDetail> OitemDetail = itemDetailRepository.findByItemId(itemId);
        if (OitemDetail.isPresent()) {
            return OitemDetail.get();
        }
        return null;
    }
    private ItemQuestion findItemQuestionById(long questionId){
        Optional<ItemQuestion> OitemQuestion= itemQuestionRepository.findById(questionId);
        if(OitemQuestion.isPresent())
        {
            return OitemQuestion.get();
        }
        return null;
    }
}