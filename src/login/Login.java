package login;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class Login extends HttpServlet
{

		protected void doGet (HttpServletRequest request, HttpServletResponse response)throws                         
         ServletException,IOException{
		 response.setContentType("text/html");
         PrintWriter out = response.getWriter();
        
        //Capture the form data into servlet
        String uname = request.getParameter("uname");
        String pwd = request.getParameter("pwd");
        HttpSession session = request.getSession();
        session.setAttribute("uname", uname);
        //Connection establishment with DB
       try{
               Class.forName("oracle.jdbc.driver.OracleDriver");
               Connection con =  
               DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","admin");
               
               String s1 = "select * from login where uname=? and pwd=?";
               PreparedStatement ps = con.prepareStatement(s1);
              
               ps.setString(1,uname);             
               ps.setString(2,pwd);
               ResultSet rs = ps.executeQuery();
   
               if(rs.next())
               {
            	   response.sendRedirect("HomePage.html");
               }
               else
               {
            	  RequestDispatcher rd = request.getRequestDispatcher("login.html");
           		  rd.include(request, response);
           		out.println("INVALID CREDENTIALS RECHECK YOUR USERNAME AND PASSWORD");
               }
               con.close( );
           } 
          catch(Exception e)
          {
                 out.println(e);
          }
		}
       protected void doPost(HttpServletRequest request, HttpServletResponse response)throws                         
       ServletException,IOException{
    	   doGet(request,response);
       }  
  }