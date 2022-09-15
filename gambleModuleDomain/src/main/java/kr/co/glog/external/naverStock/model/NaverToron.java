package kr.co.glog.external.naverStock.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NaverToron {

    String 	date;			// 날짜
    String  title;          // 제목
    String  writer;         // 글쓴이
    String  read;           // 조회
    String  sympathy;       // 공감
    String  anti;           // 비공
}
