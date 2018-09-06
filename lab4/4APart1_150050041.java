import java.sql.*;
import java.util.*;
public class test{
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		//System.out.print("Enter your id: ");
		String ID = scanner.next();

		//System.out.print("Enter your course id: ");
		String course_id = scanner.next();

		//System.out.print("Enter your sec_id: ");
		String sec_id = scanner.next();
		
		//System.out.print("Enter your semester: ");
		String semester = scanner.next();
		
		//System.out.print("Enter your year: ");
		String number = scanner.next();
		//int num = in.nextInt();
		
		//System.out.print("Enter your grade: ");
		String grade = scanner.next();
		int year = Integer.parseInt(number);	
		
		try (
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5410/postgres", "deepeshm", "");
		   //Statement stmt1 = conn.createStatement();
		)
		{
			conn.setAutoCommit(false);
			String sql = "UPDATE takes SET grade=? WHERE ID=? AND course_id=? and sec_id=? and semester=? and year=? ";
			 try(PreparedStatement stmt1 =conn.prepareStatement(sql); 
		            // PreparedStatement  stmt2 = …;
		              )
			 {      stmt1.setString(1,grade );
				 	stmt1.setString(2, ID);
					stmt1.setString(3,course_id );
					stmt1.setString(4, sec_id);   
					stmt1.setString(5,semester );
					stmt1.setInt(6, year);
				//	stmt1.setString(6, grade);
				 
				 stmt1.executeUpdate();
			               conn.commit();         // also can cause SQLException!
			       }
			 catch(Exception ex) {
		               conn.rollback();
		               throw ex; /* Other exception handling will be done at outer level */
		       } finally {
		               conn.setAutoCommit(true);
		       }
		}
		catch(Exception e) {
		       e.printStackTrace();
		}
		
		
	}
	}	