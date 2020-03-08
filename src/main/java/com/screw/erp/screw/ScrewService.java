package com.screw.erp.screw;

import cn.hutool.json.JSONString;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.screw.erp.model.Screw;
import netscape.javascript.JSObject;

import java.util.List;

import static com.alibaba.fastjson.JSON.toJSONString;

public class ScrewService {

    public static final Screw dao = new Screw();

    public JSONObject addOrSaveScrew(String screwData){
     JSONObject jsonObject=JSONObject.parseObject(screwData);
       String name= jsonObject.getString("name");
       String spec= jsonObject.getString("spec");
       String thousandPerPriceStr= jsonObject.getString("thousandPerPrice");
       String remark=  jsonObject.getString("remark");

       float  thousandPerPrice=Float.valueOf(thousandPerPriceStr);
       JSONObject result=new JSONObject();
       //添加产品
      if(jsonObject.getInteger("sid")==null){
          result.put("action","add");

          if(dao.find("select * from screw where name=?",name)==null){
               result.put("status",404);
               return result;
          }
              Screw screw = new Screw();
               screw.setName(name);
               screw.setSpec(spec);
               screw.setThousandPerPrice(thousandPerPrice);
               screw.setRemark(remark);

            if(screw.save()) {
                result.put("status",200);
            }
         //编辑产品
          } else{
          result.put("action","edit");

          int sid=jsonObject.getInteger("sid");
          Screw screw=dao.findById(sid);
          String currentName= screw.getName();
          List<String> nameList= Db.query("select name from screw ");
          if(nameList.contains(name)&&name!=currentName){
              result.put("status",404);
              return  result;
          }
          screw.setName(name);
          screw.setSpec(spec);
          screw.setThousandPerPrice(thousandPerPrice);
          screw.setRemark(remark);

          if(screw.update()) {
              result.put("status", 200);
          }
      }
      return result;


    }
   public JSONObject getScrewList(String name){
       List<Record> screwList;
       if(name!=null){
          screwList= Db.find(Db.getSqlPara("screw.queryScrewList", Kv.by("name",name)));

       }else {
           screwList = Db.find(Db.getSql("screw.queryScrewList"));
       }
       //试试json.tojsonstring?
           String jsonString=FastJson.getJson().toJson(screwList);
       JSONArray jsonArray=JSONArray.parseArray(jsonString);
           JSONObject jsonObject=new JSONObject();
           jsonObject.put("code",0);
           jsonObject.put("msg","");
           jsonObject.put("count",1000);
           jsonObject.put("data",jsonArray);
           return jsonObject;
       }

    public String getScrewNameList(){
        List<String> list= Db.query("select name from screw order by sid");
        String jsonString=JSON.toJSONString(list);
        return jsonString;
    }
    public float getScrewPrice(String name){
        float price =Db.queryFloat("select thousandPerPrice from screw where name=?",name);
      //  System.out.println(price);
        return price;
    }
    public  JSONObject deleteScrew(int sid){
       JSONObject jsonObject=new JSONObject();
        Db.delete("delete from orderitem where sid=? ",sid);
       if(dao.deleteById(sid)){
           jsonObject.put("status",200);
        }else{
           jsonObject.put("status",404);
        }
           return jsonObject;
    }
}
