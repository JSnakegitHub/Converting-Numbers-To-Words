import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Num2Word {

	private static Connection conn;
	private static ResultSet result;

	public static void main(String[] args) {

		Num2Word num2Word = new Num2Word();
		num2Word.getNumbersBeforeDecimalPointAndTranslateThem();

	}

	private int valueAfterDP;
	private int answerRight;
	private String valueAfterDecimalInWord;
	ArrayList<String> allTheIDs = new ArrayList<>();
	private String convertedToWord;

	public void getNumbersBeforeDecimalPointAndTranslateThem() {

		/***************************************************************************************************************
		 * Because I need to return the conversion to word for every id that will be found in
		 * the database table, I will grab all the ids and pass them in an array and use
		 * advanced for-loop to convert for each id
		 ***************************************************************************************************************/
		String sqlForGettingID = "SELECT id from test";

		try {

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false",
					"root", "mysql");
			PreparedStatement pst = conn.prepareStatement(sqlForGettingID);
			result = pst.executeQuery();

			while (result.next()) {
				String id = result.getString(1);

				allTheIDs.add(id);

			}

			System.out.println("Available Ids are: " + allTheIDs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn == null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		/**************************************************
		 * Now The for loop, so that I convert for each id
		 **************************************************/
		for (String ids : allTheIDs) {

			/********************************************************************************
			 * My Function is called N2W_Converter1() as shown below in the database Function
			 * Am rounding the values to 2 decimal places because cents are only from 0-99
			 ********************************************************************************/
			String sql = "SELECT id,ROUND(value,2),(select N2W_Converter1((SELECT ROUND(value,2) from test where id=" + ids
					+ "))) As Inword from test where id=" + ids + "";

			try {

				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false",
						"root", "mysql");
				PreparedStatement pst = conn.prepareStatement(sql);
				result = pst.executeQuery();

				/**************************************************
				 * Am using if, coz am targeting one id at a time
				 **************************************************/
				if (result.next()) {

					int id = result.getInt(1);

					String theValueItself = result.getString(2);//Calling the value itself

					String valueBeforeDecimalInWord = result.getString(3);

					/********************************************************
					 * The procedure prcoN2W_2() which is down this page,
					 * is going to grab the digits behind decimal point; 
					 * for a specific id
					 ********************************************************/
					
					answerRight = getNumbersAfterDecimalPointAndReturnTheNumbers(
							"call procN2W_2((" + theValueItself + "))");//Here, if we had not called the value, we would need to call it using the ids

					/*******************************************
					 * If there is no cent...for example $125.0
					 *******************************************/
					if (getDigitAfterDecimalPointAndTranslateThem().equals(" ")
							|| getDigitAfterDecimalPointAndTranslateThem() == null
							|| getDigitAfterDecimalPointAndTranslateThem().isEmpty()) {
						
						 convertedToWord = valueBeforeDecimalInWord + " dollars";
					}else {
						
						/********************************************************
						 * If there is cent...for example $125.05   ...5 cents
						 ********************************************************/
						 convertedToWord = valueBeforeDecimalInWord + " dollars and "
								+ getDigitAfterDecimalPointAndTranslateThem() + " cents";
					}

					

					System.out.println(id + ", " + theValueItself + ", " + convertedToWord);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn == null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	public String getDigitAfterDecimalPointAndTranslateThem() {

		/**************************************************************************
		 * We are converting the digits that were extracted after the decimal point
		 **************************************************************************/
		String sql = "select N2W_Converter1(" + answerRight + ")";

		try {

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false",
					"root", "mysql");
			PreparedStatement pst = conn.prepareStatement(sql);
			result = pst.executeQuery();

			while (result.next()) {
				valueAfterDecimalInWord = result.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn == null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return valueAfterDecimalInWord;

	}

	public int getNumbersAfterDecimalPointAndReturnTheNumbers(String sql) {
		
		/*********************************************************************
		 * Here we want the digits after the decimal point, in form of integer
		 *********************************************************************/
		
		try {

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false",
					"root", "mysql");
			PreparedStatement pst = conn.prepareCall(sql);
			result = pst.executeQuery();

			while (result.next()) {
				valueAfterDP = result.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn == null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return valueAfterDP;
	}

}

/***************************************************************************************************************************************
 * Database name is test, table name is test with columns id int auto_increment, value decimal(12,2)...put some test values in the table
 ***************************************************************************************************************************************/


/******************************************************************************
 * The SQL Function for converting to word....works only up to 6 digits value, 
 * for example $999,999.34, To test, just paste the below function in your 
 * database...then type select to_string(34678.23)$$
 * Integers work up to 8 digits, floats work up to 6 digits 
 * Decimal values work accurately for 5 digits,begins to deviate from around 250,000$
 ******************************************************************************/
/*
	DELIMITER $$
	CREATE FUNCTION `to_string`(n FLOAT) RETURNS varchar(1000)



	 BEGIN
	    declare ans varchar(200);
	    declare ansright varchar(200);
	    declare finalanswer varchar(200);
	    declare leftValue int;
	    declare rightValue int; 
	    declare dig1, dig2, dig3,dig4,dig5,dig6,dig7,dig8,dig9,dig1right,dig2right,dig3right int;

	set ans = '';
	set ansright = '';
	set finalanswer = '';

	    



	select substring(format( n %1,2),3,2) INTO rightValue;
	select TRUNCATE(n,0) INTO leftValue;

	    set dig9 = floor(leftValue/100000000);
	    set dig8 = floor(leftValue/10000000) - dig9*10;
	    set dig7 = floor(leftValue/1000000) -(floor(leftValue/10000000)*10);
	    set dig6 = floor(leftValue/100000) - (floor(leftValue/1000000)*10);
	    set dig5 = floor(leftValue/10000) -  (floor(leftValue/100000)*10);
	    set dig4 = floor(leftValue/1000) -   (floor(leftValue/10000)*10);
	    set dig3 = floor(leftValue/100) -    (floor(leftValue/1000)*10);
	    set dig2 = floor(leftValue/10) -     (floor(leftValue/100)*10);
	    set dig1 = leftValue - (floor(leftValue / 10)*10);



	    set dig3right = floor(rightValue/100) - (floor(rightValue/1000)*10);
	    set dig2right = floor(rightValue/10) - (floor(rightValue/100)*10);
	    set dig1right = rightValue - (floor(rightValue / 10)*10);


	If dig9 > 0 then
	 case
		    when dig9=1 then set ans=concat(ans, 'One Hundred');
		    when dig9=2 then set ans=concat(ans, 'Two Hundred');
		    when dig9=3 then set ans=concat(ans, 'Three Hundred');
		    when dig9=4 then set ans=concat(ans, 'Four Hundred');
		    when dig9=5 then set ans=concat(ans, 'Five Hundred');
		    when dig9=6 then set ans=concat(ans, 'Six Hundred');
		    when dig9=7 then set ans=concat(ans, 'Seven Hundred');
		    when dig9=8 then set ans=concat(ans, 'Eight Hundred');
		    when dig9=9 then set ans=concat(ans, 'Nine Hundred');
		    else set ans = ans;
		end case;
		 if ans <> '' and dig8 =0 and dig7 = 0 then
		    set ans=concat(ans, ' Million');
		end if;
	    end if;
	    if ans <> '' and dig8 > 0 then
		set ans=concat(ans, ' ');
	    end if;
	    if dig8 = 1 then
		case
		    when (dig8*10 + dig7) = 10 then set ans=concat(ans,'Ten Million');
		    when (dig8*10 + dig7) = 11 then set ans=concat(ans,'Eleven Million');
		    when (dig8*10 + dig7) = 12 then set ans=concat(ans,'Twelve Million');
		    when (dig8*10 + dig7) = 13 then set ans=concat(ans,'Thirteen Million');
		    when (dig8*10 + dig7) = 14 then set ans=concat(ans,'Fourteen Million');
		    when (dig8*10 + dig7) = 15 then set ans=concat(ans,'Fifteen Million');
		    when (dig8*10 + dig7) = 16 then set ans=concat(ans,'Sixteen Million');
		    when (dig8*10 + dig7) = 17 then set ans=concat(ans,'Seventeen Million');
		    when (dig8*10 + dig7) = 18 then set ans=concat(ans,'Eighteen Million');
		    when (dig8*10 + dig7) = 19 then set ans=concat(ans,'Nineteen Million');
		    else set ans=ans;
		end case;
	    else
		if dig8 > 0 then
		    case
		        when dig8=2 then set ans=concat(ans, 'Twenty ');
		        when dig8=3 then set ans=concat(ans, 'Thirty ');
		        when dig8=4 then set ans=concat(ans, 'Fourty ');
		        when dig8=5 then set ans=concat(ans, 'Fifty ');
		        when dig8=6 then set ans=concat(ans, 'Sixty ');
		        when dig8=7 then set ans=concat(ans, 'Seventy ');
		        when dig8=8 then set ans=concat(ans, 'Eighty ');
		        when dig8=9 then set ans=concat(ans, 'Ninety ');
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
	    if ans <> '' and dig5 > 0 then
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
		        when dig5=2 then set ans=concat(ans, 'Twenty ');
		        when dig5=3 then set ans=concat(ans, 'Thirty ');
		        when dig5=4 then set ans=concat(ans, 'Fourty ');
		        when dig5=5 then set ans=concat(ans, 'Fifty ');
		        when dig5=6 then set ans=concat(ans, 'Sixty ');
		        when dig5=7 then set ans=concat(ans, 'Seventy ');
		        when dig5=8 then set ans=concat(ans, 'Eighty ');
		        when dig5=9 then set ans=concat(ans, 'Ninety ');
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
	    if ans <> '' and dig2 > 0 then
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
		        when dig2=2 then set ans=concat(ans, 'Twenty ');
		        when dig2=3 then set ans=concat(ans, 'Thirty ');
		        when dig2=4 then set ans=concat(ans, 'Fourty ');
		        when dig2=5 then set ans=concat(ans, 'Fifty ');
		        when dig2=6 then set ans=concat(ans, 'Sixty ');
		        when dig2=7 then set ans=concat(ans, 'Seventy ');
		        when dig2=8 then set ans=concat(ans, 'Eighty ');
		        when dig2=9 then set ans=concat(ans, 'Ninety ');
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



	if dig3right > 0 then
		case
		    when dig3right=1 then set ansright=concat(ansright, 'one hundred');
		    when dig3right=2 then set ansright=concat(ansright, 'two hundred');
		    when dig3right=3 then set ansright=concat(ansright, 'three hundred');
		    when dig3right=4 then set ansright=concat(ansright, 'four hundred');
		    when dig3right=5 then set ansright=concat(ansright, 'five hundred');
		    when dig3right=6 then set ansright=concat(ansright, 'six hundred');
		    when dig3right=7 then set ansright=concat(ansright, 'seven hundred');
		    when dig3right=8 then set ansright=concat(ansright, 'eight hundred');
		    when dig3right=9 then set ansright=concat(ansright, 'nine hundred');
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
		        when dig2right=1 then set ansright=concat(ansright, 'ten');
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

	set finalanswer=concat(ans, ' dollars');




	case
	 
	when ansright is NULL or ansright='' then

	set finalanswer=concat(finalanswer, '.');
	 
	else

	set finalanswer=concat(finalanswer, ' and ');
	set finalanswer=concat(finalanswer, ansright);
	set finalanswer=concat(finalanswer, ' cents.');

	end case;


	select trim(finalanswer) into finalanswer;



	select concat(upper(left(finalanswer,1)),lower(substring(finalanswer,2))) into finalanswer;


	return trim(finalanswer);

	    END$$



*/

/********************************************************************************************************************************
 * The SQL Stored Procedure for extracting the digits behind the decimal point. 
 * It uses SUBSTRING_INDEX, the value entering the database table should be changed 
 * to float so that the right hand side digits can be extracted well by this procedure.
 * For example if the amount is 240 it has to be changed to float so that it is 240.0 
 *********************************************************************************************************************************/

/*

CREATE PROCEDURE procN2W_2(n VARCHAR(200)) 
BEGIN 
IF (n LIKE '%[^a-zA-Z0-9]%') THEN 
SELECT 0 FROM test; 
ELSE 
SELECT SUBSTRING_INDEX(n,'.',-1); 
END IF; 
END$$
*/