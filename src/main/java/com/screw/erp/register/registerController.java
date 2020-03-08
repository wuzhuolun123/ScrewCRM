package com.screw.erp.register;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;

public class registerController extends Controller{
    public  static final String x="";
@Inject
registerService registerservice;

//@Before(registerValidator.class)
    public void index(){

      String username=getPara("username");
      String nickname=getPara("nickname");
      String password=getPara("password");
      String passwordRepeat=getPara("passwordRepeat");

     Ret ret= registerservice.register(username,password,nickname,passwordRepeat);

     renderJson(ret);
    }

}
