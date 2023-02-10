let _company = {
    getCompany : function( param, callback ) {
        _get( '/rest/domain/krx/company/get', param, callback );
    },

    getCompanyFinance: function( param, callback ) {

    },

    getCompanyFinanceRecentYear: function( param, callback ) {
        _get( '/rest/domain/krx/companyFinance/getRecentYear', param, callback );
    },

    getCompanyFinanceRecentQuarter: function( param, callback ) {
        _get( '/rest/domain/krx/companyFinance/getRecentQuarter', param, callback );
    }

}