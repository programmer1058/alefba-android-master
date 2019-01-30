package com.todobom.opennotescanner;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.todobom.opennotescanner.db.Ocr;
import com.todobom.opennotescanner.db.OcrDao;
import com.todobom.opennotescanner.db.OcrRoomDatabase;

public class OcrRepository {
    private OcrDao mOcrDao;

    public OcrRepository(Application application) {
        OcrRoomDatabase db = OcrRoomDatabase.getDatabase(application);
        mOcrDao = db.ocrDao();
    }

    public void insert(Ocr ocr) {
        new insertAsyncTask(mOcrDao).execute(ocr);
    }

    public void delete(Ocr ocr){
        new deleteAsyncTask(mOcrDao).execute(ocr);
    }

    public LiveData<Ocr> getOcr(final String uri){
        return  mOcrDao.getOcr(uri);
    }

    private static class insertAsyncTask extends AsyncTask<Ocr, Void, Void> {

        private OcrDao mAsyncTaskDao;

        insertAsyncTask(OcrDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Ocr... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Ocr, Void, Void> {
        private OcrDao mAsyncTaskDao;

        deleteAsyncTask(OcrDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Ocr... ocrs) {
            mAsyncTaskDao.deleteOcrs(ocrs);
            return null;
        }
    }
}
