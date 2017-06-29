package com.a21vianet.wallet.vport.dao;

import com.a21vianet.wallet.vport.WalletApplication;
import com.a21vianet.wallet.vport.dao.entity.IdentityCard;
import com.a21vianet.wallet.vport.dao.entity.OperatingData;
import com.a21vianet.wallet.vport.dao.entity.OperationType;
import com.a21vianet.wallet.vport.dao.gen.DaoMaster;
import com.a21vianet.wallet.vport.dao.gen.DaoSession;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

public class GreenDaoManager {
    private static final String DATABASE_NAME = "vport";

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager mInstance; //单例

    private GreenDaoManager() {
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(WalletApplication.getContext(), DATABASE_NAME, null);
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
                    OperationTypeManager.inittable();
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

    public void deleteDatabase() {
        getSession().deleteAll(IdentityCard.class);
        getSession().deleteAll(OperatingData.class);
        getSession().deleteAll(OperationType.class);
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
