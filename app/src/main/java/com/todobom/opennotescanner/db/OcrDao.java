package com.todobom.opennotescanner.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface OcrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ocr ocr);

    @Delete
    void deleteOcrs(Ocr... ocrs);

    @Query("SELECT * from ocr_table WHERE uri = :uri")
    LiveData<Ocr> getOcr(String uri);

    @Update
    public void updateOcrs(Ocr... ocrs);

}
