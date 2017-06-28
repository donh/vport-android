package com.a21vianet.wallet.vport.dao.entity;

import com.a21vianet.wallet.vport.dao.bean.OperationTypeEnum;
import com.a21vianet.wallet.vport.dao.gen.DaoSession;
import com.a21vianet.wallet.vport.dao.gen.OperatingDataDao;
import com.a21vianet.wallet.vport.dao.gen.OperationTypeDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;
import java.util.List;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

@Entity
public class OperatingData {
    @Id(autoincrement = true)
    private Long id;

    private String username;
    private String userimg;
    private String appname;
    private String appimg;
    private String appurl;
    private String operationmsg;
    private long operationtypeId;
    private Date operationtime;
    //以 1_2_3 的方式隔开身份证id
    private String identityCards;


    @Transient
    private List<IdentityCard> IdentityCardBeans;

    @ToOne(joinProperty = "operationtypeId")
    private OperationType operationtype;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1778719501)
    private transient OperatingDataDao myDao;

    @Generated(hash = 993601222)
    private transient Long operationtype__resolvedKey;

    public OperatingData(String username, String userimg, String appname,
                         String appimg, String appurl, String operationmsg, OperationTypeEnum
                                 anEnum, String identityCards) {
        this.username = username;
        this.userimg = userimg;
        this.appname = appname;
        this.appimg = appimg;
        this.appurl = appurl;
        this.operationmsg = operationmsg;
        this.operationtypeId = anEnum.typeId;
        this.identityCards = identityCards;
        this.operationtime = new Date();
    }

    @Generated(hash = 1959987320)
    public OperatingData(Long id, String username, String userimg, String appname,
            String appimg, String appurl, String operationmsg, long operationtypeId,
            Date operationtime, String identityCards) {
        this.id = id;
        this.username = username;
        this.userimg = userimg;
        this.appname = appname;
        this.appimg = appimg;
        this.appurl = appurl;
        this.operationmsg = operationmsg;
        this.operationtypeId = operationtypeId;
        this.operationtime = operationtime;
        this.identityCards = identityCards;
    }

    @Generated(hash = 1127590806)
    public OperatingData() {
    }

    public List<IdentityCard> getIdentityCardBeans() {
        return IdentityCardBeans;
    }

    public void setIdentityCardBeans(List<IdentityCard> identityCardBeans) {
        IdentityCardBeans = identityCardBeans;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimg() {
        return this.userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getAppname() {
        return this.appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppimg() {
        return this.appimg;
    }

    public void setAppimg(String appimg) {
        this.appimg = appimg;
    }

    public String getAppurl() {
        return this.appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public String getOperationmsg() {
        return this.operationmsg;
    }

    public void setOperationmsg(String operationmsg) {
        this.operationmsg = operationmsg;
    }

    public long getOperationtypeId() {
        return this.operationtypeId;
    }

    public void setOperationtypeId(long operationtypeId) {
        this.operationtypeId = operationtypeId;
    }

    public Date getOperationtime() {
        return this.operationtime;
    }

    public void setOperationtime(Date operationtime) {
        this.operationtime = operationtime;
    }

    public String getIdentityCards() {
        return this.identityCards;
    }

    public void setIdentityCards(String identityCards) {
        this.identityCards = identityCards;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1074589290)
    public OperationType getOperationtype() {
        long __key = this.operationtypeId;
        if (operationtype__resolvedKey == null || !operationtype__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OperationTypeDao targetDao = daoSession.getOperationTypeDao();
            OperationType operationtypeNew = targetDao.load(__key);
            synchronized (this) {
                operationtype = operationtypeNew;
                operationtype__resolvedKey = __key;
            }
        }
        return operationtype;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 174810892)
    public void setOperationtype(@NotNull OperationType operationtype) {
        if (operationtype == null) {
            throw new DaoException(
                    "To-one property 'operationtypeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.operationtype = operationtype;
            operationtypeId = operationtype.getId();
            operationtype__resolvedKey = operationtypeId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1421340031)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOperatingDataDao() : null;
    }

}
