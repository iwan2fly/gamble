let _stock = {
    getStockList : function( param, callback ) {
        _get( '/rest/domain/krx/statStock/' + targetUrl, param, callback );
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

    // 거래량 순위 조회
    getVolumeDescList : function( param, callback ) {
        _get('/rest/domain/krx/statStock/volumeTradeList', param, callback);
    },

}d