import java.sql.*;
import java.util.Scanner;

import java.io.*;

/* NAME = MEET TARAVIYA*/
/* ROLL No = 150050002*/

public class ViewMetaData {
	private static final String connectionUrl = "jdbc:postgresql://localhost:5020/postgres";
	private static final String userName = "labuser";
	private static final String passWord = passWord;

	private static void printSchema(Connection conn) throws SQLException{
		DatabaseMetaData dmd = conn.getMetaData();
		ResultSet tableList = dmd.getTables("", "", "%",new String[] {"TABLE"}), columnList; 

		while(tableList.next()) {
			columnList = dmd.getColumns(null, null, tableList.getString("TABLE_NAME"), null);
			
			System.out.println(
				String.format(
					"CREATE %S %s(",
					tableList.getString("TABLE_TYPE"),
					tableList.getString("TABLE_NAME")
					)
				);
			
			while(columnList.next()){
				System.out.println(
					String.format(
						"\t%-20s %S(%d%s)%s",
						columnList.getString("COLUMN_NAME"),
						columnList.getString("TYPE_NAME"),
						columnList.getInt("CHAR_OCTET_LENGTH"),
						columnList.getString("TYPE_NAME").equals("numeric") ? String.format(",%d", columnList.getInt("DECIMAL_DIGITS")) : "",
						columnList.isLast() ? "" : ","
						)
					);
			}

			System.out.println("\t);\n");

		}
	}
	private static void injection(Connection conn) throws SQLException{
		
		Scanner sc = new Scanner(System.in);
		String pattern;
		
		// PreparedStatement pstmt = conn.prepareStatement("SELECT id, name FROM bigstudent WHERE name ILIKE ?");
		Statement stmt = conn.createStatement();

		ResultSet rs;

		while(true){
			System.out.print("\nEnter search pattern: ");
			pattern = sc.nextLine();
			System.out.println();

			// pstmt.setString(1,pattern);
			// rs = pstmt.executeQuery();

			// Allows SQL injection, using following statement to delete all entries in bigstudent	
			// '; delete from bigstudent; --
			rs = stmt.executeQuery("SELECT id, name FROM bigstudent WHERE name ILIKE '" + pattern + "'");

			while(rs.next()){
				System.out.println(
					String.format(
						"\t%5s\t%s",
						rs.getString("id"),
						rs.getString("name"))
					);
			}
		}
	}


	public static void main(String[] args) {
	
		try (
			Connection conn = DriverManager.getConnection(
					connectionUrl, userName, passWord);
			Statement stmt = conn.createStatement();
		)
		{
			printSchema(conn);
			injection(conn);
		}
		catch (Exception sqle)
		{
		System.out.println("Exception : " + sqle);
		}
	}
}
