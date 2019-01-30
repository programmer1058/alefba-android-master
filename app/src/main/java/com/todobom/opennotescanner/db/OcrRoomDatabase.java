package com.todobom.opennotescanner.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Ocr.class}, version = 1, exportSchema = false)
public abstract class OcrRoomDatabase extends RoomDatabase {
    public abstract OcrDao ocrDao();

    private static volatile OcrRoomDatabase INSTANCE;

    public static OcrRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (OcrRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            OcrRoomDatabase.class, "ocr_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
