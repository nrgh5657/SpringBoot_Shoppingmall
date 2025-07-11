package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemSearchDto;
import com.example.shop.dto.MainItemDto;
import com.example.shop.dto.QMainItemDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.QItem;
import com.example.shop.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.shop.entity.QItemImg.itemImg;

@Slf4j
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null? null: QItem.item.itemSellStatus.eq(searchSellStatus);
    }
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType)|| searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d", searchDateType)){
            dateTime=dateTime.minusDays(1);
        }else if(StringUtils.equals("1w", searchDateType)){
            dateTime=dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m", searchDateType)){
            dateTime=dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m", searchDateType)){
            dateTime=dateTime.minusDays(6);
        }
        return QItem.item.regTime.after(dateTime);

    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.contains(searchQuery);
        }else if(StringUtils.equals("createBy", searchBy)){
            return QItem.item.createdBy.contains(searchQuery);
        }
        return null;
    }



    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        log.info("itemSearchDto.getSearchDateType() : {}",itemSearchDto.getSearchDateType());
        log.info("itemSearchDto.getItemSellStatus() : {}",itemSearchDto.getSearchSellStatus());
        log.info("searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()) : {}",
                searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()));
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType())
                        ,searchSellStatusEq(itemSearchDto.getSearchSellStatus())
                        ,searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())

                )
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery)? null: QItem.item.itemNm.contains(searchQuery);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results= queryFactory
                .select(
                    new QMainItemDto(
                            item.id,
                            item.itemNm,
                            item.itemDetail,
                            itemImg.imgUrl,
                            item.price

                    )
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
