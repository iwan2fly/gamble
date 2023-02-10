
let _StrUtil = {
	// 3자리마다 ,
	toCurrency: function(value) {
		// if(isNaN(value)) return value;
		if('-' == value || '' == value) return value;

		try {
			let number;
			if (isNaN(value)) {

				const onlyNumber = /[^0-9\.\-]/g; // 숫자와 점만 허용
				number = value.toString().replace(onlyNumber, '');

				if (isNaN(number)) return value; // 위의 치환과정을 거쳤는데도 number 포맷이 아니면 그냥 그대로 리턴

			} else {
				number = value;
			}

			return new Intl.NumberFormat('ko-KR').format(number);
		} catch {
			return value;
		}
	}

}