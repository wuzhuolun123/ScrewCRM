package com.screw.erp.fore;

import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;

public class ForeController extends Controller{
    public void logout(){render("/fore/login/login.html");}
    public void index(){
        System.out.println("1111111");

        render("/fore/homePage.html");

    }
    public  void register(){
        render("/fore/register/register.html");
    }
    public void addOrderPage(){
        render("/fore/addOrderPage.html");
    }
    public void screwPage(){
        render("/fore/screwPage.html");
    }

    public void buyerPage(){
        render("/fore/buyerPage.html");
    }

    public void  screwAnalyzeByTime(){
        render("/fore/screwAnalyzeByTime.html");
    }
    public  void screwAnalyzeByBuyer(){render("/fore/screwAnalyzeByBuyer.html");}
    public  void screwListAnalyzeByTime(){render("/fore/screwListAnalyzeByTime.html");}
    public  void orderAnalyzeByTime(){ render("/fore/orderAnalyzeByTime.html");}
    public  void buyerAnalyzeByTime() {render("/fore/buyerAnalyzeByTime.html");}

}
