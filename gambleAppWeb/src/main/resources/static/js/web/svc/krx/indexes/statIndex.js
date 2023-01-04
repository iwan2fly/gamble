let v;
document.addEventListener("DOMContentLoaded", function() { v = new Vue( obj ); });

let obj = {
    el: '#body',
    mounted: function() {
        this.init();

        // Initialize the echarts instance based on the prepared dom
        var myChart = echarts.init(document.getElementById('chart'));

        // Specify the configuration items and data for the chart
        var option = {
            title: {
                text: 'ECharts Getting Started Example'
            },
            tooltip: {},
            legend: {
                data: ['sales']
            },
            xAxis: {
                data: ['Shirts', 'Cardigans', 'Chiffons', 'Pants', 'Heels', 'Socks']
            },
            yAxis: {},
            series: [
                {
                    name: 'sales',
                    type: 'bar',
                    data: [5, 20, 36, 10, 10, 20]
                }
            ]
        };

        // Display the chart using the configuration items and data just specified.
        myChart.setOption(option);
    },
    created : function() {

    },
    data : {
        serverParam : {},
        statIndex: {},
        statIndexMonthlyList: {},
    },
    watch : {

    },
    computed: {

    },
    methods: {
        init : function () {
            this.serverParam = serverParam;
            this.getStatIndex();
            this.getStatIndexMonthlyList();
           // this.drawStatIndexChart();
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
        // 특정 년도 월간 지수 통계 목록
        getStatIndexMonthlyList: function() {

            let param = {};
            param.marketCode = serverParam.marketCode;
            param.year = serverParam.year;

            _get( '/rest/domain/krx/statIndex/monthlyList', param, function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexMonthlyList = json.object.statIndexList;
                } else {
                    alert( json.responseMessage );
                }
            } );
        },
        drawStatIndexChart : function() {
            // Initialize the echarts instance based on the prepared dom
            var myChart = echarts.init(document.getElementById('main'));

            // Specify the configuration items and data for the chart
            var option = {
                title: {
                    text: 'ECharts Getting Started Example'
                },
                tooltip: {},
                legend: {
                    data: ['sales']
                },
                xAxis: {
                    data: ['Shirts', 'Cardigans', 'Chiffons', 'Pants', 'Heels', 'Socks']
                },
                yAxis: {},
                series: [
                    {
                        name: 'sales',
                        type: 'bar',
                        data: [5, 20, 36, 10, 10, 20]
                    }
                ]
            };

            // Display the chart using the configuration items and data just specified.
            myChart.setOption(option);
        }

    }
}