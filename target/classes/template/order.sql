#namespace("order")
 #sql("queryOrderList")
   select a.oid as oid, c.buyerName as buyerName, c.mobile as tel ,a.createTime as createTime,a.status as status,a.remark as remark,a.totalPrice
   as totalPrice
   from orders as a  left join buyer as c on a.bid=c.bid
   where 1=1
    #if(status)
      and a.status=#para(status)
    #end
    #if(startTime)
       and  date_format(a.createTime,'%Y-%m-%d') BETWEEN  #para(startTime) and #para(endTime)
    #end
    #if(buyerName)
       and a.bid IN ( select group_concat(bid) from  buyer where buyer.buyerName like concat('%',#para(buyerName),'%'))
    #end
  #end

   #sql("queryOrderItemList")
   select b.name,b.thousandPerPrice,a.number,a.itemPrice
   FROM orderitem as a left join screw as b on a.sid=b.sid
   where a.oid=#para(oid)
  #end

#end

