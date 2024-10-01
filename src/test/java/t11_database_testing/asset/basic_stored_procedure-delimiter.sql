delimiter //
	CREATE procedure SelectAllCustomers()
    BEGIN
		SELECT * FROM customers;
	END //
delimiter ;

CALL SelectAllCustomers();