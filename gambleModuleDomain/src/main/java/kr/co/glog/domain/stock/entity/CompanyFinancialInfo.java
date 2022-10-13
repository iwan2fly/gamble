package kr.co.glog.domain.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=false)
@Alias("CompanyFinancialInfo")
public class CompanyFinancialInfo extends EntityModel {
    String  receiptNumber;          // 접수번호
    String  companyCode;            // 회사코드
    String  reportCode;             // 보고서코드 1분기 : 11013, 반기 : 11012, 3분기 : 11014, 사업 : 11011
    String  year;                   // 사업연도
    String  subjectDiv;             // 재무제표구분
    String  subjectName;            // 재무제표명
    String  accountId;              // 계정ID     XBRL 표준계정아이디
    String  accountName;            // 계정명
    String  accountDetail;          // 계정상세
    String  kiName;                 // 당기명
    String  kiDate;                 // 당기일자
    String    kiAmount;               // 당기금액
    String  prevKiName;             // 전기명
    String  prevKiDate;             // 전기일자
    String    prevKiAmount;           // 전기금액
    String  prev2KiName;            // 전전기명
    String  prev2KiDate;            // 전전기일자
    String    prev2KiAmount;          // 전전기금액
    int     infoOrder;              // 계정과목 정렬순서
    String  currency;               // 통화단위

}
