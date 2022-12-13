package kr.co.glog.app.batch.controller;


import kr.co.glog.batch.service.StockDailyBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyBatchController {

    private final StockDailyBatchService stockDailyBatchService;


    //스프링 스케줄러 / 쿼츠 크론 표현식
    //초		분		시		일			월		요일			연도
    //0-59	0-55	0-23	1-31 / ?	1-12	0-6 / ?		생략가능
    // @Scheduled(cron = "0 31 16 13 * * ")
    // @Scheduled(fixedRate = 999999999)

    // 코스피 코스닥 전 종목 조회 후 stock 테이블에 등록
    @Scheduled(cron = "0 16 16 29 * * ")
    public void runFixCudLog() throws InterruptedException {
        stockDailyBatchService.upsertFromKrxDataAll();
    }


}
