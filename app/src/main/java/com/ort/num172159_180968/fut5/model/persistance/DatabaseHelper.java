package com.ort.num172159_180968.fut5.model.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JuanMartin on 19-Nov-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "fut5";

    // Table Names
    private static final String TABLE_FIELDS = "fields";
    private static final String TABLE_USERS = "users";

    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    //private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHOTO = "photo";

    private static final String KEY_FIELD_ID = "fieldId";
    private static final String KEY_FIELD_NAME = "fieldName";
    private static final String KEY_FIELD_LAT = "fieldLat";
    private static final String KEY_FIELD_LONG = "fieldLong";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_USER_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME
            + " TEXT," + KEY_NAME + " TEXT,"
            + KEY_LAST_NAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PHOTO + " BLOB" + ")";

    private static final String CREATE_TABLE_FIELDS = "CREATE TABLE " + TABLE_FIELDS
            + "(" + KEY_FIELD_ID + " INTEGER PRIMARY KEY," + KEY_FIELD_NAME + " TEXT,"
            + KEY_FIELD_LAT + " REAL," + KEY_FIELD_LONG + " REAL" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_FIELDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);

        // create new tables
        onCreate(db);
    }

    public long createField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIELD_ID, field.getFieldId());
        values.put(KEY_FIELD_NAME, field.getFieldName());
        values.put(KEY_FIELD_LAT, field.getFieldLat());
        values.put(KEY_FIELD_LONG, field.getFieldLon());

        // insert row
        long field_id = db.insert(TABLE_FIELDS, null, values);

        return field_id;
    }

    public Field getField(long field_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FIELDS + " WHERE "
                + KEY_FIELD_ID + " = " + field_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Field fd = new Field();
        fd.setFieldId(c.getInt(c.getColumnIndex(KEY_FIELD_ID)));
        fd.setFieldName((c.getString(c.getColumnIndex(KEY_FIELD_NAME))));
        fd.setFieldLat(c.getDouble(c.getColumnIndex(KEY_FIELD_LAT)));
        fd.setFieldLon(c.getDouble(c.getColumnIndex(KEY_FIELD_LONG)));

        return fd;
    }

    public Field getField(String field_name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FIELDS + " WHERE "
                + KEY_FIELD_NAME + " = '" + field_name + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Field fd = new Field();
        fd.setFieldId(c.getInt(c.getColumnIndex(KEY_FIELD_ID)));
        fd.setFieldName((c.getString(c.getColumnIndex(KEY_FIELD_NAME))));
        fd.setFieldLat(c.getDouble(c.getColumnIndex(KEY_FIELD_LAT)));
        fd.setFieldLon(c.getDouble(c.getColumnIndex(KEY_FIELD_LONG)));

        return fd;
    }

    public List<Field> getAllFields() {
        List<Field> fields = new ArrayList<Field>();
        String selectQuery = "SELECT  * FROM " + TABLE_FIELDS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Field fd = new Field();
                fd.setFieldId(c.getInt(c.getColumnIndex(KEY_FIELD_ID)));
                fd.setFieldName((c.getString(c.getColumnIndex(KEY_FIELD_NAME))));
                fd.setFieldLat(c.getDouble(c.getColumnIndex(KEY_FIELD_LAT)));
                fd.setFieldLon(c.getDouble(c.getColumnIndex(KEY_FIELD_LONG)));

                // adding to todo list
                fields.add(fd);
            } while (c.moveToNext());
        }

        return fields;
    }

    public int updateFields(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIELD_LAT, field.getFieldLat());
        values.put(KEY_FIELD_LONG, field.getFieldLon());

        // updating row
        return db.update(TABLE_FIELDS, values, KEY_FIELD_ID + " = ?",
                new String[] { String.valueOf(field.getFieldId()) });
    }

    public void deleteField(long field_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FIELDS, KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(field_id)});
    }

    public void deleteField(String field_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FIELDS, KEY_FIELD_NAME + " = ?",
                new String[]{field_name});
    }

    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getUserId());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_NAME, user.getFirstName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHOTO, user.getPhoto());

        // insert row
        long user_id = db.insert(TABLE_USERS, null, values);

        return user_id;
    }

    public User getUser(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_USER_ID + " = " + user_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        User us = new User();
        us.setUserId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
        us.setUsername((c.getString(c.getColumnIndex(KEY_USERNAME))));
        us.setFirstName((c.getString(c.getColumnIndex(KEY_NAME))));
        us.setLastName((c.getString(c.getColumnIndex(KEY_LAST_NAME))));
        us.setEmail((c.getString(c.getColumnIndex(KEY_EMAIL))));
        us.setPhoto((c.getString(c.getColumnIndex(KEY_PHOTO))));

        return us;
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_USERNAME + " = '" + username + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        User us = new User();
        us.setUserId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
        us.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
        us.setFirstName(c.getString(c.getColumnIndex(KEY_NAME)));
        us.setLastName(c.getString(c.getColumnIndex(KEY_LAST_NAME)));
        us.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        us.setPhoto(c.getString(c.getColumnIndex(KEY_PHOTO)));

        return us;
    }

    public boolean existsUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_USERNAME + " = '" + username + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null){
            return c.getCount() > 0;
        }
        return false;
    }

    public List<User> getAllUser() {
        List<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User us = new User();
                us.setUserId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                us.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                us.setFirstName(c.getString(c.getColumnIndex(KEY_NAME)));
                us.setLastName(c.getString(c.getColumnIndex(KEY_LAST_NAME)));
                us.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                us.setPhoto(c.getString(c.getColumnIndex(KEY_PHOTO)));

                // adding to todo list
                users.add(us);
            } while (c.moveToNext());
        }

        return users;
    }

    public int updateUsers(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_NAME, user.getFirstName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHOTO, user.getPhoto());

        // updating row
        return db.update(TABLE_USERS, values, KEY_USER_ID + " = ?",
                new String[] { String.valueOf(user.getUserId()) });
    }

    public void deleteUser(long user_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_USER_ID + " = ?",
                new String[] { String.valueOf(user_Id) });
    }

    public void deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_USERNAME + " = ?",
                new String[] { username });
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
