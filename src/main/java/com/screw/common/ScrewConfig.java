package com.screw.common;


import com.jfinal.config.*;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import com.screw.erp.buyer.BuyerController;
import com.screw.erp.fore.ForeController;
import com.screw.erp.model._MappingKit;
import com.screw.erp.order.OrderController;
import com.screw.erp.screw.ScrewAnalyzeController;
import com.screw.erp.screw.ScrewController;

public class ScrewConfig extends JFinalConfig {
    /**
     * 注意：用于启动的 main 方法可以在任意 java 类中创建，在此仅为方便演示
     *      才将 main 方法放在了 DemoConfig 中
     *
     *      开发项目时，建议新建一个 App.java 或者 Start.java 这样的专用
     *      启动入口类放置用于启动的 main 方法
     */


    static Prop p;

    /**
     * 启动入口，运行此 main 方法可以启动项目，此 main 方法可以放置在任意的 Class 类定义中，不一定要放于此
     */
    public static void main(String[] args) {
        UndertowServer.create(ScrewConfig.class).addHotSwapClassPrefix("com.jfinal").start(); ;
    }

    /**
     * PropKit.useFirstFound(...) 使用参数中从左到右最先被找到的配置文件
     * 从左到右依次去找配置，找到则立即加载并立即返回，后续配置将被忽略
     */
    static void loadConfig() {
        if (p == null) {
            p = PropKit.useFirstFound("demo-config-pro.txt", "demo-config-dev.txt");
        }
    }

    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        loadConfig();
        me.setJsonFactory(new FastJsonFactory());
        me.setDevMode(p.getBoolean("devMode", false));

        /**
         * 支持 Controller、Interceptor、Validator 之中使用 @Inject 注入业务层，并且自动实现 AOP
         * 注入动作支持任意深度并自动处理循环注入
         */
        me.setInjectDependency(true);

        // 配置对超类中的属性进行注入
        me.setInjectSuperClass(true);
    }

    /**
     * 配置路由
     */
    public void configRoute(Routes me) {
      //  me.add("/", IndexController.class, "/index");	// 第三个参数为该Controller的视图存放路径
        //me.add("/blog", BlogController.class);			// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"
        me.add("/admin", ForeController.class);
        me.add("/screw", ScrewController.class);
        me.add("/buyer", BuyerController.class);
        me.add("/order", OrderController.class);
        me.add("/screwAnalyze", ScrewAnalyzeController.class);

    }

    public void configEngine(Engine me) {
     //   me.addSharedFunction("/common/_layout.html");
       me.addSharedFunction("/fore/leftAndTopPage.html");
        me.setDevMode(true);

    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        // 配置 druid 数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password").trim());
        me.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        // 所有映射在 MappingKit 中自动化搞定
        arp.addSqlTemplate("/template/order.sql");
        arp.addSqlTemplate("/template/buyer.sql");
        arp.addSqlTemplate("/template/orderItem.sql");
        arp.addSqlTemplate("/template/screw.sql");
        arp.setShowSql(true);

        _MappingKit.mapping(arp);
        me.add(arp);

        Cron4jPlugin cp=new Cron4jPlugin();
        //任务1
        //	cp.addTask("0-59/3 * * * *",new Cron4jForTest());
        me.add(cp);
        //	me.add(new EhCachePlugin());
    }

    public static DruidPlugin createDruidPlugin() {
        loadConfig();

        return new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password").trim());
    }

    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {
        me.add(new SessionInViewInterceptor(true));

    }

    /**
     * 配置处理器
     */
    public void configHandler(Handlers me) {

    }
}
