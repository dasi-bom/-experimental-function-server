package com.dasibom.practice.repository.custom;

import static com.dasibom.practice.domain.QDiary.diary;

import com.dasibom.practice.condition.DiaryReadCondition;
import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.StampType;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.dto.DiaryBriefResDto;
import com.dasibom.practice.dto.DiaryDetailResDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomDiaryRepositoryImpl implements CustomDiaryRepository {

    private final JPAQueryFactory queryFactory;

    // 게시글 조회 및 검색
    @Override
    public Slice<DiaryBriefResDto> getDiaryBriefInfoScroll(Long cursorId, DiaryReadCondition condition,
            Pageable pageable) {

        List<Diary> diaryList = queryFactory
                .select(diary)
                .from(diary)
                .where(
                        eqIsDeleted(condition.getIsDeleted()), // 삭제되지 않은 일기만 조회
                        eqTitle(condition.getSearchKeyword()),
                        eqContent(condition.getSearchKeyword()),
                        eqCursorId(cursorId)
                )
                .limit(pageable.getPageSize() + 1) // limit 보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true 를 넣어 알림
                .orderBy(sortDiaryList(pageable)) // 최신순 정렬
                .fetch();

        List<DiaryBriefResDto> briefDiaryInfos = new ArrayList<>();
        for (Diary diary : diaryList) {
            briefDiaryInfos.add(new DiaryBriefResDto(diary));
        }

        boolean hasNext = false;
        if (briefDiaryInfos.size() > pageable.getPageSize()) {
            briefDiaryInfos.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(briefDiaryInfos, pageable, hasNext);
    }

    // 스탬프 별 조회
    @Override
    public List<DiaryDetailResDto> getDiaryDetailList(DiaryReadCondition condition) {
        int limitSize = 3;
        List<Diary> diaryList = queryFactory
                .select(diary)
                .from(diary)
                .where(
                        eqUser(condition.getUser()), // 유저 별 조회
                        eqStamp(condition.getStampType()), // 스탬프 별 조회
                        eqIsDeleted(condition.getIsDeleted()) // 삭제되지 않은 일기만 조회
                )
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc()) // 랜덤으로 조회
                .limit(limitSize)
                .fetch();

        List<DiaryDetailResDto> detailDiaryInfos = new ArrayList<>();
        for (Diary diary : diaryList) {
            detailDiaryInfos.add(new DiaryDetailResDto(diary));
        }

        return detailDiaryInfos;
    }

    // 특정 기준 정렬
    private OrderSpecifier<?> sortDiaryList(Pageable page) {
        // 서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (page.getSort().isEmpty()) {
            return new OrderSpecifier<>(Order.DESC, diary.createdAt);
        } else {
            // 정렬값이 들어 있으면 for 사용하여 값을 가져오기
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져오기
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 세팅
                switch (order.getProperty()) {
                    case "updatedTime":
                        return new OrderSpecifier<>(direction, diary.createdAt);
                }
            }
        }
        return new OrderSpecifier<>(Order.DESC, diary.createdAt);
    }

    //동적 쿼리를 위한 BooleanExpression
    private BooleanExpression eqCursorId(Long cursorId) {
        return (cursorId == null) ? null : diary.id.lt(cursorId); // lt: 작다
    }

    // 삭제된 게시글인지 필터링
    private BooleanExpression eqIsDeleted(Boolean isDeleted) {
        return (isDeleted == null) ? null : diary.isDeleted.eq(isDeleted);
    }

    // 제목에 keyword 포함되어있는지 필터링
    private BooleanExpression eqTitle(String keyword) {
        return (keyword == null) ? null : diary.title.contains(keyword);
    }

    // 내용에 keyword 포함되어있는지 필터링
    private BooleanExpression eqContent(String keyword) {
        return (keyword == null) ? null : diary.content.contains(keyword);
    }

    // 유저 필터링
    private BooleanExpression eqUser(User user) {
        return (user == null) ? null :diary.author.eq(user);
    }

    // 스탬프 필터링
    private BooleanExpression eqStamp(StampType stampType) {
        return (stampType == null) ? null :diary.diaryStamps.any().stamp.stampType.eq(stampType);
    }

}
