package com.screw.erp.login;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;

public class LoginController extends Controller{
  @Inject
  LoginService loginService;
    public void index(){
      String username=getPara("username");
      String password=getPara("password");

//      boolean keepLogin=getParaToBoolean("keeplogin");
        boolean keepLogin=true;

      //service层新建session(sid aid) 和缓存(sid:account) ,返回新建的sid和cookieLiveTime 用于新建cookie
      Ret ret=loginService.login(username,password,keepLogin);
      if(ret.isOk()) {
       String sid=ret.getStr("sid");
        int cookieLiveTime=ret.getInt("cookieLiveSeconds");
        // 新建cookie （name value)
        setCookie("sessionId",sid , cookieLiveTime, true);
      }
      renderJson(ret);

    }
    //退出时删除相应cookie 同时删除数据库里的session和缓存
    @Clear
    @ActionKey("logout")
    public void logout(){
      loginService.logout(getCookie("sessionId"));
       removeCookie("sessionId");
       redirect("/");

    }

}
