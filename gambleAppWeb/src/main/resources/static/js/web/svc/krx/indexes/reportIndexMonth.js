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
        periodCode : 'month',
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
            this.getStatIndex();
            this.getStatIndexDailyList();
            this.getStatIndexMonthlyList();

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
            param.month = _serverParam.month;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.statIndex = json.object.result;
                } else {
                    alert(json.responseMessage);
                }
            }

           _index.getStatIndexMonth( param, callback );
        },
        // 특정월 일간 지수 목록
        getStatIndexDailyList: function() {

            let param = {};
            param.marketCode = _serverParam.marketCode;

            let month = _serverParam.month < 10 ? '0' + _serverParam.month : '' + _serverParam.month;
            param.startDate = _serverParam.year + month + "01";
            param.endDate = _serverParam.year + month + "31";

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexDailyList = json.object.list;
                    _ChartUtil.drawStatIndexChart( v.statIndexDailyList, 'day', 'statIndexChart' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexDailyList( param, callback );
        },
        // 최근 13개월 월간 지수 리스트
        getStatIndexMonthlyList: function() {

            let param = {};
            param.marketCode = _serverParam.marketCode;

            let month = _serverParam.month < 10 ? '0' + _serverParam.month : '' + _serverParam.month;
            param.startYearMonth = ( _serverParam.year - 1 ) + month;
            param.endYearMonth = _serverParam.year + month;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexMonthlyList =  json.object.list;
                    _ChartUtil.drawStatIndexChart( v.statIndexMonthlyList, 'month', 'statIndexChartMonthly' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexMonthlyList( param, callback );
        },

        // 상승률 상위 주식 리스트
        getPriceDescList : function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.periodCode = this.periodCode;
            param.year = _serverParam.year;
            param.month = _serverParam.month;
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
            param.month = _serverParam.month;
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
            param.month = _serverParam.month;
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
            param.month = _serverParam.month;
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