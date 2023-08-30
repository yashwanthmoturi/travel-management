package recovery;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;


class Mailer{  
    public static void send(final String from,final String password,String to,String sub,String msg){  
          //Get properties object    
          Properties props = new Properties();    
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465");    
          //get Session   
          Session session = Session.getDefaultInstance(props,    
           new javax.mail.Authenticator() {    
           protected PasswordAuthentication getPasswordAuthentication() {    
           return new PasswordAuthentication(from,password);  
           }    
          });    
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
           message.setSubject(sub);    
           message.setText(msg);    
           //send message  
           Transport.send(message);    
           System.out.println("message sent successfully");    
          } catch (MessagingException e) {throw new RuntimeException(e);}    
             
    }  
}  
public class PasswrdRecovery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public PasswrdRecovery() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			String uname = request.getParameter("uname");
			Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con =  
            DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","admin");
            String s1="select * from login where uname=?";
            PreparedStatement ps=con.prepareStatement(s1);
            ps.setString(1, uname);
            ResultSet rs=ps.executeQuery();
            if(!rs.next())
            {
            	RequestDispatcher rd = request.getRequestDispatcher("forgetpassword.html");
         		  rd.include(request, response);
            	out.println("Username does not exist Try again or register a new account");
            	int b=5/0;
            }
            int pass = 100000*((int)(10.0*Math.random()))+(int)(10000.0* Math.random());
            String passwd=String.format("%d",pass);
			String s3="select * from login_details where uname=?";
			PreparedStatement ps2=con.prepareStatement(s3);
			ps2.setString(1, uname);
			ResultSet rs1=ps2.executeQuery();
			String mail="";
		//	out.println("Hello");
			if(rs1.next()) {
			mail=rs1.getString(2);
			}
		//	out.println(mail);
			Mailer.send("saivishaltirumala4064@gmail.com","Saivishal@123",mail,"SHY Travels",passwd+" is your SHY Travels account password");			
			String s2="update login set pwd=? where uname=?";
			PreparedStatement ps1=con.prepareStatement(s2);
			ps1.setString(1, passwd);
			ps1.setString(2, uname);
			ps1.executeUpdate();
			RequestDispatcher rd = request.getRequestDispatcher("login.html");
   		    rd.include(request, response);
   		    out.println("PASSWORD CHANGED SUCCESSFULLY YOUR NEW PASSWORD SENT TO YOUR MAIL.");
			con.close();
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