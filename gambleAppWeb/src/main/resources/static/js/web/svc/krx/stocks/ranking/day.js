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
            param.maretCode = _serverParam.marketCode;
            param.targetDate = _serverParam.targetDate;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.priceDescList = json.object.stockList;
                } else {
                    alert(json.responseMessage);
                }
            };

            _stock.getPriceDescList( param, callback );
        },
        getPriceAscList: function() {
            let param = {};
            param.maretCode = _serverParam.marketCode;
            param.targetDate = _serverParam.targetDate;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.priceAscList = json.object.stockList;
                } else {
                    alert(json.responseMessage);
                }
            };

            _stock.getPriceAscList( param, callback );
        },
        getVolumeDescList: function() {
            let param = {};
            param.maretCode = _serverParam.marketCode;
            param.targetDate = _serverParam.targetDate;

            let callback = function( json ) {
                if (json.responseCode == RestResponseStatus.OK) {
                    v.priceAscList = json.object.stockList;
                } else {
                    alert(json.responseMessage);
                }
            };

            _stock.getVolumeDescList( param, callback );
        }

    }
}