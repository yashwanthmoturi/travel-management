package register;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String mobile = request.getParameter("mobile");
        String uname = request.getParameter("uname");
        String pwd = request.getParameter("pwd");

        //Connection establishment with DB
       try{
               Class.forName("oracle.jdbc.driver.OracleDriver");
               Connection con =  
               DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","admin");
               String s="select * from login where uname=?";
               PreparedStatement ps2=con.prepareStatement(s);
               ps2.setString(1,uname);
               ResultSet rs=ps2.executeQuery();
               if(rs.next())
               {
            	   RequestDispatcher rd = request.getRequestDispatcher("register.html");
           		   rd.include(request, response);
            	   out.println("UserName already Exists");
            	   int a=1/0;
               }
               String s1="insert into login values(?,?)";
               String s2="insert into login_details values(?,?,?,?)";
               PreparedStatement ps=con.prepareStatement(s1);
               PreparedStatement ps1=con.prepareStatement(s2);
               ps.setString(1, uname);
               ps.setString(2, pwd);
               ps1.setString(1, name);
               ps1.setString(2, email);
               ps1.setString(3, mobile);
               ps1.setString(4, uname);
               ps.executeUpdate();
               ps1.executeUpdate();
               RequestDispatcher rd = request.getRequestDispatcher("login.html");
       		   rd.include(request, response);
               out.println("Registered Sucessfully!!!");
	}
       catch(Exception e)
       {
    	   
       }
}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws                         
    ServletException,IOException{
 	   doGet(request,response);
    }  
}