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
        periodCode : 'year',
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
            this.getStatIndexYearlyList();
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
            param.year = _serverParam.year;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.statIndex = json.object.result;
                } else {
                    alert(json.responseMessage);
                }
            }

           _index.getStatIndexYear( param, callback );
        },
        // 특정년도 일간 지수 목록
        getStatIndexDailyList: function() {

            let param = {};
            param.marketCode = _serverParam.marketCode;

            let year = _serverParam.year < 10 ? '0' + _serverParam.year : '' + _serverParam.year;
            param.startDate = _serverParam.year + "0101";
            param.endDate = _serverParam.year + "1231";

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
        // 년간 지수 리스트
        getStatIndexYearlyList: function() {

            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.year = _serverParam.year;

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.statIndexYearlyList =  json.object.list;
                    _ChartUtil.drawStatIndexChart( v.statIndexYearlyList, 'year', 'statIndexChartMonthly' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _index.getStatIndexYearlyList( param, callback );
        },

        // 상승률 상위 주식 리스트
        getPriceDescList : function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.periodCode = this.periodCode;
            param.year = _serverParam.year;
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
            param.yearOrder = _serverParam.year + '00';

            let callback = function( json ) {
                if ( json.responseCode == RestResponseStatus.OK ) {
                    v.changeSpreadList = json.object.list;
                    _ChartUtil.drawBarChart( v.changeSpreadList, 'changeSpreadChart' );
                } else {
                    alert( json.responseMessage );
                }
            }

            _stock.getChangeSpreadListYear( param, callback );
        }
    }
}