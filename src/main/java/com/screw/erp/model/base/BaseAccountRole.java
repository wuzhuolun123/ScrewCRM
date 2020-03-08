package com.screw.erp.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAccountRole<M extends BaseAccountRole<M>> extends Model<M> implements IBean {

	public void setAccountId(java.lang.Integer accountId) {
		set("accountId", accountId);
	}
	
	public java.lang.Integer getAccountId() {
		return getInt("accountId");
	}

	public void setRoleId(java.lang.Integer roleId) {
		set("roleId", roleId);
	}
	
	public java.lang.Integer getRoleId() {
		return getInt("roleId");
	}

}