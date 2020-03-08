package com.screw.erp.buyer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

import java.util.Map;

public class BuyerController extends Controller {
    @Inject
    BuyerService buyerService;

    //添加或编辑买家
    public void addOrSaveBuyer(){
        String newBuyer = HttpKit.readData(getRequest());
        JSONObject jsonObject = JSON.parseObject(newBuyer);
        renderJson(buyerService.addOrSaveBuyer(jsonObject));
    }
   //添加订单时 渲染买家选择框
    public void getBuyerNameList() {
        renderJson(buyerService.getBuyerNameList());
    }
   //筛选买家信息，渲染买家表格
    public void getBuyerList() {
        String buyerName=getPara("buyerName")==null?null:getPara("buyerName");
        renderJson(buyerService.getBuyerList(buyerName));
    }
    //删除买家
    public void deleteBuyerByBid() {
      int bid= getParaToInt("bid");
        if (buyerService.deleteBuyerByBid(bid)) {
         set("status",200);
            renderJson();
        } else {
            set("status",404);
            renderJson();
        }
    }

}
