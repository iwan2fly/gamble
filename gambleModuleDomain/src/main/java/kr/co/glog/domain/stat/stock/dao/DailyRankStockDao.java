package kr.co.glog.domain.stat.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stat.stock.entity.DailyRankStock;
import kr.co.glog.domain.stat.stock.mapper.DailyRankStockMapper;
import kr.co.glog.domain.stat.stock.model.DailyRankStockParam;
import kr.co.glog.domain.stat.stock.model.DailyRankStockResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class DailyRankStockDao {

    private final DailyRankStockMapper dailyRankStockMapper;


    public int insertDailyRankFromStockDaily( String marketCode, String tradeDate, String rankCode ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( tradeDate == null ) throw new ParameterMissingException( "tradeDate" );
        if ( rankCode == null ) throw new ParameterMissingException( "rankCode" );

        DailyRankStockParam dailyRankStockParam = new DailyRankStockParam();
        dailyRankStockParam.setMarketCode( marketCode );
        dailyRankStockParam.setTradeDate( tradeDate );
        dailyRankStockParam.setRankCode( rankCode );

        return dailyRankStockMapper.insertDailyRankFromStockDaily(dailyRankStockParam);
    }

    // 유니크 SELECT
    public DailyRankStockResult getDailyRankStock(String stockCode, String tradeDate, String rankCode ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( tradeDate == null ) throw new ParameterMissingException( "tradeDate" );
        if ( rankCode == null ) throw new ParameterMissingException( "rankCode" );

        DailyRankStockResult dailyRankStockResult = null;
        DailyRankStockParam dailyRankStockParam = new DailyRankStockParam();
        dailyRankStockParam.setStockCode( stockCode );
        dailyRankStockParam.setTradeDate( tradeDate );
        dailyRankStockParam.setRankCode( rankCode );
        ArrayList<DailyRankStockResult> dailyRankStockList = dailyRankStockMapper.selectDailyRankStockList( dailyRankStockParam );
        if ( dailyRankStockList != null && dailyRankStockList.size() > 0 ) dailyRankStockResult = dailyRankStockList.get(0);
        return dailyRankStockResult;
    }


    public ArrayList<DailyRankStockResult> getDailyRankStockList(DailyRankStockParam dailyRankStockParam ) {
        if ( dailyRankStockParam == null ) throw new ParameterMissingException( "dailyRankStockParam" );

        return dailyRankStockMapper.selectDailyRankStockList( dailyRankStockParam );
    }

    public int insertDailyRankStock ( DailyRankStock dailyRankStock ) {

        if ( dailyRankStock == null ) throw new ParameterMissingException( "DailyRankStock" );
        if ( dailyRankStock.getStockCode() == null || dailyRankStock.getTradeDate() == null || dailyRankStock.getRankCode() == null) throw new ParameterMissingException( "주식코드, 날짜, 순위코드는 필수값입니다.");

        return dailyRankStockMapper.insertDailyRankStock(dailyRankStock);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param dailyRankStockList
     * @return
     */
    public int insertsDailyRankStock( ArrayList<DailyRankStock> dailyRankStockList) {
        return insertsDailyRankStock(dailyRankStockList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param dailyRankStockList
     * @param insertSize
     * @return
     */
    public int insertsDailyRankStock(ArrayList<DailyRankStock> dailyRankStockList, int insertSize ) {
        if ( dailyRankStockList == null ) throw new ParameterMissingException( "dailyRankStockList" );

        int insertCount = 0;
        int remainSize = dailyRankStockList.size();
        int startStock = 0;
        int endStock = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<DailyRankStock> subList = (ArrayList) dailyRankStockList.subList( startStock, endStock );
            insertCount += dailyRankStockMapper.insertsDailyRankStock( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startStock += insertSize;           // startStock 를 다음으로
            endStock += insertSize;             // endStock를 다음으로
            if ( endStock > dailyRankStockList.size() ) endStock = dailyRankStockList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startStock < dailyRankStockList.size() ) {
            ArrayList<DailyRankStock> subList = (ArrayList) dailyRankStockList.subList(startStock, endStock);
            insertCount += dailyRankStockMapper.insertsDailyRankStock(subList);
        }

        return insertCount;
    }

    public DailyRankStock updateDailyRankStock(DailyRankStock dailyRankStock) {
        if ( dailyRankStock == null ) throw new ParameterMissingException( "DailyRankStock" );
        if ( dailyRankStock.getStockCode() == null || dailyRankStock.getTradeDate() == null || dailyRankStock.getRankCode() == null) throw new ParameterMissingException( "주식코드, 날짜, 순위코드는 필수값입니다.");
        dailyRankStockMapper.updateDailyRankStock(dailyRankStock);
        return dailyRankStock;
    }

    public DailyRankStock insertUpdateDailyRankStock(DailyRankStock dailyRankStock) {
        log.debug( dailyRankStock.toString() );
        if ( dailyRankStock == null ) throw new ParameterMissingException( "DailyRankStock" );
        if ( dailyRankStock.getStockCode() == null || dailyRankStock.getTradeDate() == null || dailyRankStock.getRankCode() == null) throw new ParameterMissingException( "주식코드, 날짜, 순위코드는 필수값입니다.");

        try {
            dailyRankStockMapper.insertDailyRankStock(dailyRankStock);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            dailyRankStockMapper.updateDailyRankStock(dailyRankStock);
        }

        return dailyRankStock;
    }

    public DailyRankStock updateInsertDailyRankStock(DailyRankStock dailyRankStock) {
        log.debug( dailyRankStock.toString() );
        if ( dailyRankStock == null ) throw new ParameterMissingException( "DailyRankStock" );
        if ( dailyRankStock.getStockCode() == null || dailyRankStock.getTradeDate() == null || dailyRankStock.getRankCode() == null) throw new ParameterMissingException( "주식코드, 날짜, 순위코드는 필수값입니다.");

        try {
            if ( dailyRankStockMapper.updateDailyRankStock(dailyRankStock) == 0 ) {
                dailyRankStockMapper.insertDailyRankStock(dailyRankStock);
            }

        } catch ( Exception e ) {
            dailyRankStockMapper.insertDailyRankStock(dailyRankStock);
        }

        return dailyRankStock;
    }

    public int deleteDailyRankStock( String marketCode, String tradeDate, String rankCode ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( tradeDate == null ) throw new ParameterMissingException( "tradeDate" );
        if ( rankCode == null ) throw new ParameterMissingException( "rankCode" );

        DailyRankStockParam dailyRankStockParam = new DailyRankStockParam();
        dailyRankStockParam.setMarketCode( marketCode );
        dailyRankStockParam.setTradeDate( tradeDate );
        dailyRankStockParam.setRankCode( rankCode );

        log.debug( dailyRankStockParam.toString() );

        return dailyRankStockMapper.deleteDailyRankStock(dailyRankStockParam);
    }


}
