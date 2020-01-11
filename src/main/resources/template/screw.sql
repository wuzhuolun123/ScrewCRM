#namespace("screw")
 #sql("queryScrewList")
   select a.sid,a.name,a.spec,a.thousandPerPrice,a.remark ,(
    select  sum(b.itemPrice) from orderitem as b where b.sid=a.sid
   )as totalPrice
   from screw as  a
   where 1=1
   #if(name)
   and a.name like concat('%',#para(name),'%')
   #end
 #end

 #sql("getScrewTotalPrice")
   select
   #if(options=="day")
   date_format(c.createTime,'%Y-%m-%d')as day,
   #end
   ifnull(sum(b.itemPrice),0)as totalPrices
   from screw as a left join orderitem  as b on a.sid=b.sid
                    left join orders as c on b.oid=c.oid
     where a.sid=#para(sid) and
     #if(options=="day")
     date_format(c.createTime,'%Y-%m')=date_format(now(),'%Y-%m')
     group by
         day(c.createTime)
     #end

 #end
#end