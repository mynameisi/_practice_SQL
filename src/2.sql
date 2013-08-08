--使用test数据库中的customer数据表
--Bob Smith家里的地址是什么【街道address, 城市city, 省state】
select c.address, c.city ,c.state 
from customer c
where c.name = 'Bob Smith';



