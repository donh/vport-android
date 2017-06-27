package com.a21vianet.wallet.vport.dao;

import com.a21vianet.wallet.vport.dao.bean.OperatingDataPage;
import com.a21vianet.wallet.vport.dao.entity.IdentityCard;
import com.a21vianet.wallet.vport.dao.entity.OperatingData;
import com.a21vianet.wallet.vport.dao.gen.IdentityCardDao;
import com.a21vianet.wallet.vport.dao.gen.OperatingDataDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

public class OperatingDataManager {
    public static void insert(final OperatingData data) {
        OperatingDataDao operatingDataDao = GreenDaoManager.getInstance().getSession()
                .getOperatingDataDao();

        operatingDataDao.insertOrReplace(data);
    }

    public static OperatingData get(final long id) {
        OperatingDataDao operatingDataDao = GreenDaoManager.getInstance().getSession()
                .getOperatingDataDao();
        OperatingData unique = operatingDataDao.load(id);
        unique.setIdentityCardBeans(getIdentityCard(unique.getIdentityCards()));
        return unique;
    }

    public static OperatingDataPage load(final int index, final int size) {
        OperatingDataDao operatingDataDao = GreenDaoManager.getInstance().getSession()
                .getOperatingDataDao();

        OperatingDataPage operatingDataPage = new OperatingDataPage();
        List<OperatingData> list = operatingDataDao.queryBuilder()
                .offset(index)
                .limit(size)
                .orderAsc(OperatingDataDao.Properties.Operationtime)
                .build()
                .list();
        for (OperatingData data : list) {
            data.setIdentityCardBeans(getIdentityCard(data.getIdentityCards()));
        }
        operatingDataPage.setOperatingDatas(list);

        operatingDataPage.setOffset(index + operatingDataPage.getSize());
        return operatingDataPage;
    }

    private static List<IdentityCard> getIdentityCard(String ids) {
        IdentityCardDao identityCardDao = GreenDaoManager.getInstance().getSession()
                .getIdentityCardDao();
        String[] split = ids.split("_");
        List<IdentityCard> identityCardList = new ArrayList<>();
        if (split != null) {
            for (String s : split) {
                identityCardList.add(identityCardDao.load(Long.parseLong(s)));
            }
        }
        return identityCardList;
    }
}
