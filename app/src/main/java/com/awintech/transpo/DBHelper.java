package com.awintech.transpo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, "BookedTicket2.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table BookedTicket2 " +
                        "(id integer primary key,firstname text,surname text, company_name text,company_id text,d_from text,d_to text," +
                        "bus_number text,location text, seat text, time text,phone text,date text,  amount text,transactionID text, duration text, cancelled text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS BookedTicket2");
        onCreate(db);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "BookedTicket2");
        return numRows;
    }

    public boolean insertTicket (String firstname,String surname, String phone, String company_name, String company_id,String d_from, String d_to, String bus_number, String location,
                                 String seat, String time, String amount, String transactionID, String date, String duration, String cancelled ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstname", firstname);
        contentValues.put("surname", surname);
        contentValues.put("phone", phone);
        contentValues.put("company_name", company_name);
        contentValues.put("company_id", company_id);
        contentValues.put("d_from", d_from);
        contentValues.put("d_to", d_to);
        contentValues.put("bus_number", bus_number);
        contentValues.put("location", location);
        contentValues.put("seat", seat);
        contentValues.put("time", time);
        contentValues.put("amount", amount);
        contentValues.put("transactionID", transactionID);
        contentValues.put("date", date);
        contentValues.put("duration", duration);
        contentValues.put("cancelled", cancelled);
        db.insert("BookedTicket2", null, contentValues);
        db.close();

        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from BookedTicket2 where id="+id+"", null );
        return res;
    }

    public Integer deleteTicket (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("BookedTicket2",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllContacts(String dataName) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from BookedTicket2", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndexOrThrow(dataName)));
            res.moveToNext();
        }
        return array_list;
    }

    //////////////////////////////////////////

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        // Select All Query
        String selectQuery ="select * from BookedTicket2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setFrom( cursor.getString(cursor.getColumnIndex("d_from")));
                note.setTo( cursor.getString(cursor.getColumnIndex("d_to")));
                note.setDate( cursor.getString(cursor.getColumnIndex("date")));
                note.setCompanyName( cursor.getString(cursor.getColumnIndex("company_name")));
                note.setCancelState( cursor.getString(cursor.getColumnIndex("cancelled")));
                note.setTransactionId( cursor.getString(cursor.getColumnIndex("transactionID")));
//                Note note = new Note();
//                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
//                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
//                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));
//
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }



    public void cancelTicketUpdate(String trans_id, String cancel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("cancelled",cancel); //These Fields should be your String values of actual column names

        db.update("BookedTicket2", cv, "transactionID= '"+trans_id +"' " , null);


//        String selectQuery ="update BookedTicket2 set cancelled = " + cancel + " where transactionID = " + trans_id;
//        Log.i("DATABASE: ", trans_id + cancel);
//
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.rawQuery(selectQuery, null);
//
//        }
//        catch (SQLException e){
//            Log.i("databasw: ",e.getMessage() );
//        }
    }
    ////////////////////////////////////

}
