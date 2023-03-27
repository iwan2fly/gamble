package kr.co.glog.domain.stat.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("DailyRankStock")
public class DailyRankStock extends EntityModel {

    String  marketCode;                         // 시장코드
    String	stockCode;                          // 주식코드
    String  tradeDate;                          // 날짜
    String  rankCode;                           // 순위코드
    Integer ranking;                            // 순위
    String  rankingValue;                       // 값
}

