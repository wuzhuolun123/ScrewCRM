package com.screw.erp.Interceptor;

import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.screw.erp.login.LoginService;
import com.screw.erp.model.Account;
import com.screw.erp.model.Session;

public class LoginInterceptor implements Interceptor {
    @Inject
    LoginService loginService;

    public void intercept(Invocation var1) {
        Controller controller = var1.getController();
        // cookie (name value）    getCookie(name)得到对应cookie的value
        String sid = controller.getCookie("sessionId");
        //有对应的cookie就取出account
        if (sid != null) {
            Account account = loginService.getLoginAccountByCache(sid);
            if (account == null) {
                account = loginService.getLoginAccountByDao(sid);
            }
            if (account == null) {
                controller.removeCookie("sessionId");
            } else {
                controller.setAttr(loginService.loginAccountCacheName, account);
            }

        }
        var1.invoke();

    }
}