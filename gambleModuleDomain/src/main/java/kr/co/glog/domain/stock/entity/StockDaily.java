package kr.co.glog.domain.stock.entity;

import kr.co.glog.common.model.EntityModel;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.naverStock.model.SiseDay;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StockDaily")
public class StockDaily extends EntityModel {

    Long    stockDailyId;
    String  isin;                       // 국제채권식별번호
    String	stockCode;                  // 종목코드
    String  stockName;                  // 종목명
    String 	tradeDate;		            // 날짜
    Integer	priceFinal;		            // 종가
    Integer	priceChange;	            // 변동
    Integer	priceStart;		            // 시작가
    Integer	priceHigh;		            // 고가
    Integer	priceLow;		            // 저가
    Long    priceTrade;                 // 거래대금
    Long	volumeTrade;		        // 거래량
    Long    priceTotal;                 // 시가총액
    Long	volumeTotal;		        // 전체주식수
    Float 	rateChange;			        // 변동비
    Long	volumeOrg;			        // 기관 순매매
    Long	volumeForeigner;	        // 외인 순매매
    Long	foreignerStockCount;		// 외인보유
    Float	foreignerHoldRate;	        // 외인보유율

    public Integer getPriceYesterday() {
        return getPriceFinal() - getPriceChange();
    }
}
