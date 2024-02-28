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
        chartDom: 'stockChart',
        statIndex: {}, // 필요없음
        stock: {},
        company: {},

        companyFinanceRecentYearList: {},
        financeYear : {
            salesAmount : '',
            bizProfit: '',
            netProfit: '',
        },
        companyFinanceRecentQuarterList: {},
        financeQuarter : {
            salesAmount : '',
            bizProfit: '',
            netProfit: '',
        },
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

        dailyRank: {
            rateChange : '',
            volumeTrade : '',
            priceTrade : '',
            priceTotal : '',
        },
        weeklyRank: {
            rateChange : '',
            volumeTrade : '',
            priceTrade : '',
            priceTotal : '',
        },
        monthlyRank: {
            rateChange : '',
            volumeTrade : '',
            priceTrade : '',
            priceTotal : '',
        },
        yearlyRank: {
            rateChange : '',
            volumeTrade : '',
            priceTrade : '',
            priceTotal : '',
        },
    },
    watch : {

    },
    computed: {

    },
    methods: {
        init : function () {
            this.getStock();
            this.getStockDailyList();
        },
        afterGetStockDaily: function() {
            this.getIndexDaily();
            this.getCompany();
            this.getCompanyFinanceRecentYear();
            this.getCompanyFinanceRecentQuarter();
            this.getStatStockWeeklyList();
            this.getStatStockMonthlyList();
            this.getStatStockYearlyList();
            this.getDailyRank();
            this.getWeeklyRank();
            this.getMonthlyRank();
            this.getYearlyRank();
        },
        getStock: function() {
            let param = {};
            param.stockCode = _serverParam.stockCode;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.stock = json.object.result;
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getStock( param, callback );
        },
        getStockDailyList: function() {
            let param = {};
            param.stockCode = _serverParam.stockCode;
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

                    v.afterGetStockDaily();

                    _ChartUtil.drawStatIndexChart( v.stockDailyList, 'day', v.chartDom );

                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getStockDailyList( param, callback );
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
                    v.financeYear.salesAmount = v.companyFinanceRecentYearList[3].amount;
                    v.financeYear.bizProfit = v.companyFinanceRecentYearList[2].amount;
                    v.financeYear.netProfit = v.companyFinanceRecentYearList[0].amount;
                    if ( v.companyFinanceRecentYearList  != null && v.companyFinanceRecentYearList.length > 0 ) v.recentYear = v.companyFinanceRecentYearList[0].year + '년';
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
                    v.financeQuarter.salesAmount = v.companyFinanceRecentQuarterList[3].amount;
                    v.financeQuarter.bizProfit = v.companyFinanceRecentQuarterList[2].amount;
                    v.financeQuarter.netProfit = v.companyFinanceRecentQuarterList[0].amount;
                    if ( v.companyFinanceRecentQuarterList  != null && v.companyFinanceRecentQuarterList.length > 0 ) v.recentQuarter= v.companyFinanceRecentQuarterList[0].year + '년 ' + v.companyFinanceRecentQuarterList[0].quarter + '분기';
                } else {
                    alert(json.responseMessage);
                }
            }

            _company.getCompanyFinanceRecentQuarter( param, callback );
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
            let year = v.stock.lastTradeDate.substring(0,4);
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
        // 일간 순위
        getDailyRank: function() {
            let param = {};
            param.stockCode = v.stock.stockCode;
            param.tradeDate = v.stock.lastTradeDate;
            param.sortIndex = 'rankCode';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.dailyRank.rateChange = _StrUtil.toCurrency(json.object.list[3].ranking);
                    v.dailyRank.volumeTrade = _StrUtil.toCurrency(json.object.list[6].ranking);
                    v.dailyRank.priceTrade = _StrUtil.toCurrency(json.object.list[2].ranking);
                    v.dailyRank.priceTotal = _StrUtil.toCurrency(json.object.list[1].ranking);
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getDailyRankList( param, callback );
        },
        getWeeklyRank: function() {
            let param = {};
            param.stockCode = v.stock.stockCode;
            param.tradeDate = v.stock.lastTradeDate;
            param.sortIndex = 'rankCode';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.weeklyRank.rateChange = _StrUtil.toCurrency(json.object.list[4].ranking);
                    v.weeklyRank.volumeTrade = _StrUtil.toCurrency(json.object.list[6].ranking);
                    v.weeklyRank.priceTrade = _StrUtil.toCurrency(json.object.list[3].ranking);
                    v.weeklyRank.priceTotal = _StrUtil.toCurrency(json.object.list[2].ranking);
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getPeriodRankListWeek( param, callback );
        },
        getMonthlyRank: function() {
            let param = {};
            param.stockCode = v.stock.stockCode;
            param.tradeDate = v.stock.lastTradeDate;
            param.sortIndex = 'rankCode';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.monthlyRank.rateChange = _StrUtil.toCurrency(json.object.list[4].ranking);
                    v.monthlyRank.volumeTrade = _StrUtil.toCurrency(json.object.list[6].ranking);
                    v.monthlyRank.priceTrade = _StrUtil.toCurrency(json.object.list[3].ranking);
                    v.monthlyRank.priceTotal = _StrUtil.toCurrency(json.object.list[2].ranking);
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getPeriodRankListMonth( param, callback );
        },
        getYearlyRank: function() {
            let param = {};
            param.stockCode = v.stock.stockCode;
            param.tradeDate = v.stock.lastTradeDate;
            param.sortIndex = 'rankCode';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.yearlyRank.rateChange = _StrUtil.toCurrency(json.object.list[4].ranking);
                    v.yearlyRank.volumeTrade = _StrUtil.toCurrency(json.object.list[6].ranking);
                    v.yearlyRank.priceTrade = _StrUtil.toCurrency(json.object.list[3].ranking);
                    v.yearlyRank.priceTotal = _StrUtil.toCurrency(json.object.list[2].ranking);
                } else {
                    alert(json.responseMessage);
                }
            }

            _stock.getPeriodRankListYear( param, callback );
        },
        drawGraph: function( periodCode) {
            let list = v.stockDailyList;
            if ( periodCode == 'month' ) list = v.statStockMonthlyList;
            else if ( periodCode == 'week' ) list = v.statStockWeeklyList;
            else if ( periodCode == 'year' ) list = v.statStockYearlyList;

            _ChartUtil.drawStatIndexChart( list, periodCode, this.chartDom );
        },

    }
}
