package kr.co.glog.domain.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=false)
@Alias("CompanyFinance")
public class CompanyFinance extends EntityModel {
    String  companyCode;            // dart corp_code
    String  stockCode;              // 주식코드
    String  yearQuarter;            // 년도분기
    Integer year;                  // 년도
    Integer quarter;               // 분기
    String  subject;                // 분류
    String  account;                // 항목
    Long    amount;                   // 금액
    Integer sortOrder;             // 정렬순서
}
