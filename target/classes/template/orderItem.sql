#namespace("orderItem")
 #sql("deleteOrderItemByOid")
    delete from orderitem as a where a.oid=#para(0)

 #end
#end