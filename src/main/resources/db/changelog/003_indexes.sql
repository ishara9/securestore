-- Fix Id sequences
SELECT setval('customer_id_seq', (SELECT MAX(id) FROM customer));
SELECT setval('order_id_seq', (SELECT MAX(id) FROM "order"));

-- Index customer_id on Order
CREATE INDEX idx_order_customer_id
ON "order" (customer_id);