--SQL 5. 练习 IN
--用销售 sale 那个数据表
--价格是29.95, 19.95,9.99中任意一个物品的编号,价格和库存
select item_id, price, quantity
from sale
where price in (29.95, 19.95, 9.99)
