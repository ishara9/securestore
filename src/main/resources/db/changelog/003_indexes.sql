-- Index customer_id on Order
CREATE INDEX idx_order_customer_id
ON "order" (customer_id);