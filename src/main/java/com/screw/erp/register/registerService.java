package com.screw.erp.register;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.screw.erp.model.Account;

import java.util.Date;
import java.util.List;

public class registerService {

    public Ret register(String username, String password, String nickname, String passwordRepeat) {
        username.trim();
        password.trim();
        passwordRepeat.trim();
        if (!password.equals(passwordRepeat)) {
            return Ret.fail("msg", "两次输入密码不同");
        }
        List<String> usernameList= Db.query("select username from account" );

        if(usernameList.contains(username)){
         return Ret.fail("msg","已存在相同帐号");
        }


        Account account = new Account();
        account.setNickName(nickname);
        account.setUserName(username);

        String salt=HashKit.generateSaltForSha256();
        account.setSalt(salt);
        String passwordEncoded = HashKit.sha256(password + salt);
        account.setPassword(passwordEncoded);

        account.setCreateAt(new Date());
        account.setStatus(1);
        account.setAvatar("0/1.jpg");
        if(account.save()){
            return Ret.ok("msg","创建帐号成功");
        }
        return Ret.fail("msg","未知错误");

    }
}
