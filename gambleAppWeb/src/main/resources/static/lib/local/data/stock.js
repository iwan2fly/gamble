let _stock = {
    getStockList : function( param, callback ) {
        _get( '/rest/domain/krx/statStock/' + targetUrl, param, callback );
    },



    getRateOfChangePriceList : function( param, callback ) {
        _get('/rest/domain/krx/statStock/rateOfChangePriceList', param, callback);
    },
    getVolumeTradeList : function( param, callback ) {
        _get('/rest/domain/krx/statStock/volumeTradeList', param, callback);
    },

}