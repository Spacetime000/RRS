package com.space000.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.space000.entity.Banner;
import com.space000.entity.QBanner;

import java.util.List;

public class BannerRepositoryCustomImpl implements BannerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public BannerRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Banner> getMainBanner() {
        List<Banner> bannerList = jpaQueryFactory.selectFrom(QBanner.banner)
                .where(QBanner.banner.state.like("노출"))
                .fetch();
        return bannerList;
    }
}
