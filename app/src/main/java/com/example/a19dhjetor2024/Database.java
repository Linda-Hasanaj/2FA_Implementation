package com.example.a19dhjetor2024;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import org.mindrot.jbcrypt.BCrypt;
public class Database extends SQLiteOpenHelper {
    public static final String DBNAME = "twofa.db";
    public Database(@Nullable Context context){
        super(context, "twofa.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE adminUser(email TEXT PRIMARY KEY, password TEXT, name TEXT, surname TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS adminUser");
        onCreate(db);
    }

    public Boolean validateUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM adminUser WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(0);
            cursor.close();

            return BCrypt.checkpw(password, storedHashedPassword);
        }
        if (cursor != null) {
            cursor.close();
        }

        return false;
    }

public Boolean insertAdminUser(String name, String surname, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("email", email);

        contentValues.put("password", hashedPassword);
        long result = db.insert("adminUser", null, contentValues);
        return result != -1;
}

    public Boolean checkAdminEmail(String email) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM adminUser WHERE email=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

}
