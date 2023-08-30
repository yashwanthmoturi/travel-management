package login;

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

public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ChangePassword() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String uname = request.getParameter("uname");
		String pwd = request.getParameter("pwd");
		String newpwd = request.getParameter("newpwd");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con =  
            DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","admin");
            String s1="select * from login where uname=? and pwd=?";
            PreparedStatement ps=con.prepareStatement(s1);
            ps.setString(1, uname);
            ps.setString(2, pwd);
            ResultSet rs=ps.executeQuery();
            if(!rs.next())
            {
            	RequestDispatcher rd = request.getRequestDispatcher("changepassword.html");
         		 rd.include(request, response);
            	out.println("INVALID CREDENTIALS RECHECK YOUR USERNAME AND CURRENT PASSWORD");
            	int a=5/0;
            }
            String s2="update login set pwd=? where uname=?";
            PreparedStatement ps1=con.prepareStatement(s2);
            ps1.setString(1, newpwd);
            ps1.setString(2, uname);
            ps1.executeUpdate();
            RequestDispatcher rd = request.getRequestDispatcher("login.html");
     		rd.include(request, response);
     		out.println("Your password has Succesfully updated.");
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
