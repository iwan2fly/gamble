package kr.co.glog.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString(callSuper = true)
public class PagingParam extends EntityModel {

	String filter		= "";			// 필터, 특정 항목을 like 검색할 때 필요합니다.
	String sortType		= "";			// 정렬 순서 ASC, DESC
	String sortIndex	= "";			// 정렬 항목, 
	
	int currentPage = 1;
	int rows = -1;
	int startIndex = 0;
	int endIndex = 0;
	int limit = 0;
	int totalCount	= 0;				// 왜 totalCount 가 없어요? 그래서 넣었어요
	
	public int getStartIndex() {
		int startIndex = (this.currentPage - 1) * this.rows;
		if ( startIndex < 0 ) startIndex = 0;
		return startIndex;
	}
	
	public int getEndIndex() {
		int endIndex = this.rows;
		return endIndex;
	}

}
