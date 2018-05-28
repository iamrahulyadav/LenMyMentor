package com.nucleustech.mymentor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * @author ritwik.rai
 */
public class MyMentorDBHelper extends SQLiteOpenHelper implements DBConstants {

	private static final String TAG = "MyMentorDBHelper";
	private static MyMentorDBHelper mDatabase;
	private SQLiteDatabase mDb;

	public MyMentorDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public static final MyMentorDBHelper getInstance(Context context) {
		if (mDatabase == null) {
			mDatabase = new MyMentorDBHelper(context);
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.i(TAG, "oncreate tables");
		// create table
		String[] createStatements = getCreatetableStatements();
		int total = createStatements.length;
		for (int i = 0; i < total; i++) {
			Log.i(TAG, "executing create query " + createStatements[i]);
			Log.i("Database", "Offline History Database created");
			db.execSQL(createStatements[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// on upgrade drop older tables
		Log.i("Tag", "Old version" + oldVersion + " New version: " + newVersion + "Constant variable version name: " + DB_VERSION);
		db.execSQL("DROP TABLE IF EXISTS " + THREADS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + MEMBERS_DIRECTORY_TABLE);

		db.setVersion(DB_VERSION);

		// create new tables
		onCreate(db);
	}

	private String[] getCreatetableStatements() {

		String[] create = new String[3];

		// THREADS table -> _id , threadID, formName, formData , parent_cat_id
		String threadsTableStatement = CREATE_TABLE_BASE + THREADS_TABLE + START_COLUMN + _ID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + THREAD_ID + TEXT + COMMA + THREAD_NAME + TEXT + COMMA + UNIQUE + START_COLUMN
				+ THREAD_ID + FINISH_COLUMN + ON_CONFLICT_REPLACE + FINISH_COLUMN;

		// HISTORY table -> _id , muId , threadId , image , latitude, longitude , comments , keywords , 
		// address , date , time , schoolCode ,villageName , otherData
		String historyTableStatement = CREATE_TABLE_BASE + HISTORY_TABLE + START_COLUMN + _ID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + MU_ID + TEXT + COMMA + THREAD_ID_HISTORY + TEXT + COMMA + IMAGE + TEXT + COMMA
				+ LATITUDE + TEXT + COMMA + LONGITUDE + TEXT + COMMA + COMMENTS + TEXT + COMMA + KEYWORDS + TEXT + COMMA
				+ ADDRESS + TEXT + COMMA + DATE + TEXT + COMMA + TIME + TEXT + COMMA + SCHOOL_CODE + TEXT + COMMA + RATH_NUMBER
				+ TEXT + COMMA + VILLAGE_NAME + TEXT + COMMA + OTHER_DATA + TEXT + COMMA + UNIQUE + START_COLUMN + DATE + COMMA
				+ TIME + FINISH_COLUMN + ON_CONFLICT_REPLACE + FINISH_COLUMN;

		// MEMBERS_DIRECTORY_TABLE --> _id, 
		String membersDirectoryTableStatement = CREATE_TABLE_BASE + MEMBERS_DIRECTORY_TABLE + START_COLUMN + _ID + INTEGER
				+ PRIMARY_KEY + AUTO_ICNREMENT + COMMA + CITY + TEXT + COMMA + ID + TEXT + COMMA + NAME + TEXT + COMMA + ID_NO
				+ TEXT + COMMA + SPOUSE_NAME + TEXT + COMMA + CONTACT_NO + TEXT + COMMA + MOBILE + TEXT + COMMA + EMAIL + TEXT
				+ COMMA + DESIGNATION + TEXT + COMMA + ADD1 + TEXT + COMMA + ADD2 + TEXT + COMMA + ADD3 + TEXT + COMMA + PIN
				+ TEXT + COMMA + TOWN + TEXT + COMMA + PIC + TEXT + COMMA + UNIQUE + START_COLUMN + ID_NO + FINISH_COLUMN
				+ ON_CONFLICT_REPLACE + FINISH_COLUMN;

		create[0] = threadsTableStatement;
		create[1] = historyTableStatement;
		create[2] = membersDirectoryTableStatement;

		return create;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {

		return mDb != null ? mDb : (mDb = super.getWritableDatabase());
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {

		return mDb != null ? mDb : (mDb = super.getReadableDatabase());
	}

	public void startmanagingcursor() {
		mDatabase.startmanagingcursor();
	}

}
