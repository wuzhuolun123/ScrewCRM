package com.screw.erp.login;

import cn.hutool.core.util.RandomUtil;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.screw.erp.model.Account;
import com.screw.erp.model.Session;
import com.sun.prism.impl.Disposer;

import java.nio.channels.SeekableByteChannel;
import java.util.UUID;

public class LoginService {
    public static final String loginAccountCacheName = "loginaccount";
   //首次登录时 新建 session 和缓存
    public Ret login(String username, String password, boolean keepLogin) {
        username.trim();
        password.trim();
        Account account =  Account.dao.findFirst("select * from account where username=?", username);
        if (account == null) {
            return Ret.fail("msg", "帐号不存在");
        }
        if (!account.isStatusOk()) {
            return Ret.fail("msg", "帐号暂时禁用");
        }
        String salt=account.getSalt();
       String encodedPassword= HashKit.sha256(password + salt);
        if (account.getPassword().equals(encodedPassword) == false) {
            return Ret.fail("msg", "密码错误");
        } else {

            long sessionLiveSeconds = keepLogin ? 3 * 365 * 24 * 60 * 60 : 120 * 6;
            int cookieLiveSeconds = (int) (keepLogin ? sessionLiveSeconds : -1);
            String sid = StrKit.getRandomUUID();

            Session session = new Session();
            session.setId(sid);
            session.setAccountId(account.getId());
            session.setExpireAt(sessionLiveSeconds * 1000 + System.currentTimeMillis());
            session.save();
            account.remove("password", "salt");
            //缓存的键值对是 sid account 而cookie中存的是  "sessionId",sid   Session存的是 sid aid
            CacheKit.put(loginAccountCacheName, sid, account);
            return Ret.ok("msg", "登录成功").set("cookieLiveSeconds", cookieLiveSeconds).set(loginAccountCacheName, account)
                    .set("sid", sid);
        }

    }

    public Account getLoginAccountByCache(String sid) {
        return CacheKit.get(loginAccountCacheName, sid);
    }

    public Account getLoginAccountByDao(String sid) {
        Session session = Session.dao.findById(sid);
        if (session == null) {
            return null;
        }
        if (session.isExpired()) {
            session.delete();
            return null;
        }
        Account account = Account.dao.findById(session.getAccountId());
        if (account != null && account.isStatusOk()) {
            account.remove("salt", "password");
            CacheKit.put(loginAccountCacheName, sid, account);
            return account;
        }
        return null;
    }


    public void logout(String sid) {
        CacheKit.remove(loginAccountCacheName, sid);
        Session.dao.deleteById(sid);

    }


}
