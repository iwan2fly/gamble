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
        },
        // 지수 통계 : 년간 / 월간 / 주간
        getStatIndex: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.periodCode = serverParam.periodCode;
            param.year = serverParam.year;

            let targetUrl = 'yearly';
            if ( param.periodCode == 'month' ) {
                param.month = serverParam.month;
                targetUrl = 'monthly';
            } else if ( param.periodCode == 'week' ) {
                param.yearWeek = serverParam.yearWeek
                targetUrl = 'weekly';
            }

            _get( '/rest/domain/krx/statIndex/' + targetUrl, param, function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndex = json.object.statIndex;
                } else {
                    alert( json.responseMessage );
                }
            } );
        },
        // 연간 지수 통계 목록
        getStatIndexYearlyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.endYear = serverParam.year;

            _get( '/rest/domain/krx/statIndex/yearlyList', param, function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexYearlyList = json.object.statIndexList;
                    v.drawStatIndexChart( 'year', 'statIndexChartYearly' );
                } else {
                    alert( json.responseMessage );
                }
            } );
        },
        // 특정 년도 월간 지수 통계 목록
        getStatIndexMonthlyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.year = serverParam.year;

            _get( '/rest/domain/krx/statIndex/monthlyList', param, function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexMonthlyList = json.object.statIndexList;
                    v.drawStatIndexChart( 'month', 'statIndexChartMonthly' );
                } else {
                    alert( json.responseMessage );
                }
            } );
        },
        // 특정 년도 주간 지수 통계 목록
        getStatIndexWeeklyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.year = serverParam.year;

            _get( '/rest/domain/krx/statIndex/weeklyList', param, function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexWeeklyList = json.object.statIndexList;
                    v.drawStatIndexChart( 'week', 'statIndexChartWeekly' );
                } else {
                    alert( json.responseMessage );
                }
            } );
        },
        // 특정 년도 일별 지수 통계 목록
        getStatIndexDailyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.startDate = serverParam.year + "0101";
            param.endDate = serverParam.year + "1231";

            _get( '/rest/domain/krx/indexDaily/getList', param, function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexDailyList = json.object.indexDailyList;
                    v.drawStatIndexChart( 'day', 'statIndexChartDaily' );
                } else {
                    alert( json.responseMessage );
                }
            } );
        },
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