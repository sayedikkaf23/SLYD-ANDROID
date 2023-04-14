package chat.hola.com.app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.post.model.PostData;

/**
 * Created by DELL on 3/26/2018.
 */

public class PostDb extends SQLiteOpenHelper {
    private static final String TABLE_POST = "post_table";
    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String POST_DATA = "post_data";
    private static final String STATUS = "status";
    private static final String FACEBOOK_POST = "facebook_post";
    private static final String TWITTER_POST = "twitter_post";
    private static final String MERGED = "merged";
    private static final String DATABASE_NAME = "post";
    private static final int DATABASE_VERSION = 6;


    @Inject
    public PostDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_POST + "(" + ID + " TEXT PRIMARY KEY ,"
                + USER_ID + " TEXT ," + POST_DATA + " TEXT ," + STATUS + " INTEGER, "
                + FACEBOOK_POST + " BOOLEAN DEFAULT false,"+
                TWITTER_POST + " BOOLEAN DEFAULT false,"
                + MERGED + " BOOLEAN DEFAULT false )";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        onCreate(sqLiteDatabase);
    }


    public void addData(PostData postData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, postData.getId());
        values.put(USER_ID, postData.getUserId());
        values.put(POST_DATA, postData.getData());
        values.put(STATUS, postData.getStatus());
        values.put(FACEBOOK_POST, postData.isFbShare());
        values.put(TWITTER_POST, postData.isTwitterShare());
        values.put(MERGED, postData.isMerged());
        db.insert(TABLE_POST, null, values);
        db.close();
    }

    public List<PostData> getAllData() {
        List<PostData> dataList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_POST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PostData data = new PostData();
                data.setId(cursor.getString(0));
                data.setUserId(cursor.getString(1));
                data.setData(cursor.getString(2));
                data.setStatus(Integer.parseInt(cursor.getString(3)));
                data.setFbShare(cursor.getInt(4) > 0);
                data.setTwitterShare(cursor.getInt(5) > 0);
                data.setMerged(cursor.getInt(6) > 0);

                dataList.add(data);
            } while (cursor.moveToNext());
        }
cursor.close();
        return dataList;
    }

    public int updateStatus(PostData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, data.getUserId());
        values.put(POST_DATA, data.getData());
        values.put(STATUS, data.getStatus());
        values.put(FACEBOOK_POST, data.isFbShare());
        values.put(TWITTER_POST, data.isTwitterShare());
        values.put(MERGED, data.isMerged());

        return db.update(TABLE_POST, values, POST_DATA + " = ?",
                new String[]{String.valueOf(data.getData())});
    }



    public int updateFileMergedStatus(PostData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, data.getUserId());
        values.put(POST_DATA, data.getData());
        values.put(STATUS, data.getStatus());
        values.put(FACEBOOK_POST, data.isFbShare());
        values.put(TWITTER_POST, data.isTwitterShare());
        values.put(MERGED, true);

        return db.update(TABLE_POST, values, ID + " = ?",
                new String[]{String.valueOf(data.getId())});
    }





    public boolean delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag = db.delete(TABLE_POST, ID + "=" + id, null) > 0;
        db.close();
        return flag;
    }

    public boolean delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag = db.delete(TABLE_POST, null, null) > 0;
        db.close();
        return flag;
    }
}
