package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stat.stock.dao.StatIndexDao;
import kr.co.glog.domain.stock.dao.IndexDailyDao;
import kr.co.glog.domain.stock.model.IndexDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatIndexService {

    private final StatIndexDao statIndexDao;
    private final IndexDailyDao indexDailyDao;

    /**
     * 특정 기간 동안의 지수 일반 통계
     * @param startDate
     * @param endDate
     */
    public void makeStatIndex( String marketCode, String startDate, String endDate ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );

        IndexDailyResult indexDailyResult = indexDailyDao.selectStatIndexCommon( marketCode, startDate, endDate );
        log.debug( indexDailyResult.toString() );

        indexDailyResult = indexDailyDao.selectStatIndexPriceStdDev( marketCode, startDate, endDate, indexDailyResult.getAveragePrice() );
        log.debug( indexDailyResult.toString() );

        indexDailyResult = indexDailyDao.selectStatIndexVolumeStdDev( marketCode, startDate, endDate, indexDailyResult.getAverageVolume() );
        log.debug( indexDailyResult.toString() );
   }



}
