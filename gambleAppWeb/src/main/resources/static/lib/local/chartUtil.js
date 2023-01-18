let _chart = {
	// 주식 일봉/주봉/월봉/연봉 차트 그리기
	drawStatIndexChart : function( stockDataList, period, dom ) {

		// X축
		let xAxisData = [];
		for ( i = 0; i < stockDatList.length; i++ ) {
			if ( period == 'month' ) xAxisData[i] = stockDatList[i].month;
			else if ( period == 'week' ) xAxisData[i] = stockDatList[i].yearWeek;
			else if ( period == 'day' ) xAxisData[i] = yyyymmdd(stockDatList[i].tradeDate);
			else xAxisData[i] = stockDatList[i].year;
		}

		// 데이터
		let seriesData = [];
		let min = stockDatList[0].priceLow;
		let max = stockDatList[0].priceHigh;
		for ( i = 0; i < stockDatList.length; i++ ) {
			let dayData = [ stockDatList[i].priceStart, stockDatList[i].priceFinal, stockDatList[i].priceLow, stockDatList[i].priceHigh ];
			min = min > stockDatList[i].priceLow ? stockDatList[i].priceLow : min;
			max = max < stockDatList[i].priceHigh ? stockDatList[i].priceHigh : max;
			seriesData[i] = dayData;
		}


		// Initialize the echarts instance based on the prepared dom
		let myChart = echarts.init( document.getElementById( dom ) );

		let option = {
			title: {
				show: false,
			},
			legend: {
				show: false
			},
			tooltip: {
				trigger: "axis",
				axisPointer: {
					type: "cross"
				}
			},
			xAxis: {
				data: xAxisData,
			},
			yAxis: {
				min : parseInt( min * 0.98 ),
				max : parseInt( max * 1.02 ),
			},
			series: [
				{
					type: 'candlestick',
					data: seriesData,
					itemStyle: {
						color: 'red',
						color0: 'blue',
						borderColor: null,
						borderColor0: null
					}
				}
			]
		};

		option && myChart.setOption(option);
	}
}