package kr.co.glog.external.daumFinance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=false)
public class DaumIndex {

    // 더 많은 정보가 있지만, 일단 필요한 것은 아래와 같습니다.

    String change;                  // RISE = 상승, FAll = 하락 ?
    String changePrice;             // 변동 지수
    String changeRate;              // 변동률
    String tradeDate;               // 20230109
    String basePrice;               // 전일지수
    String openingPrice;            // 시가
    String highPrice;               // 고가
    String lowPrice;                // 저가
    String tradePrice;              // 종가
    String accTradeVolume;          // 거래량(천주)
    String accTradePrice;           // 거래대금(백만)
}

