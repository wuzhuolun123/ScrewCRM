package com.screw.erp.screw;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

public class ScrewController extends Controller {
    @Inject
    ScrewService screwService;

    public  void addOrSaveScrew(){
        String screwData=HttpKit.readData(getRequest());
        renderJson(screwService.addOrSaveScrew(screwData));
    }

    public void getScrewList() {
        String name=null;
        if(getPara("name")!=null){
            name=getPara("name");
        }
        renderJson(screwService.getScrewList(name));
    }

    public void getScrewNameList() {
       //json字符串到了前端会自动变为json数组?
        renderJson(screwService.getScrewNameList());
    }
  //添加订单项产品时自动赋值
    public void getScrewPrice() {
        String name = getPara("name");
        set("price", screwService.getScrewPrice(name));
        renderJson();
    }
    public  void deleteScrew(){
         int sid=getParaToInt("sid");
        renderJson(screwService.deleteScrew(sid));

    }

}
