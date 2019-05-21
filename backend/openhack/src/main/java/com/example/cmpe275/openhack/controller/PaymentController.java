package com.example.cmpe275.openhack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Payment;
import com.example.cmpe275.openhack.entity.Team;
import com.example.cmpe275.openhack.entity.User;
import com.example.cmpe275.openhack.service.HackathonRepositoryService;
import com.example.cmpe275.openhack.service.OrganizationRepositoryService;
import com.example.cmpe275.openhack.service.PaymentRepositoryService;
import com.example.cmpe275.openhack.service.RequestRepositoryService;
import com.example.cmpe275.openhack.service.SubmissionRepositoryService;
import com.example.cmpe275.openhack.service.TeamRepositoryService;
import com.example.cmpe275.openhack.service.UserRepositoryService;

@RequestMapping("/payment")
@Controller
public class PaymentController {
	
//	private UserDao userDao;
//	private HackathonDao hackathonDao;
//	private OrganizationDao organizationDao;
//	private TeamDao teamDao;
//	private PaymentDao paymentDao;
	
	@Autowired
	HackathonRepositoryService hackathonDao;
	@Autowired
	UserRepositoryService userDao;
	@Autowired
	OrganizationRepositoryService organizationDao;
	@Autowired
	TeamRepositoryService teamDao;
	@Autowired
	PaymentRepositoryService paymentDao;
	@Autowired
	RequestRepositoryService requestDao;
	@Autowired
	SubmissionRepositoryService submissionDao;
	
	public PaymentController() {
		// TODO Auto-generated constructor stub
//		userDao = new UserDaoImpl();
//		hackathonDao = new HackathonDaoImpl();
//		organizationDao = new OrganizationDaoImpl();
//		teamDao = new TeamDaoImpl();
//		paymentDao = new PaymentDaoImpl();
	}
//	@Autowired
//	UserDaoImpl userDao;
//	HackathonDaoImpl hackathonDao;
//	OrganizationDaoImpl organizationDao;
//	TeamDaoImpl teamDao;
//	PaymentDaoImpl paymentDao;

	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ResponseBody
	
	public Map<Object,Object> getPayment(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(name="id") long paymentId){
		
		Map<Object,Object> responseObject = new HashMap<>();
		try {
			Payment payment = paymentDao.getPaymentById(paymentId);
			responseObject.put("fee", payment.getFee());
			responseObject.put("memberId", payment.getMemberId());
			responseObject.put("teamId", payment.getTeamId());
			responseObject.put("status", payment.getStatus());
			return responseObject;
		}catch (Exception e) {
			// TODO: handle exception
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
			responseObject.put("msg",e.getMessage());
		}
		return responseObject;
	}
	
	@RequestMapping(value="/team/{id}",method=RequestMethod.GET)
	@ResponseBody
	public List<Payment> getPaymentByTeam(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(name="id") long teamId){
		
		List<Payment> paymentByTeam = new ArrayList<>();
		try {
			paymentByTeam = paymentDao.findPaymentByTeamId(teamId);
			return paymentByTeam;
		}catch (Exception e) {
			// TODO: handle exception
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
		}
		return paymentByTeam;
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.POST)
	@ResponseBody
	
	public Map<Object,Object> doPayment(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(name="id") long paymentId,
			@RequestBody Map<Object,Object> requestBody){
		
		Map<Object,Object> responseObject = new HashMap<>();
		final long teamId = new Long((Integer)requestBody.get("teamId"));
		final long memberId = new Long((Integer)requestBody.get("memberId"));

		Payment payment = paymentDao.getPaymentById(paymentId);
		payment.setStatus(true);
		try {
			paymentDao.updatePayment(payment);
			responseObject.put("msg","Payment Successfull");
//			ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
//	        emailExecutor.execute(new Runnable() {
//	            @Override
//	            public void run() {
//	                try {	          
	                	sendEmailConfirmation(userDao.findUserbyID(memberId), teamDao.getTeamById(teamId).getParticipatedHackathon());
	                	boolean flag = true;
	                    List<Payment> payments = paymentDao.findPaymentByTeamId(teamId);
	                    System.out.println(payments);
	                    for(Payment paymentObj : payments) {
	                    	if(!paymentObj.getStatus()) {
	                    		flag=false;
	                    		break;
	                    	}
	                    }
	                    if(flag) {
	                    	Team team = teamDao.getTeamById(teamId);
	                    	team.setPaymentStatus(true);
	                    	Team updatedTeam = teamDao.updateTeam(team);
	                    	sendFinalConfirmation(updatedTeam.getTeamLead(),updatedTeam.getParticipatedHackathon());	          
	                    }
//	                } catch (Exception e) {
//	                	System.out.println("error in sending mails: "+e.getMessage());
//	                }
//	            }
//	        });
//	        emailExecutor.shutdown();
			return responseObject;
		}catch (Exception e) {
			// TODO: handle exception
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
			responseObject.put("msg",e.getMessage());
		}
		return null;
	}
	
	public void sendEmailConfirmation(User user, Hackathon hackathon) {
		final String username = "openhackservice@gmail.com";
        final String password = "openhack123";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
        		Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(user.getEmail())
                );
                message.setSubject("OpenHack: Payment Confirmation");
                message.setText("Hey "+user.getEmail()+", "
                        + "\n\n Thank you for the payment for registering to the hackathon!!! We will send you to payment receipt shortly."
                		+ "\n\n Hackathon Details are as follows"
                        + "\n Hackathon Name: "+hackathon.getName()
                        + "\n Hackathon Description: "+hackathon.getDescription() 
                        + "\n Hackathon Start Date: "+hackathon.getStartDate() 
                        + "\n Hackathon End Date: "+hackathon.getEndDate()
                        + "\n\n See you soon, Hacker!!!"
                        + "\n\n Happy Hacking,"
                		+"\n OpenHack Service");
                Transport.send(message);
                System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}
	
	public void sendFinalConfirmation(User user, Hackathon hackathon) {
		final String username = "openhackservice@gmail.com";
        final String password = "openhack123";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
        		Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(user.getEmail())
                );
                message.setSubject("OpenHack: Payment Process Completed");
                message.setText("Dear "+user.getEmail()+", "
                        + "\n\n The whole team has successfully completed the payment process for the following hackathon"
                        + "\n\n Hackathon Name: "+hackathon.getName()
                        + "\n Hackathon Description: "+hackathon.getDescription() 
                        + "\n Hackathon Start Date: "+hackathon.getStartDate() 
                        + "\n Hackathon End Date: "+hackathon.getEndDate()
                        + "\n\n See you soon, Hacker!!!"
                        +"\n\n Happy Hacking,"
                		+"\n OpenHack Service");
                Transport.send(message);
                System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}
}
