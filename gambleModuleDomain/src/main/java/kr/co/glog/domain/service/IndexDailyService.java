package kr.co.glog.domain.service;


import kr.co.glog.common.exception.NetworkCommunicationFailureException;
import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.dao.IndexDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.external.datagokr.fsc.model.GetMarketIndexInfoResult;
import kr.co.glog.external.daumFinance.DaumDailyIndexScrapper;
import kr.co.glog.external.daumFinance.DaumIndexScrapper;
import kr.co.glog.external.daumFinance.DaumTimelyIndexScrapper;
import kr.co.glog.external.daumFinance.model.DaumDailyIndex;
import kr.co.glog.external.daumFinance.model.DaumIndex;
import kr.co.glog.external.daumFinance.model.DaumTimelyIndex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexDailyService {

    private final IndexDailyDao indexDailyDao;
    private final StockDao stockDao;
    private final DaumDailyIndexScrapper daumDailyIndexScrapper;
    private final DaumIndexScrapper daumIndexScrapper;


    /**
     * 다음 주식 페이지, 지수 상세 데이터를 indexDaily 테이블에 upsert
     * @param marketCode
     */
    public void upsertIndexDailyFromDaumIndex( String marketCode  ) throws InterruptedException {

        DaumIndex daumIndex = null;
        int readCount = 0;
        boolean isRead = false;
        while ( !isRead ) {

            if ( readCount >= 5 ) throw new NetworkCommunicationFailureException( "다음 주식 페이지 지수 목록 조회가 " + readCount + " 번 실패했습니다.");

            try {
                daumIndex = daumIndexScrapper.getDaumIndex( marketCode );
                isRead = true;
            } catch (Exception e) {
                e.printStackTrace();
                readCount++;
                Thread.sleep( 5000 );
            }
        }

        IndexDaily indexDaily = convertToIndexDailyFromDaumIndex( marketCode, daumIndex );
        indexDailyDao.saveIndexDaily( indexDaily );
    }


    /**
     * 다음 주식 페이지, 일별 데이터 첫 페이지 10개 데이터를 indexDaily 테이블에 upsert
     * @param marketCode
     */
    public void upsertIndexDailyFromDaumDaily(String marketCode  ) throws InterruptedException {

        int perPage = 10;

        // 다음
        ArrayList<IndexDaily> indexDailyList = new ArrayList<IndexDaily>();
        ArrayList<DaumDailyIndex> daumDailyIndexList = null;

        int readCount = 0;
        boolean isRead = false;
        while ( !isRead ) {

            if ( readCount >= 5 ) throw new NetworkCommunicationFailureException( "다음 주식 페이지 지수 목록 조회가 " + readCount + " 번 실패했습니다.");

            try {
                daumDailyIndexList = daumDailyIndexScrapper.getDailyIndexList(marketCode, perPage, 1);
                isRead = true;
            } catch (Exception e) {
                e.printStackTrace();
                readCount++;
                Thread.sleep( 5000 );
            }
        }

        // 우리 DB에 맞게 컨버트
        for ( DaumDailyIndex daumDailyIndex : daumDailyIndexList ) {
            IndexDaily indexDaily = convertToIndexDailyFromDaumDailyIndex( marketCode, daumDailyIndex );
            indexDailyList.add( indexDaily );
        }

        // 하나씩 인서트 해보고 이미 있으면 업데이트
        for ( int i = 0; i < indexDailyList.size(); i++ ) {
            indexDailyDao.saveIndexDaily( indexDailyList.get(i) );
        }
    }


    /**
     * 다음 주식의 전체 일별 지수 데이터를 IndexDaily 테이블에 인서트
     * @param marketCode
     */
    public void insertDailyIndexAllFromDaum(String marketCode  ) {

        int perPage = 100;

        ArrayList<IndexDaily> indexDailyList = new ArrayList<IndexDaily>();
        int totalPages = daumDailyIndexScrapper.getTotalPages( marketCode, perPage );

        log.debug( "totalPages : " + totalPages );

        for ( int page = 1; page <= totalPages; page++ ) {

            ArrayList<DaumDailyIndex> daumDailyStockList = daumDailyIndexScrapper.getDailyIndexList( marketCode, perPage, page );

            for ( DaumDailyIndex daumDailyIndex : daumDailyStockList ) {
                IndexDaily indexDaily = convertToIndexDailyFromDaumDailyIndex( marketCode, daumDailyIndex );
                indexDailyList.add( indexDaily );
            }

            try {
                Thread.sleep(5000 );
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        for ( IndexDaily indexDaily : indexDailyList ) {
            log.debug( indexDaily.toString() );
            try {
                indexDailyDao.insertIndexDaily( indexDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {
                indexDailyDao.updateIndexDaily( indexDaily );
                break;
            }
        }

    }


    /**
     * DaumDailyIndex -> IndexDaily
     * @param daumDailyIndex
     */
    public IndexDaily convertToIndexDailyFromDaumDailyIndex(String marketCode, DaumDailyIndex daumDailyIndex ) {

        if ( daumDailyIndex == null ) throw new ParameterMissingException( "daumDailyIndex" );

        log.debug( daumDailyIndex.toString() );


        IndexDaily indexDaily = new IndexDaily();
        indexDaily.setMarketCode( marketCode );
        indexDaily.setTradeDate( daumDailyIndex.getDate().substring(0, 10).replaceAll("-", "") ) ;
        indexDaily.setPriceFinal( daumDailyIndex.getTradePrice() );
        indexDaily.setPriceChange( daumDailyIndex.getChangePrice() );
        indexDaily.setRateChange( (int)( 10000 * daumDailyIndex.getChangePrice() / ( daumDailyIndex.getTradePrice() - daumDailyIndex.getChangePrice() ) )  / 100f ) ;

        log.debug( "" + ( daumDailyIndex.getTradePrice() - daumDailyIndex.getChangePrice()  ) );
        log.debug( "" + ( daumDailyIndex.getChangePrice() / ( daumDailyIndex.getTradePrice() - daumDailyIndex.getChangePrice() ) ) );
        log.debug( "" + ( 10000 * daumDailyIndex.getChangePrice() / ( daumDailyIndex.getTradePrice() - daumDailyIndex.getChangePrice() ) ) );
        log.debug( "" + ( 10000 * daumDailyIndex.getChangePrice() / ( daumDailyIndex.getTradePrice() - daumDailyIndex.getChangePrice() ) / 100f ) );


        log.debug(  daumDailyIndex.getChange() );
        if ( daumDailyIndex.getChange() != null && daumDailyIndex.getChange().equals("FALL") ) {
            indexDaily.setPriceChange( -indexDaily.getPriceChange() );
            indexDaily.setRateChange( -indexDaily.getRateChange() );
        }

        return indexDaily;
    }

    /**
     * DaumIndex -> IndexDaily
     * @param marketCode
     * @param daumIndex
     * @return
     */
    public IndexDaily convertToIndexDailyFromDaumIndex(String marketCode, DaumIndex daumIndex ) {

        if ( daumIndex == null ) throw new ParameterMissingException( "daumIndex" );

        log.debug( daumIndex.toString() );

        IndexDaily indexDaily = new IndexDaily();
        indexDaily.setMarketCode( marketCode );
        indexDaily.setTradeDate( daumIndex.getTradeDate() ) ;
        indexDaily.setPriceStart( Float.parseFloat( daumIndex.getOpeningPrice() ) );
        indexDaily.setPriceHigh( Float.parseFloat( daumIndex.getHighPrice() ) );
        indexDaily.setPriceLow( Float.parseFloat( daumIndex.getLowPrice() ) );
        indexDaily.setPriceFinal( Float.parseFloat( daumIndex.getTradePrice() ) );
        indexDaily.setPriceChange( Float.parseFloat( daumIndex.getChangePrice() ) );
        indexDaily.setRateChange(  (int)(Float.parseFloat( daumIndex.getChangeRate() ) * 10000 ) / 100f ) ;
        indexDaily.setVolumeTrade( Long.parseLong( daumIndex.getAccTradeVolume() ) * 1000 );        // 단위가 천이라
        indexDaily.setPriceTrade( Long.parseLong( daumIndex.getAccTradePrice() ) * 1000000 );       // 단위가 백만이라

        if ( daumIndex.getChange() != null && daumIndex.getChange().equals("FALL") ) {
            indexDaily.setPriceChange( -indexDaily.getPriceChange() );
            indexDaily.setRateChange( -indexDaily.getRateChange() );
        }

        return indexDaily;
    }


    /**
     * FscMarketInf -> IndexDaily
     * @param marketCode
     * @param getMarketIndexInfoResult
     * @return
     */
    public IndexDaily getIndexkDailyFromFscMarketInfo( String marketCode, GetMarketIndexInfoResult getMarketIndexInfoResult ) {

        if ( getMarketIndexInfoResult == null ) throw new ParameterMissingException( "getMarketIndexInfoResult" );
        log.debug( getMarketIndexInfoResult.toString() );

        IndexDaily indexDaily = new IndexDaily();
        indexDaily.setMarketCode( marketCode );                                      // index code : KOSPI, KOSDAQ
        indexDaily.setTradeDate( getMarketIndexInfoResult.getBasDt() ) ;             // 날짜
        indexDaily.setPriceStart( getMarketIndexInfoResult.getMkp() );               // 시가
        indexDaily.setPriceHigh( getMarketIndexInfoResult.getHipr() );               // 고가
        indexDaily.setPriceLow( getMarketIndexInfoResult.getLopr() );                // 저가
        indexDaily.setPriceFinal( getMarketIndexInfoResult.getClpr() );              // 종가
        indexDaily.setPriceChange( getMarketIndexInfoResult.getVs() );               // 대비
        indexDaily.setRateChange( getMarketIndexInfoResult.getFltRt() );             // 등락율
        indexDaily.setVolumeTrade( getMarketIndexInfoResult.getTrqu() );             // 거래량
        indexDaily.setPriceTrade( getMarketIndexInfoResult.getTrPrc() );             // 거래대금


        return indexDaily;
    }

}
