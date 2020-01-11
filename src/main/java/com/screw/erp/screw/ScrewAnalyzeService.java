package com.screw.erp.screw;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.stat.ast.Else;
import com.sun.prism.impl.Disposer;

import java.util.List;

public class ScrewAnalyzeService {

    public JSONObject getData(int sid, String option) {
        List<Record> resultList;
        // 每日/月/年的单个产品销量数据  形式：
    //    System.out.println(sid);
        resultList = Db.find(Db.getSqlPara("screw.getScrewTotalPrice", Kv.by("sid", sid).set("options", option)));
//        switch (option) {
//            case "day":
//                int i=1;
//                resultList.forEach((record)->{record.set("xAxis",i);});
//            case "month":
//        }
        String jsonString = FastJson.getJson().toJson(resultList);
        JSONArray jsonArray = JSONArray.parseArray(jsonString);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }
}
