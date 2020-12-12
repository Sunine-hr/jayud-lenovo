--20201203 二期优化,优先级1
ALTER TABLE `order_send_cars`
DROP COLUMN `encode`,
DROP COLUMN `encode_url`,
DROP COLUMN `encode_url_name`;