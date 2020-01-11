package com.screw.erp.order;


import cn.hutool.json.JSONString;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.screw.erp.model.Orderitem;
import com.screw.erp.model.Orders;
import com.screw.erp.orderitem.OrderItemService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    OrderItemService orderItemService;

    public int addOrSaveOrder(String buyerName, int status, String remark, float totalPrices) {

        Orders order = new Orders();
        order.setCreateTime(new Date());
        if (status == 1) {
            order.setPayTime(new Date());
        }
        int bid = Db.queryInt("select bid from buyer where buyerName=?", buyerName);

        order.setBid(bid);
        order.setStatus(status);
        order.setRemark(remark);
        // 計算訂單總價
        order.setTotalPrice(totalPrices);
        order.save();

        return order.getOid();


    }

    Orders orders = new Orders().dao();
    Orderitem orderitem = new Orderitem().dao();

    public JSONObject getOrderList() {

        List<Record> orderList = Db.find(Db.getSql("order.queryOrderList"));
        orderList.forEach((record) -> {
            int oid = record.getInt("oid");
            List<Record> orderItemList = Db.find(Db.getSqlPara("order.queryOrderItemList", Kv.by("oid", oid)));
            record.set("orderItemList", orderItemList);
        });
        //拼接为前端要求的数据
        // list转为json字符串后再转为json数组
    //    String jsonString = JSON.toJSONStringWithDateFormat(orderList, "yyyy-MM-dd HH:mm:ss"); 读取失败？
        String jsonString= FastJson.getJson().toJson(orderList);

        JSONArray jsonArray = JSONArray.parseArray(jsonString);
        System.out.println(orderList);
        System.out.println(jsonArray);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", 1000);
        jsonObject.put("data", jsonArray);

        return jsonObject;
    }

    public JSONObject getOrderListByCondition(String status, String time, String buyerName) {

        if (status != null) {
            int statusInt = Integer.parseInt(status);
        }
        // time参数 2019-12-04 - 2020-01-02 将其分割为两部分 。  数据库的时间截取为相同形式
        String startTime = null;
        String endTime = null;
        if (time != null) {
            int index = time.indexOf("-", 9);
            startTime = time.substring(0, index).trim();
            endTime = time.substring(index+1).trim();
        }
        List<Record> orderList = Db.find(Db.getSqlPara("order.queryOrderList", Kv.by("status", status).set("startTime", startTime).set("endTime", endTime).set("buyerName", buyerName)));
      orderList.forEach((record)->{
          int oid=record.getInt("oid");
          List<Record> orderItemList= Db.find(Db.getSqlPara("order.queryOrderItemList", Kv.by("oid",oid)));
          record.set("orderItemList",orderItemList);
      });

         String jsonString=FastJson.getJson().toJson(orderList);
         JSONArray jsonArray=JSONArray.parseArray(jsonString);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", 1000);
        jsonObject.put("data", jsonArray);

        return jsonObject;
    }

    public JSONObject deleteOrderList(int oid) {

        JSONObject jsonObject = new JSONObject();
        if (Db.deleteById("orderitem", "orderItemId", oid) && Db.deleteById("orders", "oid", oid)) {
            jsonObject.put("status", 200);
        } else {
            jsonObject.put("status", 404);
        }
        return jsonObject;
    }


}
