package com.nucleustech.mymentor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by ritwik.rai on 21/12/17.
 */

public class UsersChatModel implements  DBConstants{

    private static UsersChatModel obj = null;


    public synchronized static UsersChatModel obj() {

        if (obj == null)
            obj = new UsersChatModel();
        return obj;

    }

    public Boolean saveHistoryData(Context context, ContentValues cv) {

        System.out.println(" ----------  ADD ROWS INTO HISTORY TABLE --------- ");
        SQLiteDatabase mdb = MyMentorDBHelper.getInstance(context).getWritableDatabase();
        mdb.beginTransaction();
        try {
            mdb.insert(HISTORY_TABLE, null, cv);
            mdb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mdb.endTransaction();
            return true;
        }

    }
    private Boolean isDatabaseEmpty(Cursor mCursor) {

        if (mCursor.moveToFirst()) {
            // NOT EMPTY
            return false;

        } else {
            // IS EMPTY
            return true;
        }

    }
    /*public void clearDBTables(Context mcContext) {

		System.out.println(" ----------  CLEAR BLOCK TABLES  --------- ");
		SQLiteDatabase mdb = DisaterManagementDatabase.getInstance(mcContext).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.delete(BLOCK_TABLE, null, null);
			mdb.delete(MPCS_PROJECT_TABLE, null, null);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mdb.endTransaction();
		}
	}*/

   /* public ArrayList<OfflineSubmission> getHistory(Context context) {

        ArrayList<OfflineSubmission> historyArray = new ArrayList<OfflineSubmission>();
        //		String[] columns = { _ID, MU_ID, THREAD_ID_HISTORY, IMAGE, LATITUDE, LONGITUDE, COMMENTS, KEYWORDS,
        //				ADDRESS, DATE, TIME, SCHOOL_CODE, VILLAGE_NAME, OTHER_DATA };

        SQLiteDatabase mdb = MyMentorDBHelper.getInstance(context).getReadableDatabase();
		/*Cursor cur = mdb.query(HISTORY_TABLE, columns, BLOCK_DISTRICT_ID + "=?" + "AND " + BLOCK_PROJ_TYPE + "=?", new String[] { districtId,
				projectType }, null, null, null);*/
       /* Cursor cur = mdb.query(HISTORY_TABLE, null, null, null, null, null, null);

        if (!isDatabaseEmpty(cur)) {
            try {
                if (cur.moveToFirst()) {
                    do {
                        OfflineSubmission offlineSubmission = new OfflineSubmission();
                        offlineSubmission.setMuId(cur.getString(cur.getColumnIndex(DBConstants.MU_ID)));
                        offlineSubmission.setThreadID(cur.getString(cur.getColumnIndex(DBConstants.THREAD_ID_HISTORY)));
                        offlineSubmission.setBase64Image(cur.getString(cur.getColumnIndex(DBConstants.IMAGE)));
                        offlineSubmission.setLatitude(cur.getString(cur.getColumnIndex(DBConstants.LATITUDE)));
                        offlineSubmission.setLongitude(cur.getString(cur.getColumnIndex(DBConstants.LONGITUDE)));
                        offlineSubmission.setComments(cur.getString(cur.getColumnIndex(DBConstants.COMMENTS)));
                        offlineSubmission.setKeywords(cur.getString(cur.getColumnIndex(DBConstants.KEYWORDS)));
                        offlineSubmission.setAddress(cur.getString(cur.getColumnIndex(DBConstants.ADDRESS)));
                        offlineSubmission.setDate(cur.getString(cur.getColumnIndex(DBConstants.DATE)));
                        offlineSubmission.setTime(cur.getString(cur.getColumnIndex(DBConstants.TIME)));
                        offlineSubmission.setSchoolCode(cur.getString(cur.getColumnIndex(DBConstants.SCHOOL_CODE)));
                        offlineSubmission.setRathNumber(cur.getString(cur.getColumnIndex(DBConstants.RATH_NUMBER)));
                        offlineSubmission.setVillageName(cur.getString(cur.getColumnIndex(DBConstants.VILLAGE_NAME)));
                        offlineSubmission.setOtherData(cur.getString(cur.getColumnIndex(DBConstants.OTHER_DATA)));

                        historyArray.add(offlineSubmission);
                    } while (cur.moveToNext());
                }
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return historyArray;
    }*/



    /**
     * fetch members according to cityName
     * */
    /*public ArrayList<Member> getMembers(Context context, String cityName) {

        ArrayList<Member> members = new ArrayList<Member>();
        String[] columns = { _ID, CITY, ID, NAME, ID_NO, SPOUSE_NAME, CONTACT_NO, MOBILE, EMAIL, DESIGNATION, ADD1, ADD2, ADD3,
                PIN, TOWN, PIC };

        SQLiteDatabase mdb = MyMentorDBHelper.getInstance(context).getReadableDatabase();
        Cursor cur = mdb.query(MEMBERS_DIRECTORY_TABLE, columns, CITY + "=?", new String[] { cityName }, null, null, null);

		/*Cursor cur = mdb.query(HISTORY_TABLE, columns, BLOCK_DISTRICT_ID + "=?" + "AND " + BLOCK_PROJ_TYPE + "=?", new String[] { districtId,
				projectType }, null, null, null);*/
        //Cursor cur = mdb.query(HISTORY_TABLE, null, null, null, null, null, null);

        /*if (!isDatabaseEmpty(cur)) {
            try {
                if (cur.moveToFirst()) {
                    do {
                        Member member = new Member();
                        member.city = cur.getString(cur.getColumnIndex(DBConstants.CITY));
                        member.id = cur.getString(cur.getColumnIndex(DBConstants.ID));
                        member.name = cur.getString(cur.getColumnIndex(DBConstants.NAME));
                        member.idNo = cur.getString(cur.getColumnIndex(DBConstants.ID_NO));
                        member.spouseName = cur.getString(cur.getColumnIndex(DBConstants.SPOUSE_NAME));
                        member.contactNo = cur.getString(cur.getColumnIndex(DBConstants.CONTACT_NO));
                        member.mobile = cur.getString(cur.getColumnIndex(DBConstants.MOBILE));
                        member.email = cur.getString(cur.getColumnIndex(DBConstants.EMAIL));
                        member.designation = cur.getString(cur.getColumnIndex(DBConstants.DESIGNATION));
                        member.add1 = cur.getString(cur.getColumnIndex(DBConstants.ADD1));
                        member.add2 = cur.getString(cur.getColumnIndex(DBConstants.ADD2));
                        member.add3 = cur.getString(cur.getColumnIndex(DBConstants.ADD3));
                        member.pin = cur.getString(cur.getColumnIndex(DBConstants.PIN));
                        member.town = cur.getString(cur.getColumnIndex(DBConstants.TOWN));
                        member.picUrl = cur.getString(cur.getColumnIndex(DBConstants.PIC));
                        members.add(member);
                    } while (cur.moveToNext());
                }
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return members;
    }*/
}
