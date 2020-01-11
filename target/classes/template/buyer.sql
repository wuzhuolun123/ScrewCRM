#namespace("buyer")
 #sql("queryBuyerList")
   select  d.bid ,d.buyerName,d.address,d.mobile,d.remark,(
   select  sum(c.itemPrice)
   from buyer as a left join orders as b on a.bid=b.bid
                    left join orderitem as c on b.oid=c.oid
   where a.bid=d.bid
   )as totalPrice
   from buyer as d
   where 1=1
   #if(buyerName)
  and d.buyerName LIKE concat('%',#para(buyerName),'%')
   #end

 #end


#end