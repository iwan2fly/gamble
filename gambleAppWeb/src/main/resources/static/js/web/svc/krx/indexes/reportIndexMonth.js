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
        year : _serverParam.yearOrder.substring(0,4),
        month : _serverParam.yearOrder.substring(4,6),
        statIndex: {},
        statIndexYearlyList: {},
        statIndexMonthlyList: {},
        statIndexWeeklyList: {},
        statIndexDailyList: {},
        priceDescList: {},
        priceAscList: {},
        priceTotalDescList: {},
        volumeDescList: {},
        changeSpreadList: {},
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
            this.getChangeSpreadList();
        },
        // 지수 통계 : 년간 / 월간 / 주간
        getStatIndex: function() {

            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.yearOrder = _serverParam.yearOrder;

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
            param.startDate = _serverParam.yearOrder + "01";
            param.endDate = _serverParam.yearOrder + "31";

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
            param.startYearMonth = ( this.year - 1 ) + this.month;
            param.endYearMonth = _serverParam.yearMonth;

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
            param.yearOrder = _serverParam.yearOrder;
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
            param.yearOrder = _serverParam.yearOrder;
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
            param.yearOrder = _serverParam.yearOrder;
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
            param.yearOrder = _serverParam.yearOrder;
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
        // 변동 스프레드 리스트
        getChangeSpreadList: function() {
            let param = {}
            param.marketCode = _serverParam.marketCode;
            param.yearOrder = _serverParam.yearOrder;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.changeSpreadList = json.object.list;
                    _ChartUtil.drawBarChart( v.changeSpreadList, 'changeSpreadChart' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getChangeSpreadListMonth( param, callback );
        }
    }
}