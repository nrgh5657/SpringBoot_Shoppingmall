package com.example.shop.entity;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.repository.ItemRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.shop.entity.QItem.item;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ItemTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;
    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public void testFindById() {
        List<Item> items =
        itemRepository.findByItemNm("지포스5090");
        items.forEach(item->log.info(item.toString()));
        //items.forEach(System.out::println);

        log.info("-----------------------QueryDSL--------------");
        QItem qItem = item;

        List<Item> item2=  queryFactory
                .select(qItem)
                .from(qItem)
                .where(qItem.itemNm.eq("지포스5090"))
                .fetch();
        item2.forEach(item->log.info(item.toString()));

    }
    @Test
    public void testFindByItemNmAndPrice() {
        QItem qItem = item;

        queryFactory
                .selectFrom(qItem)
                .where(qItem.itemNm.eq("지포스5090"),
                        qItem.price.gt(1000000)
                        )
                .fetch();
        log.info(item.toString());
    }

    @Test
    public void testFindByNmOrItemDetail(){
        QItem qItem = QItem.item;

        List<Item> items= queryFactory
                .select(qItem)
                .from(qItem)
                .where(qItem.itemNm.contains("포스")
                        .or(qItem.itemDetail.contains("그래픽"))

                )
                .fetch();

        items.forEach(item->log.info(item.toString()));
    }

    @Test
    public void testFindBySellStatus() {
        QItem qItem = QItem.item;

        List<Item> items= queryFactory
                .selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .fetch();
        items.forEach(item->log.info(item.toString()));
    }
    //동적 조건 검색(BooleanBuilder사용)
    @Test
    public void testDynamicSearch(){
        QItem qItem = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();

        String searchNm = "지포스";
        Integer minPrice = 1000000;

        if(searchNm != null){
            builder.and(qItem.itemNm.contains(searchNm));
        }
        if(minPrice != null){
            builder.and(qItem.price.gt(minPrice));
        }
        List<Item> items = queryFactory
                .selectFrom(qItem)
                .where(builder)
                .fetch();
        items.forEach(item->log.info(item.toString()));
    }

    @Test
    public void testPaging(){
        QItem qItem = QItem.item;

        List<Item> items = queryFactory
                .selectFrom(qItem)
                .where(qItem.price.gt(1000000))
                .orderBy(qItem.price.asc())
                .fetch();
        log.info(items.toString());
    }
    @Test
    public void testPagingAndSort(){
        QItem qItem = QItem.item;

        List<Item> items = queryFactory
                .selectFrom(qItem)
                .where(qItem.price.gt(1000000))
                .orderBy(qItem.price.asc())
                .offset(2)
                .limit(3)
                .fetch();
        log.info(items.toString());

    }
    @Test
    public void testAggreegateFunction(){
        QItem qItem = QItem.item;
        List<Tuple> fetch = queryFactory
                .select(
                        qItem.itemSellStatus,
                        qItem.price.avg()
                )
                .from(qItem)
                .groupBy(qItem.itemSellStatus)
                .fetch();
        fetch.stream().forEach(item->log.info(item.toString()));
    }
    @Test
    public void tesItemImg(){
        QItemImg qItemImg = QItemImg.itemImg;
        List<ItemImg> result = queryFactory
                .selectFrom(qItemImg)
                .where(qItemImg.repimgYn.eq("Y"))
                .fetch();
        result.forEach(item->log.info(item.toString()));
    }

    @Test
    public void testJoin(){
        QItem qItem = QItem.item;
        QItemImg qItemImg = QItemImg.itemImg;

        queryFactory
                .selectFrom(qItemImg)
                .join(qItemImg.item, qItem)
                .where(qItem.itemNm.contains("그래픽"))
                .fetch();
        log.info(item.toString());
    }
    
    @Test
    public void testItemJoin() {
        QItem qItem = QItem.item;
        QItemImg qItemImg = QItemImg.itemImg;

        List<ItemImg> result = queryFactory
                .select(qItemImg)
                .from(qItemImg)
                .join(qItemImg.item, qItem) // ← 이 부분이 핵심
                .where(qItem.itemNm.contains("그래픽"))
                .fetch();

        log.info(result.toString());
    }


}