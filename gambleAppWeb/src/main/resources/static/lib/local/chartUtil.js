let _ChartUtil = {
	// 바차트
	drawBarChart: function( dataList, dom ) {

		// 데이터
		let seriesData = [];
		let xAxisData = [];
		let min = dataList[0].rateChangeMin;
		let max = dataList[0].rateChangeMax;
		for ( i = 0; i < dataList.length; i++ ) {
			min = min > dataList[i].rateChangeMin ? dataList[i].rateChangeMin : min;
			max = max < dataList[i].rateChangeMax ? dataList[i].rateChangeMax : max;
			xAxisData[i] = dataList[i].rateChangeMin + '~' + dataList[i].rateChangeMax;
			seriesData[i] = dataList[i].stockCount;
		}

		// Initialize the echarts instance based on the prepared dom
		let myChart = echarts.init( document.getElementById( dom ) );
		let option = {
			tooltip: {
				trigger: 'axis',
				axisPointer: {
					type: 'shadow'
				}
			},
			xAxis: {
				type: 'category',
				data: xAxisData
			},
			yAxis: {
				type: 'value'
			},
			series: [
				{
					data: seriesData,
					type: 'bar',
					itemStyle:{
						color: '#d48265',
					},
				}
			]
		};

		option && myChart.setOption(option);
	},
	// 주식 일봉/주봉/월봉/연봉 차트 그리기
	drawStatIndexChart : function( dataLIst, period, dom ) {
		// X축
		let xAxisData = [];
		for ( i = 0; i < dataLIst.length; i++ ) {
			if ( period == 'month' ) xAxisData[i] = dataLIst[i].month;
			else if ( period == 'week' ) xAxisData[i] = dataLIst[i].yearOrder;
			else if ( period == 'day' ) xAxisData[i] = _DateUtil.yyyymmdd(dataLIst[i].tradeDate.substring(4,6) + '.' + dataLIst[i].tradeDate.substring(6,8) );
			else xAxisData[i] = dataLIst[i].year;
		}

		// 데이터
		let seriesData = [];
		let min = dataLIst[0].priceLow;
		let max = dataLIst[0].priceHigh;
		for ( i = 0; i < dataLIst.length; i++ ) {
			let dayData = [ dataLIst[i].priceStart, dataLIst[i].priceFinal, dataLIst[i].priceLow, dataLIst[i].priceHigh ];
			min = min > dataLIst[i].priceLow ? dataLIst[i].priceLow : min;
			max = max < dataLIst[i].priceHigh ? dataLIst[i].priceHigh : max;
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