-- Index product_id for order by id queries
CREATE INDEX idx_order_product_product_id
ON order_product(product_id);

-- Index order_id for product by id queries
CREATE INDEX idx_order_product_order_id
ON order_product(order_id);