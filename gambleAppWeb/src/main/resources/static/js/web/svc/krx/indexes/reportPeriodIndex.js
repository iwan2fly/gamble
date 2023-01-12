let v;
document.addEventListener("DOMContentLoaded", function() { v = new Vue( obj ); });

let obj = {
    el: '#body',
    mounted: function() {
        this.init();
    },
    created : function() {

    },
    data : {
        serverParam : {},
        statIndex: {},
        statIndexYearlyList: {},
        statIndexMonthlyList: {},
        statIndexWeeklyList: {},
        statIndexDailyList: {},
        rateOfRiseList: {},
        rateOfFallList: {},
        volumeTradeDescList: {},
        volumeTradeAscList: {},
    },
    watch : {

    },
    computed: {

    },
    methods: {
        init : function () {
            this.serverParam = serverParam;
            this.getStatIndex();
            this.getStatIndexYearlyList();
            this.getStatIndexMonthlyList();
            this.getStatIndexWeeklyList();
            this.getStatIndexDailyList();
            this.getRateOfRiseList();
            this.getRateOfFallList();
            this.getVolumeTradeDescList();
            this.getVolumeTradeAscList();
        },
        // 지수 통계 : 년간 / 월간 / 주간
        getStatIndex: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.periodCode = serverParam.periodCode;
            param.year = serverParam.year;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.statIndex = json.object.statIndex;
                } else {
                    alert(json.responseMessage);
                }
            }

           _index.getStatIndex( param, callback );
        },
        // 연간 지수 통계 목록
        getStatIndexYearlyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.endYear = serverParam.year;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexYearlyList = json.object.statIndexList;
                    v.drawStatIndexChart( 'year', 'statIndexChartYearly' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexYearlyList( param, callback );
        },
        // 특정 년도 월간 지수 통계 목록
        getStatIndexMonthlyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.year = serverParam.year;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexMonthlyList =  json.object.statIndexList;
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexMonthlyList( param, callback );
        },
        // 특정 년도 주간 지수 통계 목록
        getStatIndexWeeklyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.year = serverParam.year;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexWeeklyList = json.object.statIndexList;
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexWeeklyList( param, callback );
        },
        // 특정 년도 일별 지수 통계 목록
        getStatIndexDailyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.startDate = serverParam.year + "0101";
            param.endDate = serverParam.year + "1231";

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexDailyList = json.object.indexDailyList;
                    v.drawStatIndexChart( 'day', 'statIndexChart' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexDailyList( param, callback );
        },
        // 상승률 상위 주식 리스트
        getRateOfRiseList : function() {
            let param = {};
            param.marketCode = serverParam.marketCode;
            param.startDate = serverParam.year + "0101";
            param.endDate = serverParam.year + "1231";
            param.periodCode = 'year';
            param.sortIndex = 'rateChange';
            param.sortType = 'desc';

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.rateOfRiseList = json.object.statStockList;
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getRateOfChangePriceList( param, callback );
        },
        // 상승률 상위 주식 리스트
        getRateOfFallList : function() {
            let param = {};
            param.marketCode = serverParam.marketCode;
            param.startDate = serverParam.year + "0101";
            param.endDate = serverParam.year + "1231";
            param.periodCode = 'year';
            param.sortIndex = 'rateChange';
            param.sortType = 'asc';

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.rateOfFallList = json.object.statStockList;
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getRateOfChangePriceList( param, callback );
        },
        // 거래량 상위 주식 리스트
        getVolumeTradeDescList : function() {
            let param = {};
            param.marketCode = serverParam.marketCode;
            param.startDate = serverParam.year + "0101";
            param.endDate = serverParam.year + "1231";
            param.periodCode = 'year';
            param.sortIndex = 'volumeTrade';
            param.sortType = 'desc';

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.volumeTradeDescList = json.object.statStockList;
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getVolumeTradeList( param, callback );
        },
        // 거래량 하위 주식 리스트
        getVolumeTradeAscList : function() {
            let param = {};
            param.marketCode = serverParam.marketCode;
            param.startDate = serverParam.year + "0101";
            param.endDate = serverParam.year + "1231";
            param.periodCode = 'year';
            param.sortIndex = 'volumeTrade';
            param.sortType = 'asc';

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.volumeTradeAscList = json.object.statStockList;
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getVolumeTradeList( param, callback );
        },
        // 주식 일봉/주봉/월봉/연봉 차트 그리기
        drawStatIndexChart : function( period, dom ) {

            // 목록 선택
            let list = null;
            if ( period == 'month' ) list = v.statIndexMonthlyList;
            else if ( period == 'week' ) list = v.statIndexWeeklyList;
            else if ( period == 'day' ) list = v.statIndexDailyList;
            else list = v.statIndexYearlyList;

            // X축
            let xAxisData = [];
            for ( i = 0; i < list.length; i++ ) {
                if ( period == 'month' ) xAxisData[i] = list[i].month;
                else if ( period == 'week' ) xAxisData[i] = list[i].month;
                else if ( period == 'day' ) xAxisData[i] = yyyymmdd(list[i].tradeDate);
                else xAxisData[i] = list[i].year;
            }

            // 데이터
            let seriesData = [];
            let min = list[0].priceLow;
            let max = list[0].priceHigh;
            for ( i = 0; i < list.length; i++ ) {
                let dayData = [ list[i].priceStart, list[i].priceFinal, list[i].priceLow, list[i].priceHigh ];
                min = min > list[i].priceLow ? list[i].priceLow : min;
                max = max < list[i].priceHigh ? list[i].priceHigh : max;
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
}