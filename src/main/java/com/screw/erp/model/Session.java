package com.screw.erp.model;

import com.screw.erp.model.base.BaseSession;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Session extends BaseSession<Session> {

    public static final Session dao=new Session().dao();
    public boolean isExpired(){
        return System.currentTimeMillis()>getExpireAt();
    }
}
