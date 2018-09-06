import java.sql.*;
import java.util.*;
public class test{
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter your query: ");
		String sql = scanner.nextLine();

		//System.out.print("Enter your course id: ");
		//String course_id = scanner.next();

		//System.out.print("Enter your sec_id: ");
		//String sec_id = scanner.next();
		
		//System.out.print("Enter your semester: ");
		//String semester = scanner.next();
		
		//System.out.print("Enter your year: ");
		//String number = scanner.next();
		//int num = in.nextInt();
		
		//System.out.print("Enter your grade: ");
		//String grade = scanner.next();
		//int year = Integer.parseInt(number);	
		
		try (
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5410/postgres", "deepesh", "");
		   //Statement stmt1 = conn.createStatement();
		)
		{
			conn.setAutoCommit(false);
			//String sql = "UPDATE takes SET grade=? WHERE ID=? AND course_id=? and sec_id=? and semester=? and year=? ";
			 try(PreparedStatement stmt1 =conn.prepareStatement(sql); 
		            // PreparedStatement  stmt2 = …;
		              )
			 {    

					//PreparedStatement ps=con.prepareStatement("select * from emp");  
					ResultSet rs=stmt1.executeQuery();  
					ResultSetMetaData rsmd=rs.getMetaData();  
					System.out.println("<table>");
					System.out.print("<tr>");
					int x = 1;
 
       
			        while (x <=rsmd.getColumnCount() )
			        {
			            System.out.print("<th>" +rsmd.getColumnName(x)+ "</th>");
			 			x++;
			        }
					System.out.print("</tr>");
					System.out.println();

					while (rs.next()) {
						        //String row = "";
						         System.out.print("<tr>");
						        for (int i = 1; i <= rsmd.getColumnCount() ; i++) {
						        	 System.out.print("<td>");
						           System.out.print(rs.getString(i));
						           System.out.print("</td>");          
						        }
						        //System.out.println();
						        //System.out.println(row);
						        System.out.print("</tr>");
						        System.out.println();



    					}

					//System.out.println("<th>"+rsmd.getColumnName(1)+"</th>");
					//System.out.println("<th>"+rsmd.getColumnName(2)+"</th> </tr>");  
					//System.out.println("Total columns: "+rsmd.getColumnCount());  
					//System.out.println("Column Name of 1st column: "+rsmd.getColumnName(1));  
					//System.out.println("Column Type Name of 1st column: "+rsmd.getColumnTypeName(1));  
					System.out.println("</table>");  




			 	//	stmt1.setString(1,grade );
				// 	stmt1.setString(2, ID);
				//	stmt1.setString(3,course_id );
				//	stmt1.setString(4, sec_id);   
				//	stmt1.setString(5,semester );
				//	stmt1.setInt(6, year);
				//	stmt1.setString(6, grade);
				 
				 //stmt1.executeUpdate();
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