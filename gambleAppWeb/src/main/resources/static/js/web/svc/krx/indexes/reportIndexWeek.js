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
        periodCode : 'week',
        statIndex: {},
        statIndexYearlyList: {},
        statIndexMonthlyList: {},
        statIndexWeeklyList: {},
        statIndexDailyList: {},
        priceDescList: {},
        priceAscList: {},
        priceTotalDescList: {},
        volumeDescList: {},
    },
    watch : {

    },
    computed: {

    },
    methods: {
        init : function () {

            _serverParam.year = _serverParam.yearWeek.substring(0,4);
            _serverParam.week = _serverParam.yearWeek.substring(4,6);

            this.getStatIndex();
            this.getStatIndexDailyList();
            this.getStatIndexWeeklyList();

            this.getPriceDescList();
            this.getPriceAscList();
            this.getVolumeDescList();
            this.getPriceTotalDescList();
        },
        // 지수 통계 : 년간 / 월간 / 주간
        getStatIndex: function() {

            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.year = _serverParam.year;
            param.yearWeek = _serverParam.yearWeek;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.statIndex = json.object.result;
                } else {
                    alert(json.responseMessage);
                }
            }

           _index.getStatIndexWeek( param, callback );
        },
        // 특정주간 일별 지수 목록
        getStatIndexDailyList: function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.startDate = dateUtil.dateToYYYYMMDD( dateUtil.getFirstDateOfWeek( _serverParam.yearWeek ) );
            param.endDate = dateUtil.dateToYYYYMMDD( dateUtil.getLastDateOfWeek( _serverParam.yearWeek ) );

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexDailyList = json.object.list;
                    _chartchart.drawStatIndexChart( 'day', 'statIndexChart' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexDailyList( param, callback );
        },
        // 최근 주간 지수 리스트
        getStatIndexWeeklyList: function() {

            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.endYearWeek = _serverParam.yearWeek;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexWeeklyList =  json.object.list;
                    _ChartUtil.drawStatIndexChart( 'week', 'statIndexChartWeekly' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexWeeklyList( param, callback );
        },

        // 상승률 상위 주식 리스트
        getPriceDescList : function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.periodCode = this.periodCode;
            param.year = _serverParam.year;
            param.yearWeek = _serverParam.yearWeek;
            param.sortType = 'desc';
            param.rows = 20;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.priceDescList = json.object.list;
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getPriceDescList( param, callback );
        },
        // 상승률 상위 주식 리스트
        getPriceAscList : function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.periodCode = this.periodCode;
            param.year = _serverParam.year;
            param.yearWeek = _serverParam.yearWeek;
            param.sortType = 'asc';
            param.rows = 20;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.priceAscList = json.object.list;
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getPriceAscList( param, callback );
        },
        // 거래량 상위 주식 리스트
        getVolumeDescList : function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.periodCode = this.periodCode;
            param.year = _serverParam.year;
            param.yearWeek = _serverParam.yearWeek;
            param.sortType = 'desc';
            param.rows = 20;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.volumeDescList = json.object.list;
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getVolumeDescList( param, callback );
        },
        // 거래대금 상위 주식 리스트
        getPriceTotalDescList : function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.periodCode = this.periodCode;
            param.year = _serverParam.year;
            param.yearWeek = _serverParam.yearWeek;
            param.sortType = 'desc';
            param.rows = 20;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.priceTotalDescList = json.object.list;
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getPriceTotalDescList( param, callback );
        },
    }
}