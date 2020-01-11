package com.screw.erp.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.screw.erp.orderitem.OrderItemService;

import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

public class OrderController extends Controller {
    @Inject
    OrderService orderService;
    @Inject
    OrderItemService orderItemService;

    public void addOrSaveOrder() {
        String data = HttpKit.readData(getRequest());
        System.out.println(data);
      //将json字符串转为json对象
        JSONObject dataFromFront = JSON.parseObject(data, JSONObject.class);

        JSONArray orderItemsArray= dataFromFront.getJSONArray("orderItems");
        String buyerName = dataFromFront.getString("buyerName");
        int status = dataFromFront.getInteger("status")==null?0:dataFromFront.getInteger("status");
        String remark = dataFromFront.getString("remark");

        float totalPrices = 0;
        //先计算订单总价后保存订单项，生成oid供订单项作为外键保存


        for (int i = 0; i < orderItemsArray.size(); i++) {
            JSONObject orderItem = orderItemsArray.getJSONObject(i);
            float totalPrice = orderItem.getFloat("totalPrice");
            totalPrices += totalPrice;
        }

        int oid = orderService.addOrSaveOrder(buyerName, status, remark, totalPrices);
        for (int i = 0; i <  orderItemsArray.size(); i++) {
            JSONObject orderItem = orderItemsArray.getJSONObject(i);
            String screwName = orderItem.getString("screwName");
            int num = orderItem.getInteger("num");
            float totalPrice = orderItem.getFloat("totalPrice");

            orderItemService.addOrderItems(oid, screwName, num, totalPrice);
        }

        renderText("成了");
    }

    //查询所有订单项
    public void getOrderList(){

        renderJson(orderService.getOrderList());
    }
    //多条件查询订单项  若为空则用空值进行查询
    public void getOrderListByCondition(){
        String status=getPara("status")==null?null:getPara("status");
        String time=getPara("time")==null?null:getPara("time");
        String buyerName=getPara("buyerName")==null?null:getPara("buyerName");

        renderJson(orderService.getOrderListByCondition(status,time,buyerName));
    }
    //删除订单和相关订单项
    public  void deleteOrderList(){
      int oid=getParaToInt("oid");
    renderJson(orderService.deleteOrderList(oid));
    }
}

