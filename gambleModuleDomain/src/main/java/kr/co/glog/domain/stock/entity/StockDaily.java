package kr.co.glog.domain.stock.entity;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.common.model.EntityModel;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.daumFinance.model.DaumInvestorStock;
import kr.co.glog.external.naverStock.model.SiseDay;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StockDaily")
@Slf4j
public class StockDaily extends EntityModel {

    Long    stockDailyId;
    String  isin;                       // 국제채권식별번호
    String	stockCode;                  // 종목코드
    String  marketCode;                 // 시장코드
    String  stockName;                  // 종목명
    String 	tradeDate;		            // 날짜
    Integer pricePrevious;              // 전일종가
    Integer	priceChange;	            // 변동
    Integer	priceStart;		            // 시가
    Integer	priceLow;		            // 저가
    Integer	priceHigh;		            // 고가
    Integer	priceFinal;		            // 종가
    Long    priceTrade;                 // 거래대금
    Long	volumeTrade;		        // 거래량
    Long    priceTotal;                 // 시가총액
    Long	volumeTotal;		        // 전체주식수
    Float 	rateChange;			        // 변동비
    Long	volumeOrg;			        // 기관 순매매
    Long	volumeForeigner;	        // 외인 순매매
    Long	foreignerStockCount;		// 외인보유
    Float	foreignerHoldRate;	        // 외인보유율

    public StockDaily() {

    }

    public StockDaily( DaumDailyStock daumDailyStock ) {
        if ( daumDailyStock == null ) throw new ParameterMissingException( "daumDailyStock" );

        this.setStockCode( daumDailyStock.getSymbolCode() != null ? daumDailyStock.getSymbolCode().substring( 1 ) : "" );
        this.setTradeDate( daumDailyStock.getDate().substring(0, 10).replaceAll("-", "") ) ;
        this.setPriceFinal( daumDailyStock.getTradePrice() );
        this.setPriceChange( daumDailyStock.getChangePrice() );
        this.setPriceStart( daumDailyStock.getOpeningPrice() );
        this.setPriceHigh( daumDailyStock.getHighPrice() );
        this.setPriceLow( daumDailyStock.getLowPrice() );
        this.setVolumeTrade( daumDailyStock.getAccTradeVolume() );
        this.setPriceTrade( daumDailyStock.getAccTradePrice() );
        this.setRateChange( (int)( Float.parseFloat( daumDailyStock.getChangeRate() ) * 10000 ) / 100f );

        log.debug( this.toString() );
        if ( daumDailyStock.getChange() != null && daumDailyStock.getChange().equals("FALL") ) this.setPriceChange( -this.getPriceChange() );

        this.setPricePrevious( this.getPriceFinal() - this.getPriceChange() );  // 정일종가 =  오늘종가 - 대비
    }

    public StockDaily( DaumInvestorStock daumInvestorStock ) {
        if ( daumInvestorStock == null ) throw new ParameterMissingException( "daumInvestorStock" );

        this.setTradeDate( daumInvestorStock.getDate().substring(0, 10).replaceAll("-", "") ) ;
        this.setVolumeOrg( daumInvestorStock.getInstitutionStraightPurchaseVolume() );
        this.setVolumeForeigner( daumInvestorStock.getForeignStraightPurchaseVolume() );
        this.setForeignerStockCount( daumInvestorStock.getForeignOwnShares() );
        this.setForeignerHoldRate( (int)( daumInvestorStock.getForeignOwnSharesRate() * 10000) / 100f );
    }

    public StockDaily(GetStockPriceInfoResult getStockPriceInfoResult) {
        if ( getStockPriceInfoResult == null ) throw new ParameterMissingException( "getPriceStockInfoResult" );

        this.setIsin( getStockPriceInfoResult.getIsinCd() );                  // isin code
        this.setTradeDate( getStockPriceInfoResult.getBasDt() ) ;             // 날짜
        this.setStockCode( getStockPriceInfoResult.getSrtnCd() );             // 종목코드(6자리)
        this.setMarketCode( getStockPriceInfoResult.getMrktCtg().toLowerCase() );           // 시장코드
        this.setStockName( getStockPriceInfoResult.getItmsNm() );             // 종목명
        this.setPriceStart( getStockPriceInfoResult.getMkp() );               // 시가
        this.setPriceHigh( getStockPriceInfoResult.getHipr() );               // 고가
        this.setPriceLow( getStockPriceInfoResult.getLopr() );                // 저가
        this.setPriceFinal( getStockPriceInfoResult.getClpr() );              // 종가
        this.setPriceChange( getStockPriceInfoResult.getVs() );               // 대비
        this.setRateChange( getStockPriceInfoResult.getFltRt() );             // 등락율
        this.setVolumeTrade( getStockPriceInfoResult.getTrqu() );             // 거래량
        this.setPriceTrade( getStockPriceInfoResult.getTrPrc() );             // 거래대금
        this.setVolumeTotal( getStockPriceInfoResult.getLstgStCnt() );        // 주식수
        this.setPriceTotal( getStockPriceInfoResult.getMrktTotAmt() );        // 시총

        this.setPricePrevious( this.getPriceFinal() - this.getPriceChange() );  // 정일종가 =  오늘종가 - 대비
    }

    public Integer getPriceYesterday() {
        return getPriceFinal() - getPriceChange();
    }
}
