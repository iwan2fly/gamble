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
        statIndex: {}, // 필요없음
        stock: {},
        company: {},
        companyFinanceRecentYearList: {},
        companyFinanceRecentQuarterList: {},
        recentYear: '',
        recentQuarter: '',

        rateChangeColor : '',
        rateChangeSymbol : '',

        indexDaily: {},

        stockDaily: {},
        stockDailyList: {},
        statStockWeeklyList: {},
        statStockWeek: {},
        statStockMonthlyList: {},
        statStockMonth: {},
        statStockYearlyList: {},
        statStockYear: {},
    },
    watch : {

    },
    computed: {

    },
    methods: {
        init : function () {
            this.getStock();
        },
        afterGetStock: function() {
            this.getIndexDaily();
            this.getCompany();
            this.getCompanyFinanceRecentYear();
            this.getCompanyFinanceRecentQuarter();
            this.getStockDailyList();
            this.getStatStockWeeklyList();
            this.getStatStockMonthlyList();
            this.getStatStockYearlyList();
        },
        getStock: function() {
            let param = {};
            param.stockCode = _serverParam.stockCode;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.stock = json.object.result;
                    v.afterGetStock();
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getStock( param, callback );
        },
        getCompany: function() {
            let param = {};
            param.companyCode = v.stock.companyCode;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.company = json.object.result;
                } else {
                    alert(json.responseMessage);
                }
            }

            _company.getCompany( param, callback );
        },
        getCompanyFinanceRecentYear: function() {
            let param = {};
            param.companyCode = v.stock.companyCode;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.companyFinanceRecentYearList = json.object.list;
                    if ( v.companyFinanceRecentYearList  != null && v.companyFinanceRecentYearList.length > 0 ) v.recentYear = v.companyFinanceRecentYearList[0].year;
                } else {
                    alert(json.responseMessage);
                }
            }

            _company.getCompanyFinanceRecentYear( param, callback );
        },
        getCompanyFinanceRecentQuarter: function() {
            let param = {};
            param.companyCode = v.stock.companyCode;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.companyFinanceRecentQuarterList = json.object.list;
                    if ( v.companyFinanceRecentQuarterList  != null && v.companyFinanceRecentQuarterList.length > 0 ) v.recentQuarter= v.companyFinanceRecentQuarterList[0].year + '.' + v.companyFinanceRecentQuarterList[0].quarter;
                } else {
                    alert(json.responseMessage);
                }
            }

            _company.getCompanyFinanceRecentQuarter( param, callback );
        },
        getStockDailyList: function() {
            let param = {};
            param.stockCode = v.stock.stockCode;
            param.sortIndex = 'tradeDate';
            param.sortType = 'desc';
            param.rows = '100';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.stockDailyList = json.object.list.reverse();
                    v.stockDaily = v.stockDailyList[ v.stockDailyList.length - 1];

                    if ( v.stockDaily.rateChange > 0 ) {
                        v.rateChangeColor = 'text-danger';
                        v.rateChangeSymbol = '+';
                    }
                    else if ( v.stockDaily.rateChange < 0 ) v.rateChangeColor = 'text-primary';

                    _ChartUtil.drawStatIndexChart( v.stockDailyList, 'day','stockChart');

                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getStockDailyList( param, callback );
        },
        getStatStockWeeklyList: function() {
            let param = {};
            param.stockCode = v.stock.stockCode;
            param.sortIndex = 'tradeDate';
            param.sortType = 'desc';
            param.rows = '53';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.statStockWeeklyList = json.object.list;
                    v.statStockWeek = v.statStockWeeklyList[ v.statStockWeeklyList.length - 1];
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getStatStockWeeklyList( param, callback );
        },
        getStatStockMonthlyList: function() {

            let param = {};
            let year = v.stock.lastTradeDate.substring(0,4) - 2;
            param.stockCode = v.stock.stockCode;
            param.startYearMonth = year + v.stock.lastTradeDate.substring(4,6);
            param.endYearMonth = v.stock.lastTradeDate.substring(0,6);
            param.sortIndex = 'yearMonth';
            param.sortType = 'desc';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.statStockMonthlyList = json.object.list;
                    v.statStockMonth = v.statStockMonthlyList[ v.statStockMonthlyList.length - 1];
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getStatStockMonthlyList( param, callback );
        },
        getStatStockYearlyList: function() {

            let param = {};

            param.stockCode = v.stock.stockCode;
            param.endYear = v.stock.lastTradeDate.substring(0,4);
            param.sortIndex = 'year';
            param.sortType = 'desc';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.statStockYearlyList = json.object.list;
                    v.statStockYear = v.statStockYearlyList[ v.statStockYearlyList.length - 1];
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getStatStockYearlyList( param, callback );
        },
        // 일별지수
        getIndexDaily: function() {

            let param = {};
            param.marketCode = v.stock.marketCode;
            param.tradeDate = v.stock.lastTradeDate;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.indexDaily = json.object.result;
                } else {
                    alert(json.responseMessage);
                }
            }

           _index.getStatIndexDaily( param, callback );
        },

    }
}
