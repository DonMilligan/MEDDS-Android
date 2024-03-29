package medds.carecenter.library;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PatientDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "meddsData";
 
    // Contacts table name
    private static final String TABLE_PATIENT = "patient_table";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_PHOTO = "photo";
 
    public PatientDatabaseHandler(Context context) 
    {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) 
    {
        String CREATE_PATIENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PATIENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOTES + " TEXT,"
                + KEY_PHOTO + " BLOB" + ")";
        db.execSQL(CREATE_PATIENT_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
 
        // Create tables again
        onCreate(db);
    }
    
    // Adding new contact
    public void addRecord(TableData record) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_NOTES, record.getNotes()); // Patient Notes
        values.put(KEY_PHOTO, record.getPhoto()); // Patient Photo
        // Inserting Row
        db.insert(TABLE_PATIENT, null, values);
        db.close(); // Closing database connection
    }
    
    // Updating single photo record
    public int updatePhoto(TableData record) 
    {
    	int row = record.getID();
    	SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO, record.getPhoto());
        
        Log.d("Patient DB: ", " Photo updating..");
        // updating row
        int update = db.update(TABLE_PATIENT, values, KEY_ID + "=" + row, null);
        if(update > 0){ db.close(); return update; }
        db.close();
        return update;
    }
    // Updating single note record
    public int updateNotes(TableData record) 
    {
    	int row = record.getID();
    	SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTES, record.getNotes());
        Log.d("Patient DB: ", " Notes updating..");
        // updating row
        int update = db.update(TABLE_PATIENT, values, KEY_ID + " = " + row, null);
        if(update > 0){ db.close(); return update; }
        db.close();
        return update;
    }
    
    // Getting single photo / note record
    public TableData getRecord(int id) 
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	 
    	Cursor cursor = db.query(TABLE_PATIENT, new String[] { KEY_ID,
                KEY_NOTES, KEY_PHOTO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
        		cursor.moveToFirst(); 
     
        TableData record = new TableData(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getBlob(2));
        db.close();
        // return contact
        return record;
    }
     
    // Getting All Contacts
    public List<TableData> getAllRecords() 
    {
    	List<TableData> recordList = new ArrayList<TableData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PATIENT;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TableData record = new TableData();
                record.setID(Integer.parseInt(cursor.getString(0)));
                record.setNotes(cursor.getString(1));
                record.setPhoto(cursor.getBlob(2));
                // Adding contact to list
                recordList.add(record);
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return recordList;
    }
     
// Getting contacts Count
// public int getContactsCount() {}
    
    // Updating single contact
    public int updateRecord(TableData record) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_NOTES, record.getNotes());
        values.put(KEY_PHOTO, record.getPhoto());
        
        // updating row
       int dbu = db.update(TABLE_PATIENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(record.getID()) });
       if(dbu > 0){ db.close(); return dbu; }
       db.close();
       return dbu;
    }
     
    // Deleting single contact
    public void deleteRecord(TableData record) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PATIENT, KEY_ID + " = ?",
                new String[] { String.valueOf(record.getID()) });
        db.close();
    }
    
    //deleting all records
    public void deleteAllRecords()
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        Log.d("Delete: ", "Deleting ..");
        // Create tables again
        onCreate(db);
    }
    
    public int getNumRows() {
        String countQuery = "SELECT  * FROM " + TABLE_PATIENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
 
        // return count
        return cursor.getCount();
    }
}