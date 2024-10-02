CREATE database classicmodels;

use classicmodels;

SHOW tables;

SELECT * FROM customers;

SELECT COUNT(*) AS NumberOfColumns FROM information_schema.columns WHERE table_name = 'customers';

SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'customers';

SELECT * FROM customers;