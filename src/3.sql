--SQL 3. 练习 LIKE
--使用test数据库中的customer数据表
--找到那个叫“Bill 什么的”那个人的(名字, 城市, 邮编zip)
select name, city, zip
from customer
where lower(name) like '%bill%'



