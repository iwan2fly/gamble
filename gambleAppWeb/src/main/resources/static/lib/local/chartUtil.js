let _ChartUtil = {
	// 평균
	calculateMA: function(dayCount, data) {
		let result = [];
		for (let i = 0, len = data.length; i < len; i++) {
			if (i < dayCount) {
				result.push('-');
				continue;
			}
			let sum = 0;
			for (let j = 0; j < dayCount; j++) {
				sum += +data[i - j][1];
			}
			result.push((sum / dayCount).toFixed(2));
		}
		return result;
	},
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
	drawStatIndexChart : function( dataList, period, dom ) {

		const colorList = ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'];
		const labelFont = 'bold 12px Sans-serif';

		// X축
		let xAxisData = [];
		for ( i = 0; i < dataList.length; i++ ) {
			if ( period == 'month' ) xAxisData[i] = dataList[i].month;
			else if ( period == 'week' ) xAxisData[i] = dataList[i].yearOrder;
			else if ( period == 'day' ) xAxisData[i] = _DateUtil.yyyymmdd(dataList[i].tradeDate.substring(4,6) + '.' + dataList[i].tradeDate.substring(6,8) );
			else xAxisData[i] = dataList[i].year;
		}

		// 데이터
		let seriesData = [];
		let seriesVolume = [];
		let min = dataList[0].priceLow;
		let max = dataList[0].priceHigh;
		for ( i = 0; i < dataList.length; i++ ) {
			let dayData = [ dataList[i].priceStart, dataList[i].priceFinal, dataList[i].priceLow, dataList[i].priceHigh, dataList[i].volumeTrade ];
			min = min > dataList[i].priceLow ? dataList[i].priceLow : min;
			max = max < dataList[i].priceHigh ? dataList[i].priceHigh : max;
			seriesData[i] = dayData;
			seriesVolume[i] = dataList[i].volumeTrade;
		}

		const dataMA5 = this.calculateMA(5, seriesData);
		const dataMA10 = this.calculateMA(10, seriesData);
		const dataMA20 = this.calculateMA(20, seriesData);
		const dataMA60 = this.calculateMA(60, seriesData);


		// Initialize the echarts instance based on the prepared dom
		let myChart = echarts.init( document.getElementById( dom ) );

		let option = {
			animation: false,
			color: colorList,
			title: {
				show : false,
				left: 'center',
				text: ''
			},
			legend: {
				show : true,
				data : ['1', '5', '10', '20', '30']
			},
			tooltip: {
				triggerOn: 'none',
				transitionDuration: 0,
				confine: true,
				borderRadius: 4,
				borderWidth: 1,
				borderColor: '#333',
				backgroundColor: 'rgba(255,255,255,0.9)',
				textStyle: {
					fontSize: 10,
					color: '#333'
				},
				position: function (pos, params, el, elRect, size) {
					const obj = {
						top: 60
					};
					obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 5;
					return obj;
				}
			},
			axisPointer: {
				link: [
					{
						xAxisIndex: [0, 1]
					}
				]
			},
			dataZoom: [
				{
					type: 'slider',
					xAxisIndex: [0, 1],
					realtime: false,
					start: 20,
					end: 70,
					top: 65,
					height: 20,
					handleIcon:
						'path://M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
					handleSize: '120%'
				},
				{
					type: 'inside',
					xAxisIndex: [0, 1],
					start: 40,
					end: 70,
					top: 30,
					height: 20
				}
			],
			xAxis: [
				{
					type: 'category',
					data: xAxisData,
					boundaryGap: false,
					axisLine: { lineStyle: { color: '#777' } },
					/*
					axisLabel: {
						formatter: function (value) {
							return echarts.format.formatTime('MM-dd', value);
						}
					},
					*/
					min: 'dataMin',
					max: 'dataMax',
					axisPointer: {
						show: true
					}
				},
				{
					type: 'category',
					gridIndex: 1,
					data: xAxisData,
					boundaryGap: false,
					splitLine: { show: false },
					axisLabel: { show: false },
					axisTick: { show: false },
					axisLine: { lineStyle: { color: '#777' } },
					min: 'dataMin',
					max: 'dataMax',
					axisPointer: {
						type: 'line',
						label: { show: false },
						triggerTooltip: true,
						handle: {
							show: true,
							margin: 30,
							color: '#B80C00'
						}
					}
				}
			],
			yAxis: [
				{
					scale: true,
					splitNumber: 2,
					axisLine: { lineStyle: { color: '#777' } },
					splitLine: { show: true },
					axisTick: { show: false },
					axisLabel: {
						inside: true,
						formatter: '{value}\n'
					}
				},
				{
					scale: true,
					gridIndex: 1,
					splitNumber: 2,
					axisLabel: { show: false },
					axisLine: { show: false },
					axisTick: { show: false },
					splitLine: { show: false }
				}
			],
			grid: [
				{
					left: 20,
					right: 20,
					top: 110,
					height: 120
				},
				{
					left: 20,
					right: 20,
					height: 40,
					top: 260
				}
			],
			graphic: [
				{
					type: 'group',
					left: 'center',
					top: 70,
					width: 300,
					bounding: 'raw',
					children: [
						{
							id: 'MA5',
							type: 'text',
							style: { fill: colorList[1], font: labelFont },
							left: 0
						},
						{
							id: 'MA10',
							type: 'text',
							style: { fill: colorList[2], font: labelFont },
							left: 'center'
						},
						{
							id: 'MA20',
							type: 'text',
							style: { fill: colorList[3], font: labelFont },
							left: 'center'
						},
						{
							id: 'MA60',
							type: 'text',
							style: { fill: colorList[4], font: labelFont },
							right: 0
						}
					]
				}
			],
			series: [
				{
					name: 'Volume',
					type: 'bar',
					xAxisIndex: 1,
					yAxisIndex: 1,
					itemStyle: {
						color: '#7fbe9e'
					},
					emphasis: {
						itemStyle: {
							color: '#140'
						}
					},
					data: seriesVolume
				},
				{
					name: '당일',
					type: 'candlestick',
					data: seriesData,
					itemStyle: {
						color: 'red',
						color0: 'blue',
						borderColor: 'red',
						borderColor0: 'blue'
					},
					emphasis: {
						itemStyle: {
							color: 'black',
							color0: '#444',
							borderColor: 'black',
							borderColor0: '#444'
						}
					}
				},
				{
					name: '5일',
					type: 'line',
					data: dataMA5,
					smooth: true,
					showSymbol: false,
					lineStyle: {
						width: 1
					}
				},
				{
					name: '10일',
					type: 'line',
					data: dataMA10,
					smooth: true,
					showSymbol: false,
					lineStyle: {
						width: 1
					}
				},
				{
					name: '20일',
					type: 'line',
					data: dataMA20,
					smooth: true,
					showSymbol: false,
					lineStyle: {
						width: 1
					}
				},
				{
					name: '60일',
					type: 'line',
					data: dataMA60,
					smooth: true,
					showSymbol: false,
					lineStyle: {
						width: 1
					}
				}
			]
		};

		option && myChart.setOption(option);

	}
}