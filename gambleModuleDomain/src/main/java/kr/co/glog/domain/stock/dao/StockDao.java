package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.mapper.StockMapper;
import kr.co.glog.domain.stock.model.StockParam;
import kr.co.glog.domain.stock.model.StockResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class StockDao {

    private final StockMapper stockMapper;

    public ArrayList<StockResult> getCompanyIdList() {
        return stockMapper.selectCompanyIdList();
    }

    // company 테이블의 companyId를 stock 테이블의 companyId로 복사
    public int updateCompanyCodeFromCompany() {
        return stockMapper.updateCompanyCodeFromCompany();
    }

    public StockResult getStock( String stockCode ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );

        StockResult stockResult = null;
        StockParam stockParam = new StockParam();
        stockParam.setStockCode( stockCode );
        ArrayList<StockResult> stockList = stockMapper.selectStockList( stockParam );
        if ( stockList != null && stockList.size() > 0 ) stockResult = stockList.get(0);
        return stockResult;
    }

    public ArrayList<StockResult> getStockList( StockParam stockParam ) {
        if ( stockParam == null ) throw new ParameterMissingException( "stockParam" );

        return stockMapper.selectStockList( stockParam );
    }

    public int insertStock ( Stock stock ) {
        return stockMapper.insertStock( stock );
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param stockList
     * @return
     */
    public int insertsStock( ArrayList<Stock> stockList ) {
        return insertsStock( stockList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param stockList
     * @param insertSize
     * @return
     */
    public int insertsStock( ArrayList<Stock> stockList, int insertSize ) {
        if ( stockList == null ) throw new ParameterMissingException( "stockList" );

        int insertCount = 0;
        int remainSize = stockList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<Stock> subList = (ArrayList)stockList.subList( startIndex, endIndex );
            insertCount += stockMapper.insertsStock( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > stockList.size() ) endIndex = stockList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < stockList.size() ) {
            ArrayList<Stock> subList = (ArrayList) stockList.subList(startIndex, endIndex);
            insertCount += stockMapper.insertsStock(subList);
        }

        return insertCount;
    }

    public int updateStock( Stock stock ) {
        if ( stock == null ) throw new ParameterMissingException( "Stock" );
        if ( stock.getStockCode() == null ) throw new ParameterMissingException( "stockCode" );
        return stockMapper.updateStock( stock );
    }

    public Stock saveStock( Stock stock ) {
        if ( stock == null ) throw new ParameterMissingException( "Stock" );

        if ( stock.getStockCode() == null ) {
            stockMapper.insertStock( stock );
        } else {
            stockMapper.updateStock( stock );
        }

        return stock;
    }

    public Stock saveStock( StockDaily stockDaily ) {
        if ( stockDaily == null ) throw new ParameterMissingException( "stockDaily" );

        Stock stock = new Stock( stockDaily );

        if ( stockDaily.getStockCode() == null ) {
            stockMapper.insertStock( stock );
        } else {
            stockMapper.updateStock( stock );
        }

        return stock;
    }

    public int updateInsert( Stock stock ) {
        int result = updateStock( stock );

        if ( result == 00 ) {
            result = insertStock( stock );
        }

        return result;
    }

    public void deleteStock( String stockCode ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );

        StockParam stockParam = new StockParam();
        stockParam.setStockCode( stockCode );
        stockMapper.deleteStock( stockParam );
    }




}
