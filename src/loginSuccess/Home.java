package loginSuccess;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.util.Properties;

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

public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public Home() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();
			String place1=request.getParameter("place1");
			String place2=request.getParameter("place2");
			String date = request.getParameter("journey");
			String user = (String) session.getAttribute("uname");
			if(user.equals(null))
			{
				out.println("hello");
				int a=1/0;
			}
		//	out.println(user);
			String nooftickets = request.getParameter("nooftickets");
			String date1 = date.replaceAll("-", "");
			if(place1.equals(place2))
			{
				out.println("You have Entered same City on both sides");
				int c=1/0;
			}
			/*out.println(place1);
			out.println(place2);
			out.println(date1);*/
			Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con =  
            DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","admin");
            String s1="select * from bustickets where datee=? and place1=? and place2=?";
            PreparedStatement ps = con.prepareStatement(s1);
            ps.setString(1, date1);
            ps.setString(2, place1);
            ps.setString(3, place2);
            ResultSet rs=ps.executeQuery();
  
            if(rs.next())
            {
            	String s2="select tickets_left from bustickets where datee=? and place1=? and place2=?";
            	
            	PreparedStatement ps1 = con.prepareStatement(s2);
            	ps1.setString(1, date1);
            	ps1.setString(2, place1);
            	ps1.setString(3, place2);
            	ResultSet rs1=ps1.executeQuery();
            	if(rs1.next()) {
            		int s=rs1.getInt(1);
            		int t=Integer.parseInt(nooftickets);
            		if(s-t<0) {
            			out.println("Bus tickets are insufficients");
            			out.println((t-s)+" more tickets required.\nSo choose other slot.");
            			RequestDispatcher rd = request.getRequestDispatcher("HomePage.html");
               		  rd.include(request, response);
            		}
            		else {
            			String s4="update bustickets set tickets_left=? where datee=? and place1=? and place2=?";
            			PreparedStatement ps4 = con.prepareStatement(s4);
            			ps4.setInt(1, s-t);
            			ps4.setString(2, date1);
            			ps4.setString(3, place1);
            			ps4.setString(4, place2);
            			ps4.executeUpdate();
            			String s6="select email from login_details where uname=?";
            			PreparedStatement ps6=con.prepareStatement(s6);
                		ps6.setString(1, user);
                		ResultSet rs6=ps6.executeQuery();
                		String mail1="";
                		if(rs6.next())
                		{
                			mail1=rs6.getString(1);
                		}
            			String message = "You have Successfully booked "+t+" tickets successfully from "+place1+" to "+place2+" on "+date+".\nEnjoy the journey.";
                		Mailer.send("saivishaltirumala4064@gmail.com","Saivishal@123",mail1,"SHY Travels",message);
                		out.println("tickets booked successfully check your email");
                		RequestDispatcher rd = request.getRequestDispatcher("HomePage.html");
                		rd.include(request, response);
            		}
            	}
            }
            	else
            	{
            		String s3="insert into bustickets values(?,?,?,?)";
            		PreparedStatement ps3=con.prepareStatement(s3);
            		ps3.setString(1, date1);
            		ps3.setString(2, place1);
            		ps3.setString(3, place2);
            		int tem=Integer.parseInt(nooftickets);
            		ps3.setInt(4, 20-tem);
            		ps3.executeUpdate();
            		String message = "You have Successfully booked "+tem+" tickets successfully from "+place1+" to "+place2+" on "+date+".\nEnjoy the journey.";
            		String s5="select email from login_details where uname=?";
            		PreparedStatement ps5=con.prepareStatement(s5);
            		ps5.setString(1, user);
            		ResultSet rs5=ps5.executeQuery();
            		String mail="";
            		if(rs5.next())
            		{
            			mail=rs5.getString(1);
            		}
            		Mailer.send("saivishaltirumala4064@gmail.com","Saivishal@123",mail,"SHY Travels",message);
            		out.println("tickets booked successfully check your email");
            		RequestDispatcher rd = request.getRequestDispatcher("HomePage.html");
            		rd.include(request, response);
            	}
		}
		catch(Exception e) {
			PrintWriter out1=response.getWriter();
			RequestDispatcher rd = request.getRequestDispatcher("login.html");
     		  rd.include(request, response);
			out1.println("Your session has been Expired login again.");
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
