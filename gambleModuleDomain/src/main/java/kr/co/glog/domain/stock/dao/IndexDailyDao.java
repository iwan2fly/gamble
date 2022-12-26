package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.domain.stock.mapper.IndexDailyMapper;
import kr.co.glog.domain.stock.model.IndexDailyParam;
import kr.co.glog.domain.stock.model.IndexDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class IndexDailyDao {

    private final IndexDailyMapper indexDailyMapper;

    /**
     * 거래일수, 최대값, 최소값, 평균을 구합니다.
     * @param marketCode 시장코드, startDate 시작일, endDate 종료일
     * @return
     */
    public IndexDailyResult selectStatIndexCommon( String marketCode, String startDate, String endDate) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );

        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setMarketCode( marketCode );
        indexDailyParam.setStartDate( startDate );
        indexDailyParam.setEndDate( endDate );
        return indexDailyMapper.selectStatIndexCommon( indexDailyParam );
    }


    /**
     * 지수 가격에 대한 표준편차를 구합니다.
     * @param marketCode 시장코드, startDate 작일, endDate 종료일, averagePrice 평균가격
     * @return
     */
    public IndexDailyResult selectStatIndexPriceStdDev( String marketCode, String startDate, String endDate, Float averagePrice ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( averagePrice == null ) throw new ParameterMissingException( "averagePrice" );

        // 데이터 전체 건수가 필요함
        IndexDailyResult indexDailyResult = this.selectStatIndexCommon( marketCode, startDate, endDate );

        return this.selectStatIndexPriceStdDev( marketCode, startDate, endDate, averagePrice, indexDailyResult.getDataCount() );
    }

    public IndexDailyResult selectStatIndexPriceStdDev( String marketCode, String startDate, String endDate, Float averagePrice, Integer dataCount ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( averagePrice == null ) throw new ParameterMissingException( "averagePrice" );
        if ( dataCount == null ) throw new ParameterMissingException( "dataCount" );

        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setMarketCode( marketCode );
        indexDailyParam.setStartDate( startDate );
        indexDailyParam.setEndDate( endDate );
        indexDailyParam.setAveragePrice( averagePrice );
        indexDailyParam.setDataCount( dataCount );

        return indexDailyMapper.selectStatIndexPriceStdDev( indexDailyParam );
    }


    /**
     * 지수 거래량에 대한 대한 표준편차를 구합니다.
     * @param marketCode 시장코드, startDate 시작일, endDate 종료일, averageVolume 평균 거래량
     * @return
     */
    public IndexDailyResult selectStatIndexVolumeStdDev( String marketCode, String startDate, String endDate, Long averageVolume ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( averageVolume == null ) throw new ParameterMissingException( "averageVolume" );

        // 데이터 전체 건수가 필요함
        IndexDailyResult indexDailyResult = this.selectStatIndexCommon( marketCode, startDate, endDate );

        return this.selectStatIndexVolumeStdDev( marketCode, startDate, endDate, averageVolume, indexDailyResult.getDataCount() );
    }

    public IndexDailyResult selectStatIndexVolumeStdDev( String marketCode, String startDate, String endDate, Long averageVolume, Integer dataCount ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( averageVolume == null ) throw new ParameterMissingException( "averageVolume" );
        if ( dataCount == null ) throw new ParameterMissingException( "dataCount" );

        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setMarketCode( marketCode );
        indexDailyParam.setStartDate( startDate );
        indexDailyParam.setEndDate( endDate );
        indexDailyParam.setAverageVolume( averageVolume );
        indexDailyParam.setDataCount( dataCount );

        return indexDailyMapper.selectStatIndexVolumeStdDev( indexDailyParam );
    }






    // 키 SELECT
    public IndexDailyResult getIndexDaily(Long indexDailyId ) {
        if ( indexDailyId == null ) throw new ParameterMissingException( "indexDailyId" );

        IndexDailyResult indexDailyResult = null;
        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setIndexDailyId( indexDailyId );
        ArrayList<IndexDailyResult> indexDailyList = indexDailyMapper.selectIndexDailyList( indexDailyParam );
        if ( indexDailyList != null && indexDailyList.size() > 0 ) indexDailyResult = indexDailyList.get(0);
        return indexDailyResult;
    }

    // 유니크 SELECT
    public IndexDailyResult getIndexDaily( String marketCode, String tradeDate ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( tradeDate == null ) throw new ParameterMissingException( "tradeDate" );

        IndexDailyResult indexDailyResult = null;
        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setMarketCode( marketCode );
        indexDailyParam.setTradeDate( tradeDate );
        ArrayList<IndexDailyResult> indexDailyList = indexDailyMapper.selectIndexDailyList( indexDailyParam );
        if ( indexDailyList != null && indexDailyList.size() > 0 ) indexDailyResult = indexDailyList.get(0);
        return indexDailyResult;
    }


    public ArrayList<IndexDailyResult> getIndexDailyList(IndexDailyParam indexDailyParam ) {
        if ( indexDailyParam == null ) throw new ParameterMissingException( "indexDailyParam" );

        return indexDailyMapper.selectIndexDailyList( indexDailyParam );
    }

    public int insertIndexDaily ( IndexDaily indexDaily) {
        return indexDailyMapper.insertIndexDaily(indexDaily);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param indexDailyList
     * @return
     */
    public int insertsIndexDaily( ArrayList<IndexDaily> indexDailyList) {
        return insertsIndexDaily(indexDailyList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param indexDailyList
     * @param insertSize
     * @return
     */
    public int insertsIndexDaily(ArrayList<IndexDaily> indexDailyList, int insertSize ) {
        if ( indexDailyList == null ) throw new ParameterMissingException( "indexDailyList" );

        int insertCount = 0;
        int remainSize = indexDailyList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<IndexDaily> subList = (ArrayList) indexDailyList.subList( startIndex, endIndex );
            insertCount += indexDailyMapper.insertsIndexDaily( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > indexDailyList.size() ) endIndex = indexDailyList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < indexDailyList.size() ) {
            ArrayList<IndexDaily> subList = (ArrayList) indexDailyList.subList(startIndex, endIndex);
            insertCount += indexDailyMapper.insertsIndexDaily(subList);
        }

        return insertCount;
    }

    public IndexDaily updateIndexDaily( IndexDaily indexDaily ) {
        if ( indexDaily == null ) throw new ParameterMissingException( "IndexDaily" );
        if ( indexDaily.getMarketCode() == null || indexDaily.getTradeDate() == null ) throw new ParameterMissingException( "시장코드와 날짜는 필수값입니다.");
        indexDailyMapper.updateIndexDaily(indexDaily);
        return indexDaily;
    }

    public IndexDaily saveIndexDaily(IndexDaily indexDaily) {
        if ( indexDaily == null ) throw new ParameterMissingException( "IndexDaily" );
        if ( indexDaily.getMarketCode() == null || indexDaily.getTradeDate() == null ) throw new ParameterMissingException( "시장코드와 날짜는 필수값입니다.");

        try {
            indexDailyMapper.insertIndexDaily(indexDaily);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            indexDailyMapper.updateIndexDaily(indexDaily);
        }

        return indexDaily;
    }

    public void deleteIndexDaily( Long indexDailyId ) {
        if ( indexDailyId == null ) throw new ParameterMissingException( "indexDailyId" );

        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setIndexDailyId( indexDailyId );
        indexDailyMapper.deleteIndexDaily( indexDailyParam );
    }




}
