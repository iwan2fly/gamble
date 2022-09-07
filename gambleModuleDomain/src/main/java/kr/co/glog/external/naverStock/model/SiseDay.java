package kr.co.glog.external.naverStock.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SiseDay {

    // https://finance.naver.com/item/sise_day.nhn?code=011040&page=1
    String 	date;			// 날짜
    Integer	priceFinal;		// 종가
    Integer	priceChange;	// 변동
    Integer	priceStart;		// 시작가
    Integer	priceHigh;		// 고가
    Integer	priceLow;		// 저가
    Integer	volumn;			// 거래량

    //https://finance.naver.com/item/frgn.naver?code=011040&page=2
    String 	rateChange;			// 변동비
    Integer	volumnOrg;			// 기관 순매매
    Integer	volumnForeigner;	// 외인 순매매
    Long	ownForeigner;		// 외인보유
    String	ownForeignerRate;	// 외인보유율

}
