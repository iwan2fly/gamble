let _getStock = {
    getStockList : function( param, callback ) {
        _get( '/rest/domain/krx/statStock/' + targetUrl, param, callback );
    },

    // 주식정보
    getStock: function( param, callback ) {
        let url = '/rest/domain/krx/stock/get';
        _get( url, param, callback );
    },

    // 주식 특정일 데이터
    // stockCode, tradeDate
    getStockDaily: function( param, callback ) {
        _get( '/rest/domain/krx/stockDaily/get', param, callback );
    },

    // 주식 일별데이터
    // stockCode
    getStockDailyList : function( param, callback ) {
        _get( '/rest/domain/krx/stockDaily/getList', param, callback );
    },

    // 가격 상승 순위 조회
    getPriceDescList : function( param, callback ) {
        // 주간/월간/년간은 통계에서, 일간은 일별에서 조회
        let url = '/rest/domain/krx/statStock/rateOfChangePriceList';
        if ( param.periodCode == 'day' ) {
            url = '/rest/domain/krx/stockDaily/getList';
        }

        param.sortIndex = "rateChange";
        param.sortType = "desc";
        _get( url, param, callback );
    },

    // 가격 하락 순위 조회
    getPriceAscList : function( param, callback ) {
        // 주간/월간/년간은 통계에서, 일간은 일별에서 조회
        let url = '/rest/domain/krx/statStock/rateOfChangePriceList';
        if ( param.periodCode == 'day' ) {
            url = '/rest/domain/krx/stockDaily/getList';
        }

        param.sortIndex = "rateChange";
        param.sortType = "asc";
        _get( url, param, callback );
    },

    // 거래대금 순위 조회
    getPriceTotalDescList : function( param, callback ) {
        // 주간/월간/년간은 통계에서, 일간은 일별에서 조회
        let url = '/rest/domain/krx/statStock/priceTotalList';
        if ( param.periodCode == 'day' ) {
            url = '/rest/domain/krx/stockDaily/getList';
        }

        param.sortIndex = "priceTotal";
        param.sortType = "desc";
        _get( url, param, callback );
    },

    // 거래량 순위 조회
    getVolumeDescList : function( param, callback ) {
        // 주간/월간/년간은 통계에서, 일간은 일별에서 조회
        let url = '/rest/domain/krx/statStock/volumeTradeList';
        if ( param.periodCode == 'day' ) {
            url = '/rest/domain/krx/stockDaily/getList';
        }

        param.sortIndex = "volumeTrade";
        param.sortType = "desc";
        _get( url, param, callback );
    },
    // 연간 주식 통계 목록
    // param = stockCode, endYear
    getStatStockYearlyList: function( param, callback ) {
        _get( '/rest/domain/krx/statStock/yearlyList', param, callback );
    },

    // 월간 주식 통계 목록
    // param = stockCode, startYearMonth, endYearMonth
    getStatStockMonthlyList: function( param, callback ) {
        _get( '/rest/domain/krx/statStock/monthlyList', param, callback );
    },
    // 주간 주식 통계 목록
    getStatStockWeeklyList: function( param, callback ) {
        _get( '/rest/domain/krx/statStock/weeklyList', param, callback );
    },
    // 올해기준 월간 주식 통계 목록
    getStatStockMonthlyListOfYEar: function( param, callback ) {
        _get( '/rest/domain/krx/statStock/monthlyListOfYear', param, callback );
    },

    // 올해기준 주간 주식 통계 목록
    getStatStockWeeklyListOfYear: function( param, callback ) {
        _get( '/rest/domain/krx/statStock/weeklyListOfYear', param, callback );
    },

    // 특정 년도 주식 변동 스프레드 리스트
    getChangeSpreadListYear: function( param, callback ) {
        param.periodCode = 'year';
        param.rangeSize = 3;
        _get( '/rest/domain/krx/statStock/changeSpreadList', param, callback );
    },
    // 특정 월 주식 변동 스프레드 리스트
    getChangeSpreadListMonth: function( param, callback ) {
        param.periodCode = 'month';
        param.rangeSize = 3;
        _get( '/rest/domain/krx/statStock/changeSpreadList', param, callback );
    },
    // 특정 주간 주식 변동 스프레드 리스트
    getChangeSpreadListWeek: function( param, callback ) {
        param.periodCode = 'week';
        param.rangeSize = 3;
        _get( '/rest/domain/krx/statStock/changeSpreadList', param, callback );
    },




    // 일간순위
    getDailyRankList: function( param, callback ) {
        _get( '/rest/domain/krx/dailyRankStock/getList', param, callback );
    },
    // 주간/월간/년간 순위
    getPeriodRankListYear: function( param, callback ) {
        param.periodCode = 'year';
        _get( '/rest/domain/krx/periodRankStock/getList', param, callback );
    },
    getPeriodRankListMonth: function( param, callback ) {
        param.periodCode = 'month';
        _get( '/rest/domain/krx/periodRankStock/getList', param, callback );
    },
    getPeriodRankListWeek: function( param, callback ) {
        param.periodCode = 'week';
        _get( '/rest/domain/krx/periodRankStock/getList', param, callback );
    },


}