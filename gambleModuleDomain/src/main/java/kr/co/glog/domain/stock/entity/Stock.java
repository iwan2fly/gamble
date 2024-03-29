package kr.co.glog.domain.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=false)
@Alias("Stock")
public class Stock extends EntityModel {

    String  stockCode;                  // 종목코드
    String  stockName;                  // 종목명
    Integer currentPrice;               // 현재가
    String  stockTypeCode;              // 주식종류코드 ( 보통주, 우선주 )
    String  marketTypeCode;             // 시장구분코드 ( KOSPI, KOSDAQ, KOTC, KONEX, ETC

    Long    stockCount;                 // 상장주식수
    String  listedDate;                 // 상장일
    Integer facePrice;                  // 액면가
    Long    foreignersStockLimit;       // 외국인보유한도주식수
    Long    foreignersStockCount;       // 외욱인보유주식수
    Float   foreignersHoldRate;         // 외국인한도소진율

}
