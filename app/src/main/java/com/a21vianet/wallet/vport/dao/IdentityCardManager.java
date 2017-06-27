package com.a21vianet.wallet.vport.dao;

import com.a21vianet.wallet.vport.dao.entity.IdentityCard;
import com.a21vianet.wallet.vport.dao.gen.IdentityCardDao;

import java.util.List;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

public class IdentityCardManager {
    public static void insert(IdentityCard card) {
        IdentityCardDao identityCardDao = GreenDaoManager.getInstance().getSession()
                .getIdentityCardDao();
        identityCardDao.insertOrReplace(card);
    }

    public static void update(IdentityCard card) {
        IdentityCardDao identityCardDao = GreenDaoManager.getInstance().getSession()
                .getIdentityCardDao();
        identityCardDao.update(card);
    }

    public static IdentityCard get(int id) {
        IdentityCardDao identityCardDao = GreenDaoManager.getInstance().getSession()
                .getIdentityCardDao();
        return identityCardDao.load((long) id);
    }

    public static List<IdentityCard> load() {
        IdentityCardDao identityCardDao = GreenDaoManager.getInstance().getSession()
                .getIdentityCardDao();
        return identityCardDao.loadAll();
    }
}
