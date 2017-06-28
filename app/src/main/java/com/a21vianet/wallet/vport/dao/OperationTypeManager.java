package com.a21vianet.wallet.vport.dao;

import com.a21vianet.wallet.vport.dao.entity.OperationType;
import com.a21vianet.wallet.vport.dao.gen.OperationTypeDao;

import org.greenrobot.greendao.rx.RxDao;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

public class OperationTypeManager {
    public static void insert(OperationType type) {
        type.setId(null);
        OperationTypeDao typeDao = GreenDaoManager.getInstance().getSession()
                .getOperationTypeDao();
        typeDao.insertOrReplace(type);
    }

    public static void update(OperationType type) {
        OperationTypeDao typeDao = GreenDaoManager.getInstance().getSession()
                .getOperationTypeDao();
        typeDao.update(type);
    }

    public static OperationType get(int id) {
        OperationTypeDao typeDao = GreenDaoManager.getInstance().getSession()
                .getOperationTypeDao();
        return typeDao.load((long) id);
    }

    public static List<OperationType> load() {
        OperationTypeDao typeDao = GreenDaoManager.getInstance().getSession()
                .getOperationTypeDao();
        return typeDao.loadAll();
    }

    public static void inittable() {
        RxDao<OperationType, Long> rx = GreenDaoManager.getInstance().getSession()
                .getOperationTypeDao().rx();
        rx.insertOrReplaceInTx(
                new OperationType((long) 1, "登录"),
                new OperationType((long) 2, "认证"),
                new OperationType((long) 3, "授权")
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object[]>() {
            @Override
            public void call(Object[] objects) {

            }
        });
    }
}
