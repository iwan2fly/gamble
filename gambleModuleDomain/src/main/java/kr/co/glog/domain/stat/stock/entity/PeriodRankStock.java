package kr.co.glog.domain.stat.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("PeriodRankStock")
public class PeriodRankStock extends EntityModel {

    String  marketCode;                         // 시장코드
    String	stockCode;                          // 주식코드
    String  periodCode;                         // 주기코드
    Integer year;		                        // 년도
    Integer month;                              // 월
    Integer week;                               // 주차
    String  yearOrder;                          // 년도의 월순서, 주순서
    String  rankCode;                           // 순위코드
    Integer ranking;                            // 순위
    String  rankingValue;                       // 값
}
