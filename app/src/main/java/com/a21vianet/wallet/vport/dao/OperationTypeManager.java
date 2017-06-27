package com.a21vianet.wallet.vport.dao;

import com.a21vianet.wallet.vport.dao.entity.OperationType;
import com.a21vianet.wallet.vport.dao.gen.OperationTypeDao;

import java.util.List;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

public class OperationTypeManager {
    public static void insert(OperationType type) {
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
}
