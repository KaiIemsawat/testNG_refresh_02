SELECT * FROM customers;

-- CREATE Store Function --
DELIMITER //
	CREATE FUNCTION CustomerLevel(credit Decimal(10, 2)) RETURNS varchar(20)
    DETERMINISTIC
    BEGIN
		DECLARE customerLevel varchar(20);
        IF credit > 50000
			THEN SET customerLevel = 'PLATINUM';
		ELSEIF (credit >= 10000 AND credit <=50000)
			THEN SET customerLevel = 'GOLD';
		ELSEIF credit < 10000
			THEN SET customerLevel = 'SILVER';
		END IF;
        RETURN customerLevel;
    END	//
DELIMITER ;

SHOW FUNCTION STATUS WHERE db = 'classicmodels';

SELECT customerName, CustomerLevel(creditLimit) FROM customers;

-- CREATE Store Procedure --
DELIMITER //
CREATE PROCEDURE GetCustomerLevel(
	IN customerNo INT, OUT customerLevel VARCHAR(20)
)
	BEGIN
		DECLARE credit DEC(10, 2) DEFAULT 0;
        SELECT creditLimit INTO credit FROM customers WHERE customerNumber = customerNo;
        
        SET customerLevel = CustomerLevel(credit);
	END //
DELIMITER ;
        
        
        
        