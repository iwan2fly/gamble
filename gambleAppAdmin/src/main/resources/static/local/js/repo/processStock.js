let _processStock = {

    updateCurrentPriceFromPotal : function( param, callback ) {
        _get( '/rest/domain/krx/statStock/' + targetUrl, param, callback );
    },



}