package markettracker.util;

import android.annotation.SuppressLint;


@SuppressLint("SdCardPath")
abstract public interface Constants {

public static String URL="http://www.rymbus.com/fc/index.php/Api/";
	enum ControlType {
		DATE,TIME,SINGLECHOICELIST,NONE, TEXT, MULTICHOICE, SINGLECHOICE, SELECTED, DATATIME, STATUS, INTEGER, DECIMAL, EMAIL_EDIT, PHONE_EDIT, LINK, RADIOBUTTON,PICKPHOTO;
	}

	enum PhotoType {
		CHECKIN, CHECKOUT;
	}

	enum AlertType {
		INFO, ERR;
	}

	interface TemplateType {
		final static int OTHER = 0;
		final static int PLAN = 1;
		final static int REPORT = 2;
	}

	interface TableType {
		final static int LINE = 1;
		final static int FOOT = 2;
	}

	interface PanalType {
		final static String PANEL = "panel";
		final static String PRODUCTTABLE = "table";
	}

	interface TableInfo {
		final static String TABLE_KEY = "key_id";
	}

	interface LoginConfig {
		static final String USER_NAME = "userName";
		static final String USER_PWD = "pwd";
		static final String IMEI = "imei";
		static final String APP_VERSION = "appVersion";
		static final String DEVICE_TYPE = "deviceType";
	}

	interface QueryConfig {
		static final String USER_ID = "userId";
		static final String TYPE = "type";
		static final String ISALL = "isAll";
		static final String LASTTIME = "lastTime";
		static final String STARTROW = "startRow";
	}

	interface SyncType {
		static final String LOGIN = "login";
		static final String QUERY = "query";
		static final String UPLOAD = "upload";
		static final String UPLOADMSG = "uploadmsg";
		static final String GETSERVERTIME = "getservertime";
	}

	// interface FieldIndex {
	// final static int ID = 0;
	// final static int NAME = 1;
	// final static int CODE = 2;
	// final static int SHORTNAME = 3;
	//
	// final static int ADDRESS = 4;
	//
	// final static int TERMINAL = 5;
	// }

	interface CustomGridType {
		final static int OTHER = 0;
		final static int MAINMENU = 1;
		final static int ADDHOC = 2;

		final static int STOCK = 4;

		final static int MESSAGE = 3;

		final static int RESETREPORT = 5;

		final static int CUSTOMGRID = 6;

		final static int ACTIVTY = 7;
		final static int DASHBOARD = 8;

		final static int SIGN = 9;
		final static int CHENLIETTU = 10;
		
		final static int SELECTRPT = 11;	
		final static int PICKPHOTO = 12;
		
		final static int SELECTCX=15;
		
		final static int SELECTDATA=16;
		
		final static int REFRESHITEM=100;
		final static int PHOTOEDIT=1000;
		
	}

	interface MarginWidth {
		final static int TOP = 1;
		final static int BUTTOM = 1;
		final static int LEFT = 5;
		final static int RIGHT = 5;
	}

	interface SynsRequest {
		final static String PRODUCT = "Mobile_ProductList";
		final static String ACCOUNT = "Mobile_GetClientList";
		final static String VISITPLAN = "Mobile_GetVisitPlanList";
		final static String DICTIONARY = "Mobile_GetDictList";

		final static String MESSAGE = "Mobile_GetMessageList";

	}

	interface CLIENTRLTSURVEY {
		final static String SERVERID = "serverid";
		final static String SURVEYID = "surveyid";
		final static String CLIENTSERVERID = "clientserverid";
		final static String STARTDATE = "startdate";
		final static String ENDDATE = "enddate";
		final static String TAKEPHOTO = "takephoto";

	}

	interface SynsResponse {
		final static String PRODUCT = "Mobile_ProductListResponse";
		final static String ACCOUNT = "Mobile_GetClientListResponse";
		final static String VISITPLAN = "Mobile_GetVisitPlanListResponse";
		final static String DICTIONARY = "Mobile_GetDictListResponse";

		final static String MESSAGE = "Mobile_GetMessageListResult";
	}

	interface FileType {
		final static int HTML = 0;
		final static int IMAGE = 1;
		final static int PDF = 2;

		final static int TEXT = 3;
		final static int AUDIO = 4;
		final static int VIDEO = 5;
		final static int CHM = 6;
		final static int WORD = 7;
		final static int EXCEL = 8;
		final static int PPT = 9;

	}

	interface FieldType {
		final static String STRING = "TEXT";
		final static String INT = "INTEGER";
		final static String TIMESTAMP = "TIMESTAMP";
		final static String BLOB = "BLOB";
	}

	interface ButtonList {
		final static int BACK = 1;
		final static int SAVE = 2;
		final static int PHOTO = 3;
		final static int SIGN = 4;
		final static int COLUMN_INDEX = 5;

		final static int RECORD = 6;

		final static int DOWNLOAD = 7;
		final static int SYNC = 8;
		final static int VIEWLIST = 9;

		final static int START = 10;

		final static int DELETE = 11;

		final static int VIEW = 12;
	}

	interface DataBaseName {
		@SuppressLint("SdCardPath")
		final static String DATABASE_NAME = "/sdcard/fang//fang.db";
	}

	interface UIID {
		final static int CREATORDER = 0;
		final static int SYNCORDER = 1;
		final static int QUERYRDER = 2;
		final static int QUERYITEM = 3;
		final static int QUERYACCOUNT = 4;
		final static int DASHBOARD = 5;
		final static int SYSTEMSETUP = 6;
		final static int ORDERDETAIL = 7;

		final static int DASHBOARD1 = 8;
		final static int DASHBOARD2 = 9;
		final static int DASHBOARD3 = 10;

	}

	interface ActionType {
		int VIEW = 0;
		int CREAT = 1;
		int EDIT = 2;
	}

	interface CurVersion {
		final static String VERSION ="fang1.1";
		final static String UPDATEMSG = "系统检测到新版本，请先升级";

		final static String APPNAME = "fang.apk";
		final static String DOWNLOADURL = "http://www.parrowtech.com/inoherb.apk";
	}

	//
	interface DateFormat 
	{
		String DATETIME = "yyyy-MM-dd HH:mm:ss";

		String DATETIME1 = "yyyy-MM-dd HH:mm";

		String DATE = "yyyy-MM-dd";
		String MONTH = "yyyy-MM";

		String TIME = "HH:mm:ss";
		
		String TIME1 = "HH:mm";

		String SFDC_DATETIME = "yyyy-MM-ddTHH:mm:ssZ";
	}

	// 
	interface TimeZone {
		String ZH_CH = "GMT+08:00";
		String STA = "GMT+00:00";
	}

	//
	interface PropertyKey {
		final static int ERR = 0;
		final static int SYNCERR = -1;
		final static int LOGIN = 99;
		final static int QUERY = 100;

		final static int UPLOAD = 101;
		final static int UPLOADMSG = 102;
		
		final static int RESETPWD = 103;
		final static int GETSERVERTIME = 104;

	}

	interface ExternalId {
		final static String EXTERNALID = "UniqueId__c";
		final static String VERSIONID = "UserIdVersion__c";

	}
}