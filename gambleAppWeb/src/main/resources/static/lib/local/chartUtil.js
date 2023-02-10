let _ChartUtil = {
	// 주식 일봉/주봉/월봉/연봉 차트 그리기
	drawStatIndexChart : function( stockDataList, period, dom ) {
		// X축
		let xAxisData = [];
		for ( i = 0; i < stockDataList.length; i++ ) {
			if ( period == 'month' ) xAxisData[i] = stockDataList[i].month;
			else if ( period == 'week' ) xAxisData[i] = stockDataList[i].yearWeek;
			else if ( period == 'day' ) xAxisData[i] = _DateUtil.yyyymmdd(stockDataList[i].tradeDate.substring(4,6) + '.' + stockDataList[i].tradeDate.substring(6,8) );
			else xAxisData[i] = stockDataList[i].year;
		}

		// 데이터
		let seriesData = [];
		let min = stockDataList[0].priceLow;
		let max = stockDataList[0].priceHigh;
		for ( i = 0; i < stockDataList.length; i++ ) {
			let dayData = [ stockDataList[i].priceStart, stockDataList[i].priceFinal, stockDataList[i].priceLow, stockDataList[i].priceHigh ];
			min = min > stockDataList[i].priceLow ? stockDataList[i].priceLow : min;
			max = max < stockDataList[i].priceHigh ? stockDataList[i].priceHigh : max;
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