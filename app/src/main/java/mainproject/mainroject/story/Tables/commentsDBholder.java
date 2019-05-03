package mainproject.mainroject.story.Tables;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

public class commentsDBholder extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "comments.comments";
    private static final String _ID = "id";
    private static final String TABLE_NAME = "comments";
    private static final String COLUMN_NAME_TITLE = "currentstoryname";
    private static final String COLUMN_NAME_SUBTITLE = "username1";
    private static final String COLUMN_NAME_Comment = "usercomment";
    private static final String COLUMN_NAME_user___Email = "user___Email";
    private HashMap hp;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + commentsDBholder.TABLE_NAME + " (" +
                    commentsDBholder._ID + " INTEGER PRIMARY KEY," +
                    commentsDBholder.COLUMN_NAME_TITLE + " TEXT," +
                    commentsDBholder.COLUMN_NAME_SUBTITLE + " TEXT," +
                    commentsDBholder.COLUMN_NAME_Comment+ " TEXT,"+
                    commentsDBholder.COLUMN_NAME_user___Email + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + commentsDBholder.TABLE_NAME;
    public commentsDBholder(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, null, 1, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertcomments ( String currentstoryname, String username1, String usercomment,String user___Email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("currentstoryname", currentstoryname);
        contentValues.put("username1", username1);
        contentValues.put("usercomment", usercomment);
        contentValues.put("user___Email", user___Email);
        db.insert("comments", null, contentValues);
        return true;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from comments where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
    public boolean updatecomments (Integer id ,String currentstoryname, String username1, String usercomment,String user___Email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("currentstoryname", currentstoryname);
        contentValues.put("username1", username1);
        contentValues.put("usercomment", usercomment);
        contentValues.put("user___Email", user___Email);
        db.update("comments", contentValues,"id= ?",new String[]{Integer.toString(id)});
        return true;
    }
    public ArrayList<String> getAllcomments() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME_TITLE)));
            res.moveToNext();
        }
        return array_list;
    }
}
