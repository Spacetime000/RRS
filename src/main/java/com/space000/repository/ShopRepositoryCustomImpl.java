package com.space000.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.space000.dto.ShopSearchDto;
import com.space000.entity.QReview;
import com.space000.entity.QShop;
import com.space000.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;

public class ShopRepositoryCustomImpl implements ShopRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ShopRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private BooleanExpression searchCategory(String searchCategory) {
        if (StringUtils.equals("all", searchCategory) || searchCategory == null) {
            return null;
        } else {
            return QShop.shop.category.like(searchCategory);
        }
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("shopName", searchBy)) {
            return QShop.shop.name.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("address", searchBy)) {
            return QShop.shop.roadName.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Shop> getShopBoardPage(ShopSearchDto shopSearchDto, Pageable pageable) {
        List<Shop> result = jpaQueryFactory
                .selectFrom(QShop.shop)
                .where(searchByLike(shopSearchDto.getSearchBy(), shopSearchDto.getSearchQuery()),
                        searchCategory(shopSearchDto.getSearchCategory()))
                .orderBy(QShop.shop.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(Wildcard.count).from(QShop.shop)
                .where(searchByLike(shopSearchDto.getSearchBy(), shopSearchDto.getSearchQuery()),
                        searchCategory(shopSearchDto.getSearchCategory()))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public List<Shop> getBestShop() {
        List<Shop> shopList =  jpaQueryFactory
                .selectFrom(QShop.shop)
                .join(QReview.review)
                .on(QShop.shop.id.eq(QReview.review.shop.id))
                .groupBy(QReview.review.shop.id)
                .having(QReview.review.rating.avg().goe(4),
                        QReview.review.shop.id.count().goe(QReview.review.shop.id.count().mod(QShop.shop.count())))
                .fetch();

        return shopList;

    }
}
