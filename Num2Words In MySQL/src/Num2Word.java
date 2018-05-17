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


/******************************************************************************
 * The SQL Function for converting to word....works only up to 6 digits value, 
 * for example $999,999. It can be extended to accommodate more digits
 ******************************************************************************/
/*
DELIMITER $$
CREATE FUNCTION `N2W_Converter1`(n FLOAT) RETURNS varchar(200)
BEGIN
    -- This function returns the string representation of a number.
    -- The idea is:
    --      For each digit you need a position,
    --      For each position, you assign a string

    declare ans varchar(200);
    declare dig1, dig2, dig3, dig4, dig5, dig6 int;


set ans = '';

set dig6 = CAST(RIGHT(CAST(floor(n / 100000) as CHAR(8)), 1) as SIGNED);
set dig5 = CAST(RIGHT(CAST(floor(n / 10000) as CHAR(8)), 1) as SIGNED);
set dig4 = CAST(RIGHT(CAST(floor(n / 1000) as CHAR(8)), 1) as SIGNED);
set dig3 = CAST(RIGHT(CAST(floor(n / 100) as CHAR(8)), 1) as SIGNED);
set dig2 = CAST(RIGHT(CAST(floor(n / 10) as CHAR(8)), 1) as SIGNED);
set dig1 = CAST(RIGHT(floor(n), 1) as SIGNED);




if dig6 > 0 then
    case
        when dig6=1 then set ans=concat(ans, 'one hundred');
        when dig6=2 then set ans=concat(ans, 'two hundred');
        when dig6=3 then set ans=concat(ans, 'three hundred');
        when dig6=4 then set ans=concat(ans, 'four hundred');
        when dig6=5 then set ans=concat(ans, 'five hundred');
        when dig6=6 then set ans=concat(ans, 'six hundred');
        when dig6=7 then set ans=concat(ans, 'seven hundred');
        when dig6=8 then set ans=concat(ans, 'eight hundred');
        when dig6=9 then set ans=concat(ans, 'nine hundred');
        else set ans = ans;
    end case;
end if;

if dig5 = 1 then
    case
        when (dig5*10 + dig4) = 10 then set ans=concat(ans, ' ten thousand ');
        when (dig5*10 + dig4) = 11 then set ans=concat(ans, ' eleven thousand ');
        when (dig5*10 + dig4) = 12 then set ans=concat(ans, ' twelve thousand ');
        when (dig5*10 + dig4) = 13 then set ans=concat(ans, ' thirteen thousand ');
        when (dig5*10 + dig4) = 14 then set ans=concat(ans, ' fourteen thousand ');
        when (dig5*10 + dig4) = 15 then set ans=concat(ans, ' fifteen thousand ');
        when (dig5*10 + dig4) = 16 then set ans=concat(ans, ' sixteen thousand ');
        when (dig5*10 + dig4) = 17 then set ans=concat(ans, ' seventeen thousand ');
        when (dig5*10 + dig4) = 18 then set ans=concat(ans, ' eighteen thousand ');
        when (dig5*10 + dig4) = 19 then set ans=concat(ans, ' nineteen thousand ');
        else set ans=ans;
    end case;
else
    if dig5 > 0 then
        case
            when dig5=2 then set ans=concat(ans, ' twenty');
            when dig5=3 then set ans=concat(ans, ' thirty');
            when dig5=4 then set ans=concat(ans, ' fourty');
            when dig5=5 then set ans=concat(ans, ' fifty');
            when dig5=6 then set ans=concat(ans, ' sixty');
            when dig5=7 then set ans=concat(ans, ' seventy');
            when dig5=8 then set ans=concat(ans, ' eighty');
            when dig5=9 then set ans=concat(ans, ' ninety');
            else set ans=ans;
        end case;
    end if;
    if dig4 > 0 then
        case
            when dig4=1 then set ans=concat(ans, ' one thousand ');
            when dig4=2 then set ans=concat(ans, ' two thousand ');
            when dig4=3 then set ans=concat(ans, ' three thousand ');
            when dig4=4 then set ans=concat(ans, ' four thousand ');
            when dig4=5 then set ans=concat(ans, ' five thousand ');
            when dig4=6 then set ans=concat(ans, ' six thousand ');
            when dig4=7 then set ans=concat(ans, ' seven thousand ');
            when dig4=8 then set ans=concat(ans, ' eight thousand ');
            when dig4=9 then set ans=concat(ans, ' nine thousand ');
            else set ans=ans;
        end case;
    end if;
    if dig4 = 0 AND (dig5 != 0 || dig6 != 0) then
        set ans=concat(ans, ' thousand ');
    end if;
end if;

if dig3 > 0 then
    case
        when dig3=1 then set ans=concat(ans, 'one hundred');
        when dig3=2 then set ans=concat(ans, 'two hundred');
        when dig3=3 then set ans=concat(ans, 'three hundred');
        when dig3=4 then set ans=concat(ans, 'four hundred');
        when dig3=5 then set ans=concat(ans, 'five hundred');
        when dig3=6 then set ans=concat(ans, 'six hundred');
        when dig3=7 then set ans=concat(ans, 'seven hundred');
        when dig3=8 then set ans=concat(ans, 'eight hundred');
        when dig3=9 then set ans=concat(ans, 'nine hundred');
        else set ans = ans;
    end case;
end if;

if dig2 = 1 then
    case
        when (dig2*10 + dig1) = 10 then set ans=concat(ans, ' ten');
        when (dig2*10 + dig1) = 11 then set ans=concat(ans, ' eleven');
        when (dig2*10 + dig1) = 12 then set ans=concat(ans, ' twelve');
        when (dig2*10 + dig1) = 13 then set ans=concat(ans, ' thirteen');
        when (dig2*10 + dig1) = 14 then set ans=concat(ans, ' fourteen');
        when (dig2*10 + dig1) = 15 then set ans=concat(ans, ' fifteen');
        when (dig2*10 + dig1) = 16 then set ans=concat(ans, ' sixteen');
        when (dig2*10 + dig1) = 17 then set ans=concat(ans, ' seventeen');
        when (dig2*10 + dig1) = 18 then set ans=concat(ans, ' eighteen');
        when (dig2*10 + dig1) = 19 then set ans=concat(ans, ' nineteen');
        else set ans=ans;
    end case;
else
    if dig2 > 0 then
        case
            when dig2=2 then set ans=concat(ans, ' twenty');
            when dig2=3 then set ans=concat(ans, ' thirty');
            when dig2=4 then set ans=concat(ans, ' fourty');
            when dig2=5 then set ans=concat(ans, ' fifty');
            when dig2=6 then set ans=concat(ans, ' sixty');
            when dig2=7 then set ans=concat(ans, ' seventy');
            when dig2=8 then set ans=concat(ans, ' eighty');
            when dig2=9 then set ans=concat(ans, ' ninety');
            else set ans=ans;
        end case;
    end if;
    if dig1 > 0 then
        case
            when dig1=1 then set ans=concat(ans, ' one');
            when dig1=2 then set ans=concat(ans, ' two');
            when dig1=3 then set ans=concat(ans, ' three');
            when dig1=4 then set ans=concat(ans, ' four');
            when dig1=5 then set ans=concat(ans, ' five');
            when dig1=6 then set ans=concat(ans, ' six');
            when dig1=7 then set ans=concat(ans, ' seven');
            when dig1=8 then set ans=concat(ans, ' eight');
            when dig1=9 then set ans=concat(ans, ' nine');
            else set ans=ans;
        end case;
    end if;
end if;

return trim(ans);
END
$$


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