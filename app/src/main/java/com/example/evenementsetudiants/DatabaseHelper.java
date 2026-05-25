package com.example.evenementsetudiants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EventsDB";
    private static final int DATABASE_VERSION = 7;

    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIME = "time";
    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COLUMN_NOTIF_ID = "id";
    public static final String COLUMN_NOTIF_MESSAGE = "message";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createEventsTable =
                "CREATE TABLE " + TABLE_EVENTS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT UNIQUE, " +
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_TIME + " TEXT, " +
                        COLUMN_LOCATION + " TEXT,"+
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_IMAGE + " TEXT)";

        db.execSQL(createEventsTable);

        String createUsersTable =
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT , " +
                        "email TEXT UNIQUE, " +
                        "filiere TEXT, " +
                        "password TEXT, " +
                        "image TEXT)";

        db.execSQL(createUsersTable);

        String createNotificationsTable =
                "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                        COLUMN_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NOTIF_MESSAGE + " TEXT)";

        db.execSQL(createNotificationsTable);

        db.execSQL("CREATE TABLE user_activities (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT, " +
                "event_title TEXT)");
        db.execSQL("CREATE TABLE user_clubs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT, " +
                "club_name TEXT)");
        db.execSQL("CREATE TABLE club_notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "club_name TEXT, " +
                "message TEXT, " +
                "date TEXT)");
        db.execSQL(
                "CREATE TABLE clubs (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT UNIQUE, " +   // 👈 مهم جدا
                        "image TEXT)"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS user_activities");
        db.execSQL("DROP TABLE IF EXISTS user_clubs");
        db.execSQL("DROP TABLE IF EXISTS club_notifications");
        onCreate(db);
    }

    public void addEvent(String title,
                         String date,
                         String time,
                         String location,
                         String description,
                         String image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, image);

        db.insert(TABLE_EVENTS, null, values);

        db.close();
    }

    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
    }
    public Cursor getTodayEvents(String date) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM events WHERE date=?",
                new String[]{date}
        );
    }

    public void addNotification(String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIF_MESSAGE, message);
        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
    }
    public void addUser(String name,
                        String email,
                        String filiere,
                        String password,
                        String image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("email", email);
        values.put("filiere", filiere);
        values.put("password", password);
        values.put("image", image);

        db.insert("users", null, values);

        db.close();
    }

    public Cursor getUser(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM users WHERE email=?",
                new String[]{email}
        );
    }
    public boolean checkUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }
    public boolean checkEmailExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email=?",
                new String[]{email}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public Cursor getNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS, null);
    }
    public void deleteEvent(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EVENTS, "id=?",
                new String[]{String.valueOf(id)});

        db.close();
    }
    public void updateEvent(int id, String title, String date, String location, String image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_IMAGE, image);

        db.update(TABLE_EVENTS, values, "id=?",
                new String[]{String.valueOf(id)});

        db.close();
    }
    public void updateNotification(int id, String message) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIF_MESSAGE, message);

        db.update(TABLE_NOTIFICATIONS,
                values,
                COLUMN_NOTIF_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }
    public void deleteNotification(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NOTIFICATIONS,
                COLUMN_NOTIF_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }
    public void updateUser(int id, String name, String email, String filiere, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("filiere", filiere);
        values.put("password", password);

        db.update("users",
                values,
                "id=?",
                new String[]{String.valueOf(id)});

        db.close();
    }
    public void deleteUser(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("users",
                "id=?",
                new String[]{String.valueOf(id)});

        db.close();
    }
    public void addParticipation(String email, String eventTitle) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_email", email);
        values.put("event_title", eventTitle);

        db.insert("user_activities", null, values);
        db.close();
    }
    public boolean alreadyParticipated(String email, String eventTitle) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM user_activities WHERE user_email=? AND event_title=?",
                new String[]{email, eventTitle}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }
    public Cursor getUserActivities(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT event_title FROM user_activities WHERE user_email=?",
                new String[]{email}
        );
    }
    public void deleteParticipation(String email, String eventTitle) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("user_activities",
                "user_email=? AND event_title=?",
                new String[]{email, eventTitle});
        db.close();
    }

    public void addClub(String email, String clubName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_email", email);
        values.put("club_name", clubName);

        db.insert("user_clubs", null, values);

        db.close();
    }
    public boolean alreadyInClub(String email, String clubName) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM user_clubs WHERE user_email=? AND club_name=?",
                new String[]{email, clubName}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }
    public void removeClub(String email, String clubName) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("user_clubs",
                "user_email=? AND club_name=?",
                new String[]{email, clubName});

        db.close();
    }
    public boolean isInClub(String email, String clubName) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM user_clubs WHERE user_email=? AND club_name=?",
                new String[]{email, clubName}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }
    public void leaveClub(String email, String clubName) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("user_clubs",
                "user_email=? AND club_name=?",
                new String[]{email, clubName});

        db.close();
    }
    public void joinClub(String email, String clubName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_email", email);
        values.put("club_name", clubName);

        db.insert("user_clubs", null, values);
        db.close();
    }
    public Cursor getUserClubs(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT club_name FROM user_clubs WHERE user_email=?",
                new String[]{email}
        );
    }
    public Cursor getTodayClubNotifications(String date) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM club_notifications WHERE date=?",
                new String[]{date}
        );
    }
    public void addClubNotification(
            String clubName,
            String message,
            String date) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("club_name", clubName);
        values.put("message", message);
        values.put("date", date);

        db.insert(
                "club_notifications",
                null,
                values
        );

        db.close();
    }

    public void addClubData(String name, String image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);

        db.insertWithOnConflict(
                "clubs",
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
        );

        db.close();
    }


    public Cursor getAllClubs() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM clubs",
                null
        );
    }


    public void deleteClub(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                "clubs",
                "id=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }
    public void updateEventFull(int id, String title, String date,
                                String time, String location, String desc) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        values.put("time", time);
        values.put("location", location);
        values.put("description", desc);

        db.update("events", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void updateClub(int id, String name, String image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);

        db.update(
                "clubs",
                values,
                "id=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }

}
