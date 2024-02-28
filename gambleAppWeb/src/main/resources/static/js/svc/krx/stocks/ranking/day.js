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
        priceDescList: {},
        priceAscList: {},
        volumeDescList: {},
    },
    watch : {

    },
    computed: {

    },
    methods: {
        init : function () {
            this.getPriceDescList();
            this.getPriceAscList();
            this.getVolumeDescList();
        },
        getPriceDescList: function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.tradeDate = _serverParam.tradeDate;
            param.periodCode = 'day';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.priceDescList = json.object.list;
                    console.log( v.priceDescList );
                } else {
                    alert(json.responseMessage);
                }
            };

            _stock.getPriceDescList( param, callback );
        },
        getPriceAscList: function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.tradeDate = _serverParam.tradeDate;
            param.periodCode = 'day';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.priceAscList = json.object.list;
                } else {
                    alert(json.responseMessage);
                }
            };

            _stock.getPriceAscList( param, callback );
        },
        getVolumeDescList: function() {
            let param = {};
            param.marketCode = _serverParam.marketCode;
            param.tradeDate = _serverParam.tradeDate;
            param.periodCode = 'day';

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.volumeDescList = json.object.list;
                } else {
                    alert(json.responseMessage);
                }
            };

            _stock.getVolumeDescList( param, callback );
        }

    }
}