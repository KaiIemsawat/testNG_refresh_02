delimiter //
	CREATE procedure SelectAllCustomers()
    BEGIN
		SELECT * FROM customers;
	END //
delimiter ;

CALL SelectAllCustomers();

--------------------------------------------

delimiter //
	CREATE procedure SelectAllCustomersByCity(IN mycity varchar(50))
    BEGIN
		SELECT * FROM customers WHERE city = mycity;
	END //
delimiter ;
    
CALL SelectAllCustomersByCity('Singapore');


--------------------------------------------
delimiter //
	CREATE procedure SelectAllCustomersByCityAndPost(IN mycity varchar(50), IN pCode varchar(15))
    BEGIN
		SELECT * FROM customers 
			WHERE city = mycity AND pCode = postalCode;
	END //
delimiter ;

CALL SelectAllCustomersByCityAndPost('Singapore', '079903');

--------------------------------------------
delimiter //
	CREATE procedure SelectAllCustomersByCityAndPost(IN mycity varchar(50), IN pCode varchar(15))
    BEGIN
		SELECT * FROM customers 
			WHERE city = mycity AND pCode = postalCode;
	END //
delimiter ;

CALL SelectAllCustomersByCityAndPost('Singapore', '079903');

--------------------------------------------
delimiter //
CREATE procedure get_order_by_cust(
	IN cust_no INT,
    OUT shipped INT,
    OUT canceled INT,
    OUT resolved INT,
    OUT disputed INT
)
	BEGIN
		-- shipped
		SELECT COUNT(*) INTO shipped FROM orders WHERE customerNumber = cust_no AND status = 'shipped';
        
        -- canceled
        SELECT COUNT(*) INTO canceled FROM orders WHERE customerNumber = cust_no AND status = 'Canceled';
        
        -- resolved
        SELECT COUNT(*) INTO resolved FROM orders WHERE customerNumber = cust_no AND status = 'Resolved';
        
        -- disputed
        SELECT COUNT(*) INTO disputed FROM orders WHERE customerNumber = cust_no AND status = 'Disputed';
	END //
delimiter ;

CALL get_order_by_cust(141, @shipped, @canceled, @resolved, @disputed);

        
--------------------------------------------

delimiter //
CREATE procedure GetCustomerShipping(IN pCustomerNumber INT, OUT pShipping VARCHAR(50))
BEGIN
	DECLARE customerCountry VARCHAR(100);
    
    SELECT country INTO customerCountry FROM customers WHERE customerNumber = pCustomerNumber;
    
    CASE customerCountry
		WHEN 'USA' THEN 
			SET pShipping = '2-day Shipping';
        WHEN 'Canada' THEN 
			SET pShipping = '3-day Shipping';
        ELSE 
			SET pShipping = '5-day Shipping';
	END CASE;
END //
delimiter ;

CALL GetCustomerShipping(112, @shipping);
SELECT @shipping;

SELECT country,
	CASE 
		WHEN country = 'USA' THEN '2-day Shipping'
		WHEN country = 'Canada' THEN '3-day Shipping'
		ELSE '5-day Shipping'
	END as ShippingTime
FROM customers
WHERE customerNumber=112;


--------------------------------------------
       
       
       