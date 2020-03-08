package com.screw.erp.buyer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.screw.erp.model.Buyer;
import com.screw.erp.model.Screw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

public class BuyerService {
    public static final Buyer dao = new Buyer().dao();

    public JSONObject addOrSaveBuyer(JSONObject jsonObject) {

        String buyerName = jsonObject.getString("buyerName");
        String address = jsonObject.getString("address");
        String mobile = jsonObject.getString("mobile");
        String remark = jsonObject.getString("remark");

        JSONObject result=new JSONObject();
        //编辑买家
        if (jsonObject.getInteger("bid") != null) {
            result.put("action","edit");
            int  bid=jsonObject.getInteger("bid");

            Buyer buyer = dao.findById(bid);
            buyer.setBuyerName(buyerName);
            buyer.setAddress(address);
            buyer.setMobile(mobile);
            buyer.setRemark(remark);

            List<String> buyerNameList= Db.query("select buyerName from buyer");
            String currentBuyerName=buyer.getBuyerName();
            //编辑时买家名称不能与其他买家名称相同（可以与原来名称相同）
            if(buyerNameList.contains(buyerName)&&!buyerName.equals(currentBuyerName)){
                result.put("status",404);
                return result;
            }

            if(buyer.update()){
                result.put("status",200);
            }else{
                result.put("status",404);
            }
            return result;
        }
        //添加买家
        else {
            result.put("action","add");
            List<String> buyerNameList= Db.query("select buyerName from buyer");

            if(buyerNameList.contains(buyerName)){
                 result.put("status",404);
                 return result;
            }
            Buyer buyer = new Buyer();
            buyer.setBuyerName(buyerName);
            buyer.setAddress(address);
            buyer.setMobile(mobile);
            buyer.setRemark(remark);

            if(buyer.save()){
                result.put("status",200);
            }else{
                result.put("status",404);
            }
            return result;

        }
    }
    public String getBuyerNameList() {
        List<String> list = Db.query("select buyerName from buyer order by bid");
        String jsonString = JSON.toJSONString(list);
       // System.out.println(jsonString);
        return jsonString;
    }

    public JSONObject getBuyerList(String buyerName) {
        List<Record> resultList=null;
        // 必须用map,因为sql语句中模板引擎#(buyerName)需要识别取值，用Object paras无法识别
        resultList=buyerName==null? Db.find(Db.getSql("buyer.queryBuyerList")):Db.find(Db.getSqlPara("buyer.queryBuyerList", Kv.by("buyerName",buyerName)));

         System.out.println(resultList);
        // String jsonString = FastJson.getJson().toJson(resultList);
       // JSONArray jsonArray = JSONArray.parseArray(jsonString);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", 1000);
        jsonObject.put("data", resultList);

     //System.out.println(jsonObject);
        return jsonObject;
    }
  //删除买家信息  需先删除相关订单项和订单
    public boolean deleteBuyerByBid(int bid) {

        List<Integer> orderIds = Db.query("select oid from orders where orders.bid=?", bid);
        List<Integer> allOrderItemIds = new ArrayList<>();
        orderIds.forEach((oid) -> {
            List<Integer> orderItemIds = Db.query("select orderItemId from orderitem where orderitem.oid=?", oid);
            allOrderItemIds.addAll(orderItemIds);
        });
        orderIds.forEach((id)->{
            Db.delete("delete from orders where oid=?",id);
        });
        allOrderItemIds.forEach((id)->{
            Db.delete("delete from orderitem where orderItemId=?",id);
        });
            Db.delete("delete from buyer where bid=?",bid);

            return true;

    }
}
