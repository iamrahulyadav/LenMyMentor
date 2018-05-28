package com.nucleustech.mymentor.db;

public interface DBConstants {

	public static final int DB_VERSION = 1; // DB version changed on 12-4-2017

	public static final String DB_NAME = "Chat.db";
	/* public static final String DB_NAME = Environment.getExternalStorageDirectory() + "/Chat.db";*/ //This is for debugging purpose only

	public static final String _ID = "_id";

	final String CREATE_TABLE_BASE = "CREATE TABLE IF NOT EXISTS ";

	final String ON = " ON ";

	final String PRIMARY_KEY = " PRIMARY KEY";

	final String INTEGER = " Integer";

	final String TEXT = " TEXT";

	final String DATE_TIME = " DATETIME";

	final String BLOB = " BLOB";

	final String AUTO_ICNREMENT = " AUTOINCREMENT";

	final String UNIQUE = "UNIQUE";

	final String START_COLUMN = " ( ";

	final String FINISH_COLUMN = " ) ";

	final String COMMA = ",";

	final String ON_CONFLICT_REPLACE = "ON CONFLICT REPLACE";

	// Threads Table
	public static final String THREADS_TABLE = " threadsTable";

	public static final String THREAD_ID = "threadID";

	public static final String THREAD_NAME = "threadName";

	// OFFLINE Table
	public static final String HISTORY_TABLE = " historyTable";

	public static final String MU_ID = "muID";

	public static final String THREAD_ID_HISTORY = "threadId";

	public static final String IMAGE = "image";

	public static final String LATITUDE = "latitude";

	public static final String LONGITUDE = "longitude";

	public static final String COMMENTS = "comments";

	public static final String KEYWORDS = "keywords";

	public static final String ADDRESS = "address";

	public static final String DATE = "date";

	public static final String TIME = "time";

	public static final String SCHOOL_CODE = "schoolCode";

	public static final String RATH_NUMBER = "rathNumber";

	public static final String VILLAGE_NAME = "villageName";

	public static final String OTHER_DATA = "otherData";

	//MEMBERS DIRECTORY

	public static final String MEMBERS_DIRECTORY_TABLE = " membersDirectoryTable";

	public static final String CITY = "city";

	public static final String ID = "id";

	public static final String NAME = "name";

	public static final String ID_NO = "idNo";

	public static final String SPOUSE_NAME = "spouseName";

	public static final String CONTACT_NO = "contactNo";

	public static final String MOBILE = "mobile";

	public static final String EMAIL = "email";

	public static final String DESIGNATION = "designation";

	public static final String ADD1 = "add1";

	public static final String ADD2 = "add2";

	public static final String ADD3 = "add3";

	public static final String PIN = "pin";

	public static final String TOWN = "town";

	public static final String PIC = "pic";

}
