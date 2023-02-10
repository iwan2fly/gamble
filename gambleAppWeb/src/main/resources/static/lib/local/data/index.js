let _index = {

    // 연간 지수 통계
    getStatIndexYear: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/year', param, callback );
    },
    // 월간 지수 통계
    getStatIndexMonth: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/month', param, callback );
    },
    // 주간 지수 통계
    getStatIndexWeek: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/week', param, callback );
    },


    // 연간 지수 통계 목록
    getStatIndexYearlyList: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/yearlyList', param, callback );
    },

    // 월간 지수 통계 목록
    getStatIndexMonthlyList: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/monthlyList', param, callback );
    },

    // 주간 지수 통계 목록
    getStatIndexWeeklyList: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/weeklyList', param, callback );
    },

    // 일간 지수 통계 목록
    getStatIndexDailyList: function( param, callback ) {
        _get( '/rest/domain/krx/indexDaily/getList', param, callback );
    },

    // 특정 날짜 / 특정 시장 정보
    // marketCode, tradeDate
    getStatIndexDaily: function( param, callback ) {
        _get( '/rest/domain/krx/indexDaily/get', param, callback );
    },


    // 특정 년도 이전 지수 통계 목록
    getStatIndexYearlyBeforeYear: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/yearlyListOfYear', param, callback );
    },

    // 특정 년도 월간 지수 통계 목록
    getStatIndexMonthlyListOfYear: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/monthlyListOfYear', param, callback );
    },

    // 특정 년도 주간 지수 통계 목록
    getStatIndexWeeklyListOfYear: function( param, callback ) {
        _get( '/rest/domain/krx/statIndex/weeklyListOfYear', param, callback );
    },

    // 특정 년도 일간 지수 통계 목록
    getStatIndexDailyListOfYear: function( param, callback ) {
        _get( '/rest/domain/krx/indexDaily/getListOfYear', param, callback );
    },

}