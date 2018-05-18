

/***************************************************************************************************************************************
 * So Basically I resorted to using three functions to accomplish this task. 
 * ---One function called `to_string_left` 
 *   -converts all the digits before decimal point to words. 
 * ---The next function called `to_string_right` converts the digits
 *   -after decimal point to word. This, I had to limit its work to only 2 decimal places to handle just cents which are normally 2digits.
 *   -So basically it rounds off every number after d.place to 2decimal places then converts it to word
 * ---The last function called `final_string` is the one to combine the results.
 * To test the functions:
 *   1.   Paste the `to_string_left` function in ur database, make sure it does not throw any errors; if it does, paste again. 
 *       -I discovered that it throws errors on pasting the first time but on repeat, it behaves as expected
 *   2.   Paste the `to_string_right` function in ur database. Surely this should not throw any errors.
 *   3.   Paste the `final_string` function to the database.
 *   4.   Test it by executing the `final_strig` function as: SELECT final_string(1234567895.34)$$
 ***************************************************************************************************************************************/


/*********************************************************************************
 * The SQL Function for converting the left values to word..works up to 24 digits,
 * but my computer can't allow input beyond 1 Billion, but I extended the function 
 * to accommodate up to 24 digit before the decimal points, and I am not very sure 
 * about the naming of the amounts after trillion. I just placed any how
 ******************************************************************************/
/*
	DELIMITER $$
	DROP FUNCTION IF EXISTS to_string_left$$
	CREATE FUNCTION `to_string_left`(n VARCHAR(1000)) RETURNS varchar(1000)
	
	 BEGIN
	    declare ans varchar(500);
	    declare ansright varchar(500);
	    declare finalanswer varchar(500);
	    declare leftValue int(200);
	    declare rightValue FLOAT; 
	    declare dig1, dig2, dig3,dig4,dig5,dig6,dig7,dig8,dig9,dig10,dig11,dig12,dig13,dig14,dig15,
            dig16,dig17,dig18,dig19,dig20,dig21,dig22,dig23,dig24 int(200);

	set ans = '';

	select TRUNCATE(n,0) INTO leftValue;
	
            set dig24 = floor(leftValue/100000000000000000000000);
            set dig23 = floor(leftValue/10000000000000000000000) -dig12*10;
	        set dig22 = floor(leftValue/1000000000000000000000)  -(floor(leftValue/10000000000000000000000)*10);
	        set dig21 =  floor(leftValue/100000000000000000000)  -(floor(leftValue/1000000000000000000000)*10);
	        set dig20 =  floor(leftValue/10000000000000000000)   -(floor(leftValue/100000000000000000000)*10);
	        set dig19 =  floor(leftValue/1000000000000000000)    -(floor(leftValue/10000000000000000000)*10);
	        set dig18 =  floor(leftValue/100000000000000000)     -(floor(leftValue/1000000000000000000)*10);
	        set dig17 =  floor(leftValue/10000000000000000)      -(floor(leftValue/100000000000000000)*10);
	        set dig16 =  floor(leftValue/1000000000000000)       -(floor(leftValue/10000000000000000)*10);
            set dig15 =  floor(leftValue/100000000000000)        -(floor(leftValue/1000000000000000)*10);
	        set dig14 =  floor(leftValue/10000000000000)         -(floor(leftValue/100000000000000)*10);
            set dig13 =  floor(leftValue/1000000000000)          -(floor(leftValue/10000000000000)*10);
            set dig12 =  floor(leftValue/100000000000)           -(floor(leftValue/1000000000000)*10);
            set dig11 =  floor(leftValue/10000000000)            -(floor(leftValue/100000000000)*10);
            set dig10 =  floor(leftValue/1000000000)             -(floor(leftValue/10000000000)*10);
            set dig9  =  floor(leftValue/100000000)              -(floor(leftValue/1000000000)*10);
            set dig8  =  floor(leftValue/10000000)               -(floor(leftValue/100000000)*10);
            set dig7  =  floor(leftValue/1000000)                -(floor(leftValue/10000000)*10);
            set dig6  =  floor(leftValue/100000)                 -(floor(leftValue/1000000)*10);
            set dig5  =  floor(leftValue/10000)                  -(floor(leftValue/100000)*10);
            set dig4  =  floor(leftValue/1000)                   -(floor(leftValue/10000)*10);
            set dig3  =  floor(leftValue/100)                    -(floor(leftValue/1000)*10);
            set dig2  =  floor(leftValue/10)                     -(floor(leftValue/100)*10);
            set dig1  =  floor(leftValue)                        -(floor(leftValue/10)*10);



If dig24 > 0 then
	 case
		    when dig24=1 then set ans=concat(ans, 'One Hundred ');
		    when dig24=2 then set ans=concat(ans, 'Two Hundred ');
		    when dig24=3 then set ans=concat(ans, 'Three Hundred ');
		    when dig24=4 then set ans=concat(ans, 'Four Hundred ');
		    when dig24=5 then set ans=concat(ans, 'Five Hundred ');
		    when dig24=6 then set ans=concat(ans, 'Six Hundred ');
		    when dig24=7 then set ans=concat(ans, 'Seven Hundred ');
		    when dig24=8 then set ans=concat(ans, 'Eight Hundred ');
		    when dig24=9 then set ans=concat(ans, 'Nine Hundred ');
		    else set ans = ans;
		end case;
		 if ans <> '' and dig23 =0 and dig22 = 0 then
		    set ans=concat(ans, ' Octillion');
		end if;
	    end if;
	    if ans <> '' and dig23 = 0 and dig22 >0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig23 = 1 then
		case
		    when (dig23*10 + dig22) = 10 then set ans=concat(ans,'Ten Octillion ');
		    when (dig23*10 + dig22) = 11 then set ans=concat(ans,'Eleven Octillion ');
		    when (dig23*10 + dig22) = 12 then set ans=concat(ans,'Twelve Octillion ');
		    when (dig23*10 + dig22) = 13 then set ans=concat(ans,'Thirteen Octillion ');
		    when (dig23*10 + dig22) = 14 then set ans=concat(ans,'Fourteen Octillion ');
		    when (dig23*10 + dig22) = 15 then set ans=concat(ans,'Fifteen Octillion ');
		    when (dig23*10 + dig22) = 16 then set ans=concat(ans,'Sixteen Octillion ');
		    when (dig23*10 + dig22) = 17 then set ans=concat(ans,'Seventeen Octillion ');
		    when (dig23*10 + dig22) = 18 then set ans=concat(ans,'Eighteen Octillion ');
		    when (dig23*10 + dig22) = 19 then set ans=concat(ans,'Nineteen Octillion ');
		    else set ans=ans;
		end case;
	    else
		if dig23 > 0 then
		    case
		        when dig23=2 then set ans=concat(ans, ' Twenty ');
		        when dig23=3 then set ans=concat(ans, ' Thirty ');
		        when dig23=4 then set ans=concat(ans, ' Fourty ');
		        when dig23=5 then set ans=concat(ans, ' Fifty ');
		        when dig23=6 then set ans=concat(ans, ' Sixty ');
		        when dig23=7 then set ans=concat(ans, ' Seventy ');
		        when dig23=8 then set ans=concat(ans, ' Eighty ');
		        when dig23=9 then set ans=concat(ans, ' Ninety ');
		        else set ans=ans;
		    end case;
		    if ans <> '' and dig22 =0 then
		    set ans=concat(ans, ' Octillion');
		    end if;
		end if;
		if ans <> '' and dig22 > 0 and dig23 =0 then
		set ans=concat(ans, ' ');
		end if;
		if dig22 > 0 then
		case
		    when dig22=1 then set ans=concat(ans, 'One Octillion ');
		    when dig22=2 then set ans=concat(ans, 'Two Octillion ');
		    when dig22=3 then set ans=concat(ans, 'Three Octillion ');
		    when dig22=4 then set ans=concat(ans, 'Four Octillion ');
		    when dig22=5 then set ans=concat(ans, 'Five Octillion ');
		    when dig22=6 then set ans=concat(ans, 'Six Octillion ');
		    when dig22=7 then set ans=concat(ans, 'Seven Octillion ');
		    when dig22=8 then set ans=concat(ans, 'Eight Octillion ');
		    when dig22=9 then set ans=concat(ans, 'Nine Octillion ');
		    else set ans = ans;
		end case;
	    end if;
	end if;
	    if ans <> '' and dig21 > 0 then
		set ans=concat(ans, ' ');
	    end if;



If dig21 > 0 then
	 case
		    when dig21=1 then set ans=concat(ans, 'One Hundred ');
		    when dig21=2 then set ans=concat(ans, 'Two Hundred ');
		    when dig21=3 then set ans=concat(ans, 'Three Hundred ');
		    when dig21=4 then set ans=concat(ans, 'Four Hundred ');
		    when dig21=5 then set ans=concat(ans, 'Five Hundred ');
		    when dig21=6 then set ans=concat(ans, 'Six Hundred ');
		    when dig21=7 then set ans=concat(ans, 'Seven Hundred ');
		    when dig21=8 then set ans=concat(ans, 'Eight Hundred ');
		    when dig21=9 then set ans=concat(ans, 'Nine Hundred ');
		    else set ans = ans;
		end case;
		 if ans <> '' and dig20 =0 and dig19 = 0 then
		    set ans=concat(ans, ' Decillion');
		end if;
	    end if;
	    if ans <> '' and dig20 = 0 and dig19 >0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig20 = 1 then
		case
		    when (dig20*10 + dig19) = 10 then set ans=concat(ans,'Ten Decillion ');
		    when (dig20*10 + dig19) = 11 then set ans=concat(ans,'Eleven Decillion ');
		    when (dig20*10 + dig19) = 12 then set ans=concat(ans,'Twelve Decillion ');
		    when (dig20*10 + dig19) = 13 then set ans=concat(ans,'Thirteen Decillion ');
		    when (dig20*10 + dig19) = 14 then set ans=concat(ans,'Fourteen Decillion ');
		    when (dig20*10 + dig19) = 15 then set ans=concat(ans,'Fifteen Decillion ');
		    when (dig20*10 + dig19) = 16 then set ans=concat(ans,'Sixteen Decillion ');
		    when (dig20*10 + dig19) = 17 then set ans=concat(ans,'Seventeen Decillion ');
		    when (dig20*10 + dig19) = 18 then set ans=concat(ans,'Eighteen Decillion ');
		    when (dig20*10 + dig19) = 19 then set ans=concat(ans,'Nineteen Decillion ');
		    else set ans=ans;
		end case;
	    else
		if dig20 > 0 then
		    case
		        when dig20=2 then set ans=concat(ans, ' Twenty ');
		        when dig20=3 then set ans=concat(ans, ' Thirty ');
		        when dig20=4 then set ans=concat(ans, ' Fourty ');
		        when dig20=5 then set ans=concat(ans, ' Fifty ');
		        when dig20=6 then set ans=concat(ans, ' Sixty ');
		        when dig20=7 then set ans=concat(ans, ' Seventy ');
		        when dig20=8 then set ans=concat(ans, ' Eighty ');
		        when dig20=9 then set ans=concat(ans, ' Ninety ');
		        else set ans=ans;
		    end case;
		    if ans <> '' and dig19 =0 then
		    set ans=concat(ans, ' Decillion');
		    end if;
		end if;
		if ans <> '' and dig19 > 0 and dig20 =0 then
		set ans=concat(ans, ' ');
		end if;
		if dig19 > 0 then
		case
		    when dig19=1 then set ans=concat(ans, 'One Decillion ');
		    when dig19=2 then set ans=concat(ans, 'Two Decillion ');
		    when dig19=3 then set ans=concat(ans, 'Three Decillion ');
		    when dig19=4 then set ans=concat(ans, 'Four Decillion ');
		    when dig19=5 then set ans=concat(ans, 'Five Decillion ');
		    when dig19=6 then set ans=concat(ans, 'Six Decillion ');
		    when dig19=7 then set ans=concat(ans, 'Seven Decillion ');
		    when dig19=8 then set ans=concat(ans, 'Eight Decillion ');
		    when dig19=9 then set ans=concat(ans, 'Nine Decillion ');
		    else set ans = ans;
		end case;
	    end if;
	end if;
	    if ans <> '' and dig18 > 0 then
		set ans=concat(ans, ' ');
	    end if;


If dig18 > 0 then
	 case
		    when dig18=1 then set ans=concat(ans, 'One Hundred ');
		    when dig18=2 then set ans=concat(ans, 'Two Hundred ');
		    when dig18=3 then set ans=concat(ans, 'Three Hundred ');
		    when dig18=4 then set ans=concat(ans, 'Four Hundred ');
		    when dig18=5 then set ans=concat(ans, 'Five Hundred ');
		    when dig18=6 then set ans=concat(ans, 'Six Hundred ');
		    when dig18=7 then set ans=concat(ans, 'Seven Hundred ');
		    when dig18=8 then set ans=concat(ans, 'Eight Hundred ');
		    when dig18=9 then set ans=concat(ans, 'Nine Hundred ');
		    else set ans = ans;
		end case;
		 if ans <> '' and dig17 =0 and dig16 = 0 then
		    set ans=concat(ans, ' Quintillion');
		end if;
	    end if;
	    if ans <> '' and dig17 = 0 and dig16 >0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig17 = 1 then
		case
		    when (dig17*10 + dig16) = 10 then set ans=concat(ans,'Ten Quintillion ');
		    when (dig17*10 + dig16) = 11 then set ans=concat(ans,'Eleven Quintillion ');
		    when (dig17*10 + dig16) = 12 then set ans=concat(ans,'Twelve Quintillion ');
		    when (dig17*10 + dig16) = 13 then set ans=concat(ans,'Thirteen Quintillion ');
		    when (dig17*10 + dig16) = 14 then set ans=concat(ans,'Fourteen Quintillion ');
		    when (dig17*10 + dig16) = 15 then set ans=concat(ans,'Fifteen Quintillion ');
		    when (dig17*10 + dig16) = 16 then set ans=concat(ans,'Sixteen Quintillion ');
		    when (dig17*10 + dig16) = 17 then set ans=concat(ans,'Seventeen Quintillion ');
		    when (dig17*10 + dig16) = 18 then set ans=concat(ans,'Eighteen Quintillion ');
		    when (dig17*10 + dig16) = 19 then set ans=concat(ans,'Nineteen Quintillion ');
		    else set ans=ans;
		end case;
	    else
		if dig17 > 0 then
		    case
		        when dig17=2 then set ans=concat(ans, ' Twenty ');
		        when dig17=3 then set ans=concat(ans, ' Thirty ');
		        when dig17=4 then set ans=concat(ans, ' Fourty ');
		        when dig17=5 then set ans=concat(ans, ' Fifty ');
		        when dig17=6 then set ans=concat(ans, ' Sixty ');
		        when dig17=7 then set ans=concat(ans, ' Seventy ');
		        when dig17=8 then set ans=concat(ans, ' Eighty ');
		        when dig17=9 then set ans=concat(ans, ' Ninety ');
		        else set ans=ans;
		    end case;
		    if ans <> '' and dig16 =0 then
		    set ans=concat(ans, ' Quintillion');
		    end if;
		end if;
		if ans <> '' and dig16 > 0 and dig17 =0 then
		set ans=concat(ans, ' ');
		end if;
		if dig16 > 0 then
		case
		    when dig16=1 then set ans=concat(ans, 'One Quintillion ');
		    when dig16=2 then set ans=concat(ans, 'Two Quintillion ');
		    when dig16=3 then set ans=concat(ans, 'Three Quintillion ');
		    when dig16=4 then set ans=concat(ans, 'Four Quintillion ');
		    when dig16=5 then set ans=concat(ans, 'Five Quintillion ');
		    when dig16=6 then set ans=concat(ans, 'Six Quintillion ');
		    when dig16=7 then set ans=concat(ans, 'Seven Quintillion ');
		    when dig16=8 then set ans=concat(ans, 'Eight Quintillion ');
		    when dig16=9 then set ans=concat(ans, 'Nine Quintillion ');
		    else set ans = ans;
		end case;
	    end if;
	end if;
	    if ans <> '' and dig15 > 0 then
		set ans=concat(ans, ' ');
	    end if;
	    

If dig15 > 0 then
	 case
		    when dig15=1 then set ans=concat(ans, 'One Hundred ');
		    when dig15=2 then set ans=concat(ans, 'Two Hundred ');
		    when dig15=3 then set ans=concat(ans, 'Three Hundred ');
		    when dig15=4 then set ans=concat(ans, 'Four Hundred ');
		    when dig15=5 then set ans=concat(ans, 'Five Hundred ');
		    when dig15=6 then set ans=concat(ans, 'Six Hundred ');
		    when dig15=7 then set ans=concat(ans, 'Seven Hundred ');
		    when dig15=8 then set ans=concat(ans, 'Eight Hundred ');
		    when dig15=9 then set ans=concat(ans, 'Nine Hundred ');
		    else set ans = ans;
		end case;
		 if ans <> '' and dig14 =0 and dig13 = 0 then
		    set ans=concat(ans, ' Trillion');
		end if;
	    end if;
	    if ans <> '' and dig14 = 0 and dig13 >0 then
		set ans=concat(ans, '');
	    end if;
	    if dig14 = 1 then
		case
		    when (dig14*10 + dig13) = 10 then set ans=concat(ans,'Ten Trillion ');
		    when (dig14*10 + dig13) = 11 then set ans=concat(ans,'Eleven Trillion ');
		    when (dig14*10 + dig13) = 12 then set ans=concat(ans,'Twelve Trillion ');
		    when (dig14*10 + dig13) = 13 then set ans=concat(ans,'Thirteen Trillion ');
		    when (dig14*10 + dig13) = 14 then set ans=concat(ans,'Fourteen Trillion ');
		    when (dig14*10 + dig13) = 15 then set ans=concat(ans,'Fifteen Trillion ');
		    when (dig14*10 + dig13) = 16 then set ans=concat(ans,'Sixteen Trillion ');
		    when (dig14*10 + dig13) = 17 then set ans=concat(ans,'Seventeen Trillion ');
		    when (dig14*10 + dig13) = 18 then set ans=concat(ans,'Eighteen Trillion ');
		    when (dig14*10 + dig13) = 19 then set ans=concat(ans,'Nineteen Trillion ');
		    else set ans=ans;
		end case;
	    else
		if dig14 > 0 then
		    case
		        when dig14=2 then set ans=concat(ans, ' Twenty ');
		        when dig14=3 then set ans=concat(ans, ' Thirty ');
		        when dig14=4 then set ans=concat(ans, ' Fourty ');
		        when dig14=5 then set ans=concat(ans, ' Fifty ');
		        when dig14=6 then set ans=concat(ans, ' Sixty ');
		        when dig14=7 then set ans=concat(ans, ' Seventy ');
		        when dig14=8 then set ans=concat(ans, ' Eighty ');
		        when dig14=9 then set ans=concat(ans, ' Ninety ');
		        else set ans=ans;
		    end case;
		    if ans <> '' and dig13 =0 then
		    set ans=concat(ans, ' Trillion');
		    end if;
		end if;
		if ans <> '' and dig13 > 0 and dig14 =0 then
		set ans=concat(ans, '');
		end if;
		if dig13 > 0 then
		case
		    when dig13=1 then set ans=concat(ans, 'One Trillion ');
		    when dig13=2 then set ans=concat(ans, 'Two Trillion ');
		    when dig13=3 then set ans=concat(ans, 'Three Trillion ');
		    when dig13=4 then set ans=concat(ans, 'Four Trillion ');
		    when dig13=5 then set ans=concat(ans, 'Five Trillion ');
		    when dig13=6 then set ans=concat(ans, 'Six Trillion ');
		    when dig13=7 then set ans=concat(ans, 'Seven Trillion ');
		    when dig13=8 then set ans=concat(ans, 'Eight Trillion ');
		    when dig13=9 then set ans=concat(ans, 'Nine Trillion ');
		    else set ans = ans;
		end case;
	    end if;
	end if;
	    if ans <> '' and dig12 > 0 then
		set ans=concat(ans, '');
	    end if;
	    

	If dig12 > 0 then
	 case
		    when dig12=1 then set ans=concat(ans, 'One Hundred ');
		    when dig12=2 then set ans=concat(ans, 'Two Hundred ');
		    when dig12=3 then set ans=concat(ans, 'Three Hundred ');
		    when dig12=4 then set ans=concat(ans, 'Four Hundred ');
		    when dig12=5 then set ans=concat(ans, 'Five Hundred ');
		    when dig12=6 then set ans=concat(ans, 'Six Hundred ');
		    when dig12=7 then set ans=concat(ans, 'Seven Hundred ');
		    when dig12=8 then set ans=concat(ans, 'Eight Hundred ');
		    when dig12=9 then set ans=concat(ans, 'Nine Hundred ');
		    else set ans = ans;
		end case;
		 if ans <> '' and dig11 =0 and dig10 = 0 then
		    set ans=concat(ans, ' Billion');
		end if;
	    end if;
	    if ans <> '' and dig11 = 0 and dig10 >0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig11 = 1 then
		case
		    when (dig11*10 + dig10) = 10 then set ans=concat(ans,'Ten Billion ');
		    when (dig11*10 + dig10) = 11 then set ans=concat(ans,'Eleven Billion ');
		    when (dig11*10 + dig10) = 12 then set ans=concat(ans,'Twelve Billion ');
		    when (dig11*10 + dig10) = 13 then set ans=concat(ans,'Thirteen Billion ');
		    when (dig11*10 + dig10) = 14 then set ans=concat(ans,'Fourteen Billion ');
		    when (dig11*10 + dig10) = 15 then set ans=concat(ans,'Fifteen Billion ');
		    when (dig11*10 + dig10) = 16 then set ans=concat(ans,'Sixteen Billion ');
		    when (dig11*10 + dig10) = 17 then set ans=concat(ans,'Seventeen Billion ');
		    when (dig11*10 + dig10) = 18 then set ans=concat(ans,'Eighteen Billion ');
		    when (dig11*10 + dig10) = 19 then set ans=concat(ans,'Nineteen Billion ');
		    else set ans=ans;
		end case;
	    else
		if dig11 > 0 then
		    case
		        when dig11=2 then set ans=concat(ans, ' Twenty ');
		        when dig11=3 then set ans=concat(ans, ' Thirty ');
		        when dig11=4 then set ans=concat(ans, ' Fourty ');
		        when dig11=5 then set ans=concat(ans, ' Fifty ');
		        when dig11=6 then set ans=concat(ans, ' Sixty ');
		        when dig11=7 then set ans=concat(ans, ' Seventy ');
		        when dig11=8 then set ans=concat(ans, ' Eighty ');
		        when dig11=9 then set ans=concat(ans, ' Ninety ');
		        else set ans=ans;
		    end case;
		    if ans <> '' and dig10 =0 then
		    set ans=concat(ans, ' Billion');
		    end if;
		end if;
		if ans <> '' and dig10 > 0 and dig11 =0 then
		set ans=concat(ans, ' ');
		end if;
		if dig10 > 0 then
		case
		    when dig10=1 then set ans=concat(ans, 'One Billion ');
		    when dig10=2 then set ans=concat(ans, 'Two Billion ');
		    when dig10=3 then set ans=concat(ans, 'Three Billion ');
		    when dig10=4 then set ans=concat(ans, 'Four Billion ');
		    when dig10=5 then set ans=concat(ans, 'Five Billion ');
		    when dig10=6 then set ans=concat(ans, 'Six Billion ');
		    when dig10=7 then set ans=concat(ans, 'Seven Billion ');
		    when dig10=8 then set ans=concat(ans, 'Eight Billion ');
		    when dig10=9 then set ans=concat(ans, 'Nine Billion ');
		    else set ans = ans;
		end case;
	    end if;
	end if;
	
	
	    if ans <> '' and dig9 > 0 then
		set ans=concat(ans, ' ');
	    end if;
	    
	    If dig9 > 0 then
	 case
		    when dig9=1 then set ans=concat(ans, 'One Hundred ');
		    when dig9=2 then set ans=concat(ans, 'Two Hundred ');
		    when dig9=3 then set ans=concat(ans, 'Three Hundred ');
		    when dig9=4 then set ans=concat(ans, 'Four Hundred ');
		    when dig9=5 then set ans=concat(ans, 'Five Hundred ');
		    when dig9=6 then set ans=concat(ans, 'Six Hundred ');
		    when dig9=7 then set ans=concat(ans, 'Seven Hundred ');
		    when dig9=8 then set ans=concat(ans, 'Eight Hundred ');
		    when dig9=9 then set ans=concat(ans, 'Nine Hundred ');
		    else set ans = ans;
		end case;
		 if ans <> '' and dig8 =0 and dig7 = 0 then
		    set ans=concat(ans, ' Million');
		end if;
	    end if;
	    if ans <> '' and dig8 = 0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig8 = 1 then
		case
		    when (dig8*10 + dig7) = 10 then set ans=concat(ans,'Ten Million ');
		    when (dig8*10 + dig7) = 11 then set ans=concat(ans,'Eleven Million ');
		    when (dig8*10 + dig7) = 12 then set ans=concat(ans,'Twelve Million ');
		    when (dig8*10 + dig7) = 13 then set ans=concat(ans,'Thirteen Million ');
		    when (dig8*10 + dig7) = 14 then set ans=concat(ans,'Fourteen Million ');
		    when (dig8*10 + dig7) = 15 then set ans=concat(ans,'Fifteen Million ');
		    when (dig8*10 + dig7) = 16 then set ans=concat(ans,'Sixteen Million ');
		    when (dig8*10 + dig7) = 17 then set ans=concat(ans,'Seventeen Million ');
		    when (dig8*10 + dig7) = 18 then set ans=concat(ans,'Eighteen Million ');
		    when (dig8*10 + dig7) = 19 then set ans=concat(ans,'Nineteen Million ');
		    else set ans=ans;
		end case;
	    else
		if dig8 > 0 then
		    case
		        when dig8=2 then set ans=concat(ans, ' Twenty ');
		        when dig8=3 then set ans=concat(ans, ' Thirty ');
		        when dig8=4 then set ans=concat(ans, ' Fourty ');
		        when dig8=5 then set ans=concat(ans, ' Fifty ');
		        when dig8=6 then set ans=concat(ans, ' Sixty ');
		        when dig8=7 then set ans=concat(ans, ' Seventy ');
		        when dig8=8 then set ans=concat(ans, ' Eighty ');
		        when dig8=9 then set ans=concat(ans, ' Ninety ');
		        else set ans=ans;
		    end case;
		    if ans <> '' and dig7 =0 then
		    set ans=concat(ans, ' Million');
		    end if;
		end if;
		if ans <> '' and dig7 > 0 and dig8 =0 then
		set ans=concat(ans, ' ');
		end if;
		if dig7 > 0 then
		case
		    when dig7=1 then set ans=concat(ans, 'One Million');
		    when dig7=2 then set ans=concat(ans, 'Two Million');
		    when dig7=3 then set ans=concat(ans, 'Three Million');
		    when dig7=4 then set ans=concat(ans, 'Four Million');
		    when dig7=5 then set ans=concat(ans, 'Five Million');
		    when dig7=6 then set ans=concat(ans, 'Six Million');
		    when dig7=7 then set ans=concat(ans, 'Seven Million');
		    when dig7=8 then set ans=concat(ans, 'Eight Million');
		    when dig7=9 then set ans=concat(ans, 'Nine Million');
		    else set ans = ans;
		end case;
	    end if;
	end if;
	    if ans <> '' and dig6 > 0 then
		set ans=concat(ans, ' ');
	    end if;


	    if dig6 > 0 then
		case
		    when dig6=1 then set ans=concat(ans, 'One Hundred');
		    when dig6=2 then set ans=concat(ans, 'Two Hundred');
		    when dig6=3 then set ans=concat(ans, 'Three Hundred');
		    when dig6=4 then set ans=concat(ans, 'Four Hundred');
		    when dig6=5 then set ans=concat(ans, 'Five Hundred');
		    when dig6=6 then set ans=concat(ans, 'Six Hundred');
		    when dig6=7 then set ans=concat(ans, 'Seven Hundred');
		    when dig6=8 then set ans=concat(ans, 'Eight Hundred');
		    when dig6=9 then set ans=concat(ans, 'Nine Hundred');
		    else set ans = ans;
		end case;
		 if ans <> '' and dig4 =0 and dig5 = 0 then
		    set ans=concat(ans, ' Thousand');
		end if;
	    end if;
	    if ans <> '' and dig5 = 0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig5 = 1 then
		case
		    when (dig5*10 + dig4) = 10 then set ans=concat(ans,'Ten Thousand');
		    when (dig5*10 + dig4) = 11 then set ans=concat(ans,'Eleven Thousand');
		    when (dig5*10 + dig4) = 12 then set ans=concat(ans,'Twelve Thousand');
		    when (dig5*10 + dig4) = 13 then set ans=concat(ans,'Thirteen Thousand');
		    when (dig5*10 + dig4) = 14 then set ans=concat(ans,'Fourteen Thousand');
		    when (dig5*10 + dig4) = 15 then set ans=concat(ans,'Fifteen Thousand');
		    when (dig5*10 + dig4) = 16 then set ans=concat(ans,'Sixteen Thousand');
		    when (dig5*10 + dig4) = 17 then set ans=concat(ans,'Seventeen Thousand');
		    when (dig5*10 + dig4) = 18 then set ans=concat(ans,'Eighteen Thousand');
		    when (dig5*10 + dig4) = 19 then set ans=concat(ans,'Nineteen Thousand');
		    else set ans=ans;
		end case;
	    else
		if dig5 > 0 then
		    case
		        when dig5=2 then set ans=concat(ans, ' Twenty ');
		        when dig5=3 then set ans=concat(ans, ' Thirty ');
		        when dig5=4 then set ans=concat(ans, ' Fourty ');
		        when dig5=5 then set ans=concat(ans, ' Fifty ');
		        when dig5=6 then set ans=concat(ans, ' Sixty ');
		        when dig5=7 then set ans=concat(ans, ' Seventy ');
		        when dig5=8 then set ans=concat(ans, ' Eighty ');
		        when dig5=9 then set ans=concat(ans, ' Ninety ');
		        else set ans=ans;
		    end case;
		    if ans <> '' and dig4 =0 then
		    set ans=concat(ans, ' Thousand');
		    end if;
		end if;
		if ans <> '' and dig4 > 0 and dig5 =0 then
		set ans=concat(ans, ' ');
		end if;
		if dig4 > 0 then
		case
		    when dig4=1 then set ans=concat(ans, 'One Thousand');
		    when dig4=2 then set ans=concat(ans, 'Two Thousand');
		    when dig4=3 then set ans=concat(ans, 'Three Thousand');
		    when dig4=4 then set ans=concat(ans, 'Four Thousand');
		    when dig4=5 then set ans=concat(ans, 'Five Thousand');
		    when dig4=6 then set ans=concat(ans, 'Six Thousand');
		    when dig4=7 then set ans=concat(ans, 'Seven Thousand');
		    when dig4=8 then set ans=concat(ans, 'Eight Thousand');
		    when dig4=9 then set ans=concat(ans, 'Nine Thousand');
		    else set ans = ans;
		end case;
	    end if;
	end if;
	    if ans <> '' and dig3 > 0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig3 > 0 then
		case
		    when dig3=1 then set ans=concat(ans, 'One Hundred');
		    when dig3=2 then set ans=concat(ans, 'Two Hundred');
		    when dig3=3 then set ans=concat(ans, 'Three Hundred');
		    when dig3=4 then set ans=concat(ans, 'Four Hundred');
		    when dig3=5 then set ans=concat(ans, 'Five Hundred');
		    when dig3=6 then set ans=concat(ans, 'Six Hundred');
		    when dig3=7 then set ans=concat(ans, 'Seven Hundred');
		    when dig3=8 then set ans=concat(ans, 'Eight Hundred');
		    when dig3=9 then set ans=concat(ans, 'Nine Hundred');
		    else set ans = ans;
		end case;
	    end if;
	    if ans <> '' and dig2 = 0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig2 = 1 then
		case
		    when (dig2*10 + dig1) = 10 then set ans=concat(ans,'Ten');
		    when (dig2*10 + dig1) = 11 then set ans=concat(ans,'Eleven');
		    when (dig2*10 + dig1) = 12 then set ans=concat(ans,'Twelve');
		    when (dig2*10 + dig1) = 13 then set ans=concat(ans,'Thirteen');
		    when (dig2*10 + dig1) = 14 then set ans=concat(ans,'Fourteen');
		    when (dig2*10 + dig1) = 15 then set ans=concat(ans,'Fifteen');
		    when (dig2*10 + dig1) = 16 then set ans=concat(ans,'Sixteen');
		    when (dig2*10 + dig1) = 17 then set ans=concat(ans,'Seventeen');
		    when (dig2*10 + dig1) = 18 then set ans=concat(ans,'Eighteen');
		    when (dig2*10 + dig1) = 19 then set ans=concat(ans,'Nineteen');
		    else set ans=ans;
		end case;
	    else
		if dig2 > 0 then
		    case
		        when dig2=2 then set ans=concat(ans, ' Twenty ');
		        when dig2=3 then set ans=concat(ans, ' Thirty ');
		        when dig2=4 then set ans=concat(ans, ' Fourty ');
		        when dig2=5 then set ans=concat(ans, ' Fifty ');
		        when dig2=6 then set ans=concat(ans, ' Sixty ');
		        when dig2=7 then set ans=concat(ans, ' Seventy ');
		        when dig2=8 then set ans=concat(ans, ' Eighty ');
		        when dig2=9 then set ans=concat(ans, ' Ninety ');
		        else set ans=ans;
		    end case;
		end if;
		if ans <> '' and dig1 > 0 and dig2 =0 then
		set ans=concat(ans, ' ');
		end if;
		if dig1 > 0 then
		    case
		        when dig1=1 then set ans=concat(ans, 'One');
		        when dig1=2 then set ans=concat(ans, 'Two');
		        when dig1=3 then set ans=concat(ans, 'Three');
		        when dig1=4 then set ans=concat(ans, 'Four');
		        when dig1=5 then set ans=concat(ans, 'Five');
		        when dig1=6 then set ans=concat(ans, 'Six');
		        when dig1=7 then set ans=concat(ans, 'Seven');
		        when dig1=8 then set ans=concat(ans, 'Eight');
		        when dig1=9 then set ans=concat(ans, 'Nine');
		        else set ans=ans;
		    end case;
		end if;
	    end if;
	

	set ans=concat(ans, ' dollars');


	select trim(ans) into ans;



	select concat(upper(left(ans,1)),lower(substring(ans,2))) into ans;


	return trim(ans);

	    END$$



*/

/**********************************************************************************************************************
 * The SQL Function for converting the right values to word..works up to 2 digits,
 * It rounds off every other number after decimal point to only 2 digits then converts the 2 digits to word
 **********************************************************************************************************************/

/*

	DELIMITER $$
	DROP FUNCTION IF EXISTS to_string_right$$
	CREATE FUNCTION `to_string_right`(n VARCHAR(200)) RETURNS varchar(1000)



	 BEGIN
	    declare ansright varchar(500);
	    declare rightValue FLOAT; 
	    declare dig1right,dig2right,dig3right int;

	set ansright = '';

	select substring(format( n %1,2),3,2) INTO rightValue;


	    set dig3right = floor(rightValue/100);
	    set dig2right = floor(rightValue/10) - (floor(rightValue/100)*10);
	    set dig1right = rightValue - (floor(rightValue / 10)*10);

	
	if dig3right > 0 then
		case
		    when dig3right=1 then set ansright=concat(ansright, 'one hundred ');
		    when dig3right=2 then set ansright=concat(ansright, 'two hundred ');
		    when dig3right=3 then set ansright=concat(ansright, 'three hundred ');
		    when dig3right=4 then set ansright=concat(ansright, 'four hundred ');
		    when dig3right=5 then set ansright=concat(ansright, 'five hundred ');
		    when dig3right=6 then set ansright=concat(ansright, 'six hundred ');
		    when dig3right=7 then set ansright=concat(ansright, 'seven hundred ');
		    when dig3right=8 then set ansright=concat(ansright, 'eight hundred ');
		    when dig3right=9 then set ansright=concat(ansright, 'nine hundred ');
		    else set ansright = ansright;
		end case;
	    end if;

	    if dig2right = 1 then
		case
		    when (dig2right*10 + dig1right) = 10 then set ansright=concat(ansright,'ten');
		    when (dig2right*10 + dig1right) = 11 then set ansright=concat(ansright,'eleven');
		    when (dig2right*10 + dig1right) = 12 then set ansright=concat(ansright,'twelve');
		    when (dig2right*10 + dig1right) = 13 then set ansright=concat(ansright,'thirteen');
		    when (dig2right*10 + dig1right) = 14 then set ansright=concat(ansright,'fourteen');
		    when (dig2right*10 + dig1right) = 15 then set ansright=concat(ansright,'fifteen');
		    when (dig2right*10 + dig1right) = 16 then set ansright=concat(ansright,'sixteen');
		    when (dig2right*10 + dig1right) = 17 then set ansright=concat(ansright,'seventeen');
		    when (dig2right*10 + dig1right) = 18 then set ansright=concat(ansright,'eighteen');
		    when (dig2right*10 + dig1right) = 19 then set ansright=concat(ansright,'nineteen');
		    else set ansright=ansright;
		end case;
	    else
		if dig2right > 0 then
		    case

		        when dig2right=2 then set ansright=concat(ansright, 'twenty ');
		        when dig2right=3 then set ansright=concat(ansright, 'thirty ');
		        when dig2right=4 then set ansright=concat(ansright, 'fourty ');
		        when dig2right=5 then set ansright=concat(ansright, 'fifty ');
		        when dig2right=6 then set ansright=concat(ansright, 'sixty ');
		        when dig2right=7 then set ansright=concat(ansright, 'seventy ');
		        when dig2right=8 then set ansright=concat(ansright, 'eighty ');
		        when dig2right=9 then set ansright=concat(ansright, 'ninety ');
		        
		        
		        
		        else set ansright=ansright;
		    end case;
		end if;
		if dig1right > 0 then
		    case
		        when dig1right=1 then set ansright=concat(ansright, 'one');
		        when dig1right=2 then set ansright=concat(ansright, 'two');
		        when dig1right=3 then set ansright=concat(ansright, 'three');
		        when dig1right=4 then set ansright=concat(ansright, 'four');
		        when dig1right=5 then set ansright=concat(ansright, 'five');
		        when dig1right=6 then set ansright=concat(ansright, 'six');
		        when dig1right=7 then set ansright=concat(ansright, 'seven');
		        when dig1right=8 then set ansright=concat(ansright, 'eight');
		        when dig1right=9 then set ansright=concat(ansright, 'nine');
		        else set ansright=ansright;
		    end case;
		end if;
	    end if;


	

	set ansright=concat(ansright, '');



	select trim(ansright) into ansright;



	select lower(ansright) into ansright;


	return trim(ansright);

	    END$$


*/



/**********************************************************************************************************************
 * The SQL Function for combining the right and left conversion..
 * This is the test function. It has both the left and right digits converted values.
 * How to test is as above
 ***********************************************************************************************************************/

/*

	DELIMITER $$
	DROP FUNCTION IF EXISTS final_string$$
	CREATE FUNCTION `final_string`(n varchar(1000)) RETURNS VARCHAR(5000)
        BEGIN

	declare valueLeft varchar(200);
        declare valueRight varchar(200);
        declare out_value varchar(200);

	set valueLeft='';
	set valueRight='';
	set out_value='';

	select to_string_left(n) into valueLeft;
	select to_string_right(n) into valueRight;

	set out_value = valueLeft;

	case
	 
	when valueRight is NULL or valueRight='' then

	set out_value=concat(out_value, '.');
	 
	else

	set out_value=concat(out_value, ' and ');
	set out_value=concat(out_value, valueRight);
	set out_value=concat(out_value, ' cents.');

	end case;


	select trim(out_value) into out_value;



	select concat(upper(left(out_value,1)),lower(substring(out_value,2))) into out_value;

	return trim(out_value);
	END$$




*/