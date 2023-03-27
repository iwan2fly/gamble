package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.NetworkCommunicationFailureException;
import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stat.stock.dao.DailyRankStockDao;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.PeriodCode;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.model.StockResult;
import kr.co.glog.external.datagokr.fsc.GetStockPriceInfo;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
import kr.co.glog.external.daumFinance.DaumDailyInvestorScrapper;
import kr.co.glog.external.daumFinance.DaumDailyStockScrapper;
import kr.co.glog.external.daumFinance.DaumQuotesScrapper;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.daumFinance.model.DaumInvestorStock;
import kr.co.glog.external.daumFinance.model.DaumQuotes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyRankStockService {

    private final DailyRankStockDao dailyRankStockDao;
    String rankCodeList [] = { "rateChange", "priceTotal", "priceTrade", "volumeTrade", "volumeOrg", "volumeForeigner", "foreignerHoldRate" };

    public void makeDailyRank( String marketCode, String tradeDate ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( tradeDate == null ) throw new ParameterMissingException( "tradeDate" );

        for ( int i = 0; i < rankCodeList.length; i++ ) {
            dailyRankStockDao.deleteDailyRankStock(marketCode, tradeDate, rankCodeList[i] );
            dailyRankStockDao.insertDailyRankFromStockDaily(marketCode, tradeDate, rankCodeList[i] );
        }
    }


}

