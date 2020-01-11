package com.screw.erp.orderitem;

import cn.hutool.db.sql.Order;
import com.jfinal.plugin.activerecord.Db;
import com.screw.erp.model.Orderitem;

public class OrderItemService {

    public void addOrderItems(int oid,String screwName,int num,float totalPrice){
        Orderitem orderitem=new Orderitem();
        orderitem.setOid(oid);
        int sid= Db.queryInt("select sid from screw where name=?",screwName);
        orderitem.setSid(sid);
        orderitem.setNumber(num);
        orderitem.setItemPrice(totalPrice);
        orderitem.save();
    }
}
