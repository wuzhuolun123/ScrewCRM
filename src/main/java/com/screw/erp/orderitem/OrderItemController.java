package com.screw.erp.orderitem;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.screw.erp.order.OrderService;

public class OrderItemController extends Controller {
    @Inject
    OrderItemService orderItemService;
    public void addOrderItems(){
//     orderItemService.addOrderItems();
    }
}
