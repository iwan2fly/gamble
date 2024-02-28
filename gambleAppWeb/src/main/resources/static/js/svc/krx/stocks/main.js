let v;
document.addEventListener("DOMContentLoaded", function() { v = new Vue( obj ); });

let obj = {
    el: '#main',
    mounted: function() {
        this.init();
    },
    created : function() {

    },
    data : {
        stock: {},
        stockDaily: {},
        company: {},
    },
    watch : {
        /*

        siteGroupIntroduceList: {
            handler: function (val, oldVal) {
                this.makeFullInfoBuildText();
            },
            deep: true,
        },

         */
    },
    computed: {
        /*

        siteGroupDescValid: function () {
            return validationField( this.fullInfoBuildText, true, 20, 1000);
        },
        */
    },
    methods: {
        init : async function () {
            await this.getStock(serverParam.stockCode);
            await this.getDailyStock( serverParam.stockCode, this.stock.lastTradeDate );
            await this.getCompany( this.stock.companyCode );
        },
        getStock: async function( stockCode ) {
            let param = {};
            param.stockCode = stockCode;

            const json = await _aget( '/rest/domain/krx/stock/get', param );
            if ( json.responseCode == RestResponseStatus.OK ) {
                v.stock = json.object.stock;
            } else {
                alert( json.responseMessage );
            }
        },
        getDailyStock: async function( stockCode, tradeDate ) {
            let param = {};
            param.stockCode = stockCode;
            param.tradeDate = tradeDate;

            const json = await _aget( '/rest/domain/krx/stockDaily/get', param);
            if ( json.responseCode == RestResponseStatus.OK ) {
                v.stockDaily = json.object.stockDaily;
            } else {
                alert( json.responseMessage );
            }
        },
        getCompany: async function( companyCode ) {
            let param = {};
            param.companyCode = companyCode;

            const json = await _aget( '/rest/domain/krx/company/get', param);
            if ( json.responseCode == RestResponseStatus.OK ) {
                v.company = json.object.company;
            } else {
                alert( json.responseMessage );
            }
        }
    }
}