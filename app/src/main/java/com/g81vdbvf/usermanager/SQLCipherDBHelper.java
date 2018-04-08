package com.g81vdbvf.usermanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;


public class SQLCipherDBHelper extends net.sqlcipher.database.SQLiteOpenHelper {
    private static final String MIS_PREFERENCIAS = "com.g81vdbvf.usermanager.login";
    private static SharedPreferences sprefs;
    private static String pass;

    private static String DATABASE_NAME = "";
    private static final String TABLE_NAME = "users_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "GENDER";
    private static final String COL_4 = "LOCATION";
    private static final String COL_5 = "USERNAME";
    private static final String COL_6 = "PASSWORD";
    private static final String COL_7 = "REGISTERED";
    private static final String COL_8 = "PICTURE";

    private static SQLCipherDBHelper sInstance;
    private static Context cont;

    public static synchronized SQLCipherDBHelper getInstance(Context context, String DBNAME) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SQLCipherDBHelper(context.getApplicationContext(),DBNAME);
            cont = context.getApplicationContext();
        }
        sprefs = cont.getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);

        if(DBNAME.equals("PBE.db")) pass = sprefs.getString("PassBBDD","");
        else if(DBNAME.equals("KEYSTORE.db")) pass = sprefs.getString("KeyStoreBBDD","");

        return sInstance;
    }

    public void close(){
        sInstance = null;
        cont = null;
    }

    private SQLCipherDBHelper(Context context, String DBNAME){
        super(context, DBNAME, null, 1);
    }


    public void onCreate(net.sqlcipher.database.SQLiteDatabase db){
        net.sqlcipher.database.SQLiteDatabase.loadLibs(cont);
        db.execSQL("create table "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, " +
                "GENDER TEXT, LOCATION TEXT, USERNAME TEXT, PASSWORD TEXT, REGISTERED TEXT, PICTURE TEXT)");
    }

    public void onUpgrade(net.sqlcipher.database.SQLiteDatabase db, int oldVersion, int newVersion){
        net.sqlcipher.database.SQLiteDatabase.loadLibs(cont);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData( String name,String gender,String location,
                              String username, String password, String registered,
                              String picture) {
        net.sqlcipher.database.SQLiteDatabase.loadLibs(cont);
        net.sqlcipher.database.SQLiteDatabase db = this.getWritableDatabase(pass);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,gender);
        contentValues.put(COL_4,location);
        contentValues.put(COL_5,username);
        contentValues.put(COL_6,password);
        contentValues.put(COL_7,registered);
        contentValues.put(COL_8,picture);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        net.sqlcipher.database.SQLiteDatabase.loadLibs(cont);
        Log.v("SQLCIPHER-PASS", "LA pass es "+ pass);
        net.sqlcipher.database.SQLiteDatabase db = this.getWritableDatabase(pass);
        return db.rawQuery("select * from "+TABLE_NAME,null);
    }

    public boolean updateData(String id, String name,String gender,String location,
                              String username, String password, String registered,
                              String picture) {
        net.sqlcipher.database.SQLiteDatabase.loadLibs(cont);
        net.sqlcipher.database.SQLiteDatabase db = this.getWritableDatabase(pass);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,gender);
        contentValues.put(COL_4,location);
        contentValues.put(COL_5,username);
        contentValues.put(COL_6,password);
        contentValues.put(COL_7,registered);
        contentValues.put(COL_8,picture);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        net.sqlcipher.database.SQLiteDatabase.loadLibs(cont);
        net.sqlcipher.database.SQLiteDatabase db = this.getWritableDatabase(pass);
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

}

