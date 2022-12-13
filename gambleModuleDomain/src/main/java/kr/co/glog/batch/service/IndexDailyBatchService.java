package kr.co.glog.batch.service;

import kr.co.glog.external.daumFinance.DaumDailyIndexScrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexDailyBatchService {

    private final DaumDailyIndexScrapper daumDailyIndexScrapper;

    /**
     *
     */
    public void upsertDailyData() {

        String batch = "[BATCH] 일별 KOSPI, KOSDAQ 지수 업데이트 - ";

        long startTime = System.currentTimeMillis();
        log.debug( batch + "시작 : " + startTime);

        try {
            daumDailyIndexScrapper.upsertDailyIndex("kospi");
            daumDailyIndexScrapper.upsertDailyIndex("kosdaq");
        } catch ( InterruptedException ie ) {
            long exceptionTime = System.currentTimeMillis();
            log.debug( batch + "에러 : " + exceptionTime);
        } finally {
            long endTime = System.currentTimeMillis();
            log.debug( batch + "종료 : " + endTime);
        }

    }
}
