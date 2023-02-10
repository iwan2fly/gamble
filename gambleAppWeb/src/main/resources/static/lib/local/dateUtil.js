
let _DateUtil = {
	// yyyyxx : yyyy = year, xx : 주차
	getFirstDateOfWeek: function( yyyyxx ) {
		let year = yyyyxx.substring(0,4);
		let week = yyyyxx.substring(4,6);
		return dayjs(year).week( week );

	},
	getLastDateOfWeek: function( yyyyxx ) {
		let firstDate = this.getFirstDateOfWeek( yyyyxx );
		return firstDate.add(6, "day");
	},
	dateToYYYYMMDD : function( date ) {
		return date.format("YYYYMMDD");
	},
	strToDate: function( yyyymmdd ) {
		let value	= toNumberStr(yyyymmdd);
		if ( value.length != 8 ) return value;

		let year	= value.substring(0,4);
		let month	= value.substring(4,6) - 1;
		let day		= value.substring(6,8);

		let date = new Date( year, month, day);
		return date;
	},
	// 날짜
	yyyymmdd: function( value, delimeter ) {
		if ( delimeter == undefined || delimeter == null ) delimeter = '';
		if ( value != null && value != "" ) {
			if ( value.length == 8 || value.length == 13 ) {
				value = value.substring(0,4) + delimeter + value.substring(4,6) + delimeter + value.substring(6,8);
			}
		}
		return value;
	},

	mmdd: function ( value, delimeter ) {
		if ( delimeter == undefined || delimeter == null ) delimeter = ".";
		if ( value != null && value != "" ) {
			if ( value.length == 8 || value.length == 13 ) {
				value = value.substring(4,6) + delimeter + value.substring(6,8);
			}
		}
		return value;
	}

}