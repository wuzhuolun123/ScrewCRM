package com.screw.erp.screw;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class ScrewAnalyzeController extends Controller{

    @Inject
    ScrewAnalyzeService screwAnalyzeService;

    public void getData(){
        int sid=getParaToInt("sid");
       String option=getPara("option");
       renderJson(screwAnalyzeService.getData(sid,option));
    }
}
