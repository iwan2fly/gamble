package kr.co.glog.domain.stat.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatIndexDaily")
public class StatIndexDaily extends EntityModel {

    Long    statIndexDailyId;
    String	marketCode;                 // 시장코드
    String 	tradeDate;		            // 날짜
    String 	statNameCode;		        // 통계이름코드
    Integer	statDays;		            // 통계일수
    String	startDate;	                // 통계시작일자

}
