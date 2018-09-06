import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

// import com.sun.corba.se.spi.orbutil.fsm.State;

import java.io.*;
/* NAME = MEET TARAVIYA*/
/* ROLL No = 150050002*/

public class Main {
	private static final String connectionUrl = "jdbc:postgresql://localhost:5020/postgres";
	private static final String userName = "labuser";
	private static final String passWord = "";
	
	// Failed, gave error: org.postgresql.util.PSQLException: FATAL: sorry, too many clients already
	private static int ConnectionLeakage(ArrayList<String> rows) {
		String values[];
		for(String row : rows) {
			values = row.split(", ");
			try {
				Connection conn = DriverManager.getConnection(
						connectionUrl, userName, passWord);
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO BIGSTUDENT VALUES(?,?,?,?);");
				stmt.setString(1, values[0].substring(1, values[0].length()-1));
				stmt.setString(2, values[1].substring(1, values[1].length()-1));
				stmt.setString(3, values[2].substring(1, values[2].length()-1));
				stmt.setInt(4, Integer.parseInt(values[3]));
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				return -1;
			}
		}
		return 0;
	}
	
	// Succeeded, completed in 46.861 seconds
	private static int ConnectionPerRow(ArrayList<String> rows) {
		String values[];
		for(String row : rows) {
			values = row.split(", ");
			try (Connection conn = DriverManager.getConnection(
					connectionUrl, userName, passWord);){
				
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO BIGSTUDENT VALUES(?,?,?,?);");
				stmt.setString(1, values[0].substring(1, values[0].length()-1));
				stmt.setString(2, values[1].substring(1, values[1].length()-1));
				stmt.setString(3, values[2].substring(1, values[2].length()-1));
				stmt.setInt(4, Integer.parseInt(values[3]));
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				return -1;
			}
		}
		return 0;
	}

	// Succeeded, completed in 17.676 seconds
	private static int SingleConnection(ArrayList<String> rows) {
		String values[];

		try (Connection conn = DriverManager.getConnection(
				connectionUrl, userName, passWord);){
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO BIGSTUDENT VALUES(?,?,?,?);");
			for(String row : rows) {
					values = row.split(", ");
					stmt.setString(1, values[0].substring(1, values[0].length()-1));
					stmt.setString(2, values[1].substring(1, values[1].length()-1));
					stmt.setString(3, values[2].substring(1, values[2].length()-1));
					stmt.setInt(4, Integer.parseInt(values[3]));
					stmt.executeUpdate();
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	// Succeeded, completed in 0.866 seconds
	private static int GroupCommit(ArrayList<String> rows) {
		String values[];

		try (Connection conn = DriverManager.getConnection(
				connectionUrl, userName, passWord);){
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO BIGSTUDENT VALUES(?,?,?,?);");
			for(int i=0; i<rows.size(); i++) {
					values = rows.get(i).split(", ");
					stmt.setString(1, values[0].substring(1, values[0].length()-1));
					stmt.setString(2, values[1].substring(1, values[1].length()-1));
					stmt.setString(3, values[2].substring(1, values[2].length()-1));
					stmt.setInt(4, Integer.parseInt(values[3]));
					stmt.executeUpdate();
					if((i+1)%1000==0) {
						conn.commit();
					}
				
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	// Succeeded, completed in 0.468 seconds
	private static int BatchedInsert(ArrayList<String> rows) {
		String values[];

		try (Connection conn = DriverManager.getConnection(
				connectionUrl, userName, passWord);){
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO BIGSTUDENT VALUES(?,?,?,?);");
			for(int i=0; i<rows.size(); i++) {
					values = rows.get(i).split(", ");
					stmt.setString(1, values[0].substring(1, values[0].length()-1));
					stmt.setString(2, values[1].substring(1, values[1].length()-1));
					stmt.setString(3, values[2].substring(1, values[2].length()-1));
					stmt.setInt(4, Integer.parseInt(values[3]));
					
					stmt.addBatch();
					
					if((i+1)%1000==0) {
						stmt.executeBatch();
					}
				
			}
			stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	private static void SQLInjectionTestWithRawStatement() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter name to search: ");
		String name = sc.nextLine();
		
		String query = "select * from bigstudent where name like '%"+name+"%';";
				
		try (Connection conn = DriverManager.getConnection(
				connectionUrl, userName, passWord);){
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			System.out.println("Search results:-");
			while(rs.next()) {
				System.out.println(
						rs.getString(1)+","
						+rs.getString(2)+","
						+rs.getString(3)+","
						+rs.getString(4));
			}
			System.out.println("Query executed: "+query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private static void SQLInjectionTestWithPreparedStatement() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter name to search: ");
		String name = sc.nextLine();
		
		String query = "select * from bigstudent where name like ?;";
				
		try (Connection conn = DriverManager.getConnection(
				connectionUrl, userName, passWord);){
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, "%"+name+"%");
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("Search results:-");
			while(rs.next()) {
				System.out.println(
						rs.getString(1)+","
						+rs.getString(2)+","
						+rs.getString(3)+","
						+rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
	
		try (
			Connection conn = DriverManager.getConnection(
					connectionUrl, userName, passWord);
			Statement stmt = conn.createStatement();
		)
		{
			
			try {
				stmt.executeUpdate("DROP TABLE IF EXISTS BIGSTUDENT;");
			}
			catch(SQLException e) {
				System.out.println("Failed to drop table." + e);
			}
			try {
				stmt.executeUpdate("create table BIGSTUDENT " + 
						"	(ID varchar(5), " + 
						"	 name varchar(20) not null, " + 
						"	 dept_name varchar(20), " + 
						"	 tot_cred numeric(3,0) check (tot_cred >= 0), " + 
						"	 primary key (ID)" + 
						"	);");
			} catch (SQLException e) {
				System.out.println("Failed to create table bigstudent."+e);
			}
		}
		catch (Exception sqle)
		{
		System.out.println("Exception : " + sqle);
		}
		
		String dataFilePath = "student.txt";
		BufferedReader br = null;
		String line = passWord;
		
		ArrayList<String> rows = new ArrayList<>();
		
		try {
			br = new BufferedReader( new FileReader(dataFilePath));
			while ((line = br.readLine()) != null) {
				rows.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long startTime, elapsedTime;
		int success;
		
		startTime = System.nanoTime();
		// success = ConnectionLeakage(rows);
		// success = ConnectionPerRow(rows);
		// success = SingleConnection(rows);
		// success = GroupCommit(rows);
		success = BatchedInsert(rows);
		elapsedTime = System.nanoTime() - startTime;
		elapsedTime /= 1000000;
		
		System.out.println("Time elapsed in called function: "+ Long.toString(elapsedTime)+" ms");
		if(success==0) {
			System.out.println("Call succeeded");
		}
		else {
			System.out.println("Call failed");
		}
		while(true)
		// SQLInjectionTestWithRawStatement();
		// 	Enter "e%'; delete from bigstudent;--" to delete all records
			SQLInjectionTestWithPreparedStatement();
	}
}
