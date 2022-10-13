package kr.co.glog.external.dartApi.model;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.lang.invoke.StringConcatException;

@Getter
@Setter
@ToString(callSuper=false)
public class DartFinancialInfo {

    String  recept_no;              // 접수번호
    String  reprt_code;             // 보고서코드 1분기 : 11013, 반기 : 11012, 3분기 : 11014, 사업 : 11011
    String  bsns_year;              // 사업연도
    String  corp_code;              // 고유번호
    String  sj_div;                 // 재무제표구분
    String  sj_nm;                  // 재무제표명
    String  account_id;             // 계정ID     XBRL 표준계정아이디
    String  account_nm;             // 계정명
    String  account_detail;         // 계정상세
    String  thstrm_nm;              // 당기명
    String  thstrm_amount;          // 당기금액
    String  thstrm_add_amount;      // 당기누적금액
    String  frmtrm_nm;              // 전기명
    String  frmtrm_amount;          // 전기금액
    String  bfefrmtrm_nm;           // 전전기명
    String  bfefrmtrm_amount;       // 전전기금액
    String  ord;                    // 계정과목 정렬순서
    String  currency;               // 통화단위

}
