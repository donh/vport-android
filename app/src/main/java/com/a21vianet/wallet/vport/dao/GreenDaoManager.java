package com.a21vianet.wallet.vport.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

import com.a21vianet.wallet.vport.WalletApplication;
import com.a21vianet.wallet.vport.dao.gen.DaoMaster;
import com.a21vianet.wallet.vport.dao.gen.DaoSession;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager mInstance; //单例

    private GreenDaoManager() {
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(WalletApplication.getContext(), "vport", new SQLiteDatabase.CursorFactory() {

                @Override
                public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String
                        editTable, SQLiteQuery query) {
                    db.execSQL("insert INTO OPERATION_TYPE VALUES(1,'登录')");
                    db.execSQL("insert INTO OPERATION_TYPE VALUES(2,'授权')");
                    db.execSQL("insert INTO OPERATION_TYPE VALUES(3,'认证')");
                    return null;
                }
            });
            //此处为自己需要处理的表
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {//保证异步处理安全操作

                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
