--SQL 5. 练习 and
--用销售 sale 那个数据表
--价格在20元以上，而且库存数量(quantity)大于1个的物品的编号,价格和库存
select item_id, price, quantity
from sale
where price>20 and quantity>1
