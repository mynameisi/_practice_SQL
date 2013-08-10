--SQL 2. 练习 =, select多列
--使用test数据库中的customer数据表
--Bob Smith家里的地址是什么【街道address, 城市city, 省state】
select address, city, state
from customer
where lower(name) like 'bob smith'



