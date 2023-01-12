let _index = {
    getStatIndex: function( param, callback ) {
        let targetUrl = 'yearly';
        if ( param.periodCode == 'month' ) {
            param.month = serverParam.month;
            targetUrl = 'monthly';
        } else if ( param.periodCode == 'week' ) {
            param.yearWeek = serverParam.yearWeek
            targetUrl = 'weekly';
        }

        _get( '/rest/domain/krx/statIndex/' + targetUrl, param, callback );
    },

    // 연간 지수 통계 목록
    getStatIndexYearlyList: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/yearlyList', param, callback );
    },

    // 특정 년도 월간 지수 통계 목록
    getStatIndexMonthlyList: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/monthlyList', param, callback );
    },

    // 특정 년도 주간 지수 통계 목록
    getStatIndexWeeklyList: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/weeklyList', param, callback );
    },

    // 특정 년도 일별 지수 통계 목록
    getStatIndexDailyList: function( param, callback ) {
        _get( '/rest/domain/krx/indexDaily/getList', param, callback );
    },

}