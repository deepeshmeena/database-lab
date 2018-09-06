import java.sql.*;
import java.util.*;
public class Ass4B_Part1_150050041_150050077.java{
	 static String sql ;
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter your query: ");
		 sql = scanner.nextLine();

					toHTML();
					toJSON();
					
  		
	}

public static void toHTML() {
	
	try (
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5410/postgres", "deepesh", "");
		   
		)
		{
			conn.setAutoCommit(false);
			
			 try(PreparedStatement stmt1 =conn.prepareStatement(sql); 
		            // PreparedStatement  stmt2 = …;
		              )
			 { 
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

							System.out.println("</table>");  
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

public static void toJSON() {
	try (
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5410/postgres", "deepesh", "");
		   
		)
		{
			conn.setAutoCommit(false);
			
			 try(PreparedStatement stmt1 =conn.prepareStatement(sql); 
		            // PreparedStatement  stmt2 = …;
		              )
			 { 
			 		ResultSet rs=stmt1.executeQuery();  
					ResultSetMetaData rsmd=rs.getMetaData(); 
				System.out.print("{header: [");
            //System.out.print("<tr>");
            int x = 1;


            while (x <=rsmd.getColumnCount() )
            {
                System.out.print("\"" +rsmd.getColumnName(x)+ "\"");
                if(x!=rsmd.getColumnCount())System.out.print(",");
                x++;
            }
            System.out.print("],");
            System.out.println();
            System.out.print("data: [");
            int rows = 0;

            while (rs.next()) {
                        //String row = "";
                             if (rows>0) {System.out.print(",");
                             System.out.println();}
                             System.out.print("{");
                             for (int i = 1; i <= rsmd.getColumnCount() ; i++) {
                             System.out.print(rsmd.getColumnName(i)+":");
                       		 System.out.print("\""+rs.getString(i)+"\"");
                       		 if(i!=rsmd.getColumnCount())System.out.print(",");
                           //System.out.print("</td>");          
                        }
                        
                        System.out.print("}");
                        
                    rows++;                                


}

					System.out.println(); 
					System.out.println("]");  
					System.out.println("}");

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


