package kr.co.glog.domain.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=false)
@Alias("DartCompany")
public class DartCompany extends EntityModel {
    String  corpCode;               // dart corp_code
    String  companyName;           // 정식회사명칭
    String  companyNameEng;        // 영문정식회사명칭
    String  stockName;          // 종목명(상장사) 또는 약식명칭(기타법인)
    String  stockCode;          // 상장회사인 경우 주식의 종목코드
    String  ceoName;              // 대표자명
    String  companyClass;         // 법인구분 Y(유가), K(코스닥), N(코넥스), E(기타)
    String  companyNo;            // 법인등록번호
    String  businessNo;          // 사업자등록번호
    String  address;              // 주소
    String  homepage;              // 홈페이지 URL
    String  ir;              // IR URL
    String  phone;              // 전화번호
    String  fax;              // 팩스번호
    String  industryCode;         // 업종코드
    String  establishDate;              // 설립일
    String  accountsMonth;              // 결산월
    String  modifyDate;                 // DART 수정일자
}
