package com.example.cmpe275.openhack.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.cmpe275.openhack.dao.HackathonDao;
import com.example.cmpe275.openhack.dao.HackathonDaoImpl;
import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.OrganizationDaoImpl;
import com.example.cmpe275.openhack.dao.PaymentDao;
import com.example.cmpe275.openhack.dao.PaymentDaoImpl;
import com.example.cmpe275.openhack.dao.TeamDao;
import com.example.cmpe275.openhack.dao.TeamDaoImpl;
import com.example.cmpe275.openhack.dao.UserDao;
import com.example.cmpe275.openhack.dao.UserDaoImpl;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.Payment;
import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.entity.Team;
import com.example.cmpe275.openhack.entity.User;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;

@RequestMapping("/hackathon")
@Controller
public class HackathonController {


	private UserDao userDao;
	private HackathonDao hackathonDao;
	private OrganizationDao organizationDao;
	private TeamDao teamDao;
	private PaymentDao paymentDao;
	
	public HackathonController() {
		// TODO Auto-generated constructor stub
		userDao = new UserDaoImpl();
		hackathonDao = new HackathonDaoImpl();
		organizationDao = new OrganizationDaoImpl();
		teamDao = new TeamDaoImpl();
		paymentDao = new PaymentDaoImpl();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> createHackathon(
			@RequestBody HashMap<Object,Object> map,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		System.out.println("\n Hackathon to be created");
		Map<Object,Object> responseObject = new HashMap<>();
		String name = (String)map.get("name");
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse((String)map.get("startDate"));
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse((String)map.get("endDate"));
		String description = (String)map.get("description");
		int teamSizeMin = (int) map.get("teamSizeMin");
		int teamSizeMax = (int) map.get("teamSizeMax");
		double fee = new Double((int)map.get("fee"));
		List<Integer> judgesId = (List<Integer>) map.get("judgesId");
		Hackathon hackathon = new Hackathon();
		hackathon.setName(name);
		hackathon.setDescription(description);
		hackathon.setStartDate(startDate);
		hackathon.setEndDate(endDate);
		hackathon.setTeamSizeMin(teamSizeMin);
		hackathon.setTeamSizeMax(teamSizeMax);
		hackathon.setFee(fee);
		if(map.get("discount") != null) {
			double discount = new Double((String)map.get("discount"));
			hackathon.setDiscount(discount);
		}
		Set<User> judges = new HashSet<>();
		for(int i=0;i<judgesId.size();i++) {
			User temp = userDao.findUserbyID(judgesId.get(i));
			judges.add(temp);
		}
		hackathon.setJudges(judges);
		if(map.get("sponsorsId")!=null) {
			List<Integer> sponsorsId = (List<Integer>)map.get("sponsorsId");
			Set<Organization> sponsors = new HashSet<>();
			for(int i=0;i<sponsorsId.size();i++) {
				Organization temp = organizationDao.findOrganizationById(sponsorsId.get(i));
				sponsors.add(temp);
			}
			hackathon.setSponsors(sponsors);
		}
		try {
			hackathonDao.create(hackathon);
			responseObject.put("msg", "Successfully created");
		}catch (Exception e) {
			// TODO: handle exception
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
			responseObject.put("msg",e.getMessage());
		}
		return responseObject;
	}
	
	@RequestMapping(value="{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<Object,Object> getHackathonDetails(HttpServletRequest request,
												HttpServletResponse response,
												@RequestBody Map<Object,Object> map,
												@PathVariable(name="id") long hackathonId){
		System.out.print("Getting hackathon details");
		long userId = new Long((String)map.get("userId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		User user = userDao.findUserbyID(userId);
		return createHackathonResponseBody(hackathon, user);
	}
	
	public Map<Object,Object> createHackathonResponseBody(Hackathon hackathon,User user){
		Map<Object,Object> responseBody = new HashMap<>();
		String message="";
		Team userTeam = null;
		String submissionUrl = "";
		responseBody.put("id",hackathon.getId());
		responseBody.put("name",hackathon.getName());
		responseBody.put("description",hackathon.getDescription());
		responseBody.put("startDate",hackathon.getStartDate());
		responseBody.put("endDate",hackathon.getEndDate());
		responseBody.put("fee",hackathon.getFee());
		responseBody.put("teamSizeMin", hackathon.getTeamSizeMin());
		responseBody.put("teamSizeMax", hackathon.getTeamSizeMax());
		responseBody.put("discount",hackathon.getDiscount());
		Set<Team> teams = hackathon.getTeams();
		Set<User> judges = hackathon.getJudges();
		Set<Organization> sponsors = hackathon.getSponsors();
		Set<Submission> submissions = hackathon.getSubmissions();
		List<Map<Object,Object>> teamDetails = new ArrayList<>();
		List<Map<Object,Object>> judgeDetails = new ArrayList<>();
		List<Map<Object,Object>> sponsorDetails = new ArrayList<>();
		List<Map<Object,Object>> submissionDetails = new ArrayList<>();
		for(Team team : teams) {
			Map<Object,Object> temp = new HashMap<>();
			temp.put("teamId", team.getId());
			temp.put("teamSize", team.getMembers().size());
			temp.put("teamName", team.getTeamName());
			teamDetails.add(temp);
			if(team.getMembers().contains(user)) {
				message="registered";
				userTeam = team;
			}
		}
		for(User judge : judges) {
			Map<Object,Object> temp = new HashMap<>();
			temp.put("judgeId",judge.getId());
			temp.put("judgeName",judge.getName());
			temp.put("judgeEmail",judge.getEmail());
			temp.put("judgeScreenName", judge.getScreenName());
			judgeDetails.add(temp);
			if(judge.getId() == user.getId()) {
				message="judge";
			}
		}
		for(Organization sponsor : sponsors) {
			Map<Object,Object> temp = new HashMap<>();
			temp.put("sponsorId",sponsor.getId());
			temp.put("sponsorName",sponsor.getName());
			temp.put("sponsorDescription",sponsor.getDescription());
			sponsorDetails.add(temp);
		}
		if(userTeam!=null) {
			for(Submission submission : submissions) {
				if(submission.getTeam().getId() == userTeam.getId()) {
					submissionUrl = submission.getURL();
				}
//				Map<Object,Object> temp = new HashMap<>();
//				temp.put("submissionId",submission.getId());
//				temp.put("url", submission.getURL());
//				temp.put("submittedBy",submission.getTeam().getTeamName());
//				temp.put("grade",submission.getGrade());
//				submissionDetails.add(temp);
			}
		}
		
		
		responseBody.put("teamDetails", teamDetails);
		responseBody.put("judgeDetails", judgeDetails);
		responseBody.put("sponsorDetails", sponsorDetails);
		responseBody.put("submissionUrl", submissionUrl);
		responseBody.put("message", message);
		return responseBody;
	}
	
	@RequestMapping(value="register/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<Object,Object> registerHackathon(HttpServletRequest request,
				HttpServletResponse response,
				@PathVariable(name="id") long hackathonId,
				@RequestBody Map<Object,Object> requestBody){
		
		Map<Object,Object> responseObject = new HashMap<>();
		String teamName = (String) requestBody.get("teamName");
		String idea = (String)requestBody.get("idea");
		List<Integer> userIds = (List<Integer>)requestBody.get("userIds");
		long leadId = new Long((String)requestBody.get("leadId"));
		
		Team team = new Team();
		team.setTeamName(teamName);
		team.setIdea(idea);
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		team.setParticipatedHackathon(hackathon);
		User teamLead = userDao.findUserbyID(leadId);
		team.setTeamLead(teamLead);		
		Set<User> members = new HashSet<>();
		List<String> userEmails = new ArrayList<>();
		for(int i=0;i<userIds.size();i++) { 
			User temp = userDao.findUserbyID(new Long(userIds.get(i)));
			members.add(temp);
			userEmails.add(temp.getEmail());
		}
		team.setMembers(members);
		try {
			Team createdTeam = teamDao.createTeam(team);
			responseObject.put("msg","Successfully registered");
			ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
	        emailExecutor.execute(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                    for(int i=0;i<userIds.size();i++) {
	                    	User temp = userDao.findUserbyID(userIds.get(i));
	                    	Payment payment = new Payment();
	                    	payment.setFee(hackathon.getFee());
	                    	payment.setTeamId(createdTeam.getId());
	                    	payment.setMemberId(userIds.get(i));
	                    	payment.setStatus(false);
	                    	Payment createdPayment = paymentDao.createPayment(payment);
	                    	sendPaymentEmail(temp.getEmail(), hackathon, teamLead, createdPayment.getId(),createdTeam.getId());
	                    }
	                } catch (Exception e) {
	                	System.out.println("error in sending mails: "+e.getMessage());
	                }
	            }
	        });
	        emailExecutor.shutdown();
			return responseObject;
		}catch (Exception e) {
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
			responseObject.put("msg",e.getMessage());
			return responseObject;
		}
		
	}
	
	public void sendPaymentEmail(String userEmail,Hackathon hackathon,User teamLead, long paymentId, long teamId) {
		
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
                        InternetAddress.parse(userEmail)
                );
                message.setSubject("Registered to Hackathon: Payment Required");
                message.setText("Dear "+userEmail+", "
                        + "\n\n Congratulations!!! You have been invited by ."+teamLead.getName()+"("+teamLead.getEmail()+") for the following hackathon event."
                        + "\n\n Hackathon Name: "+hackathon.getName()
                		+ "\n Hackathon Description: "+hackathon.getDescription()
                		+ "\n Hackathon Start Date: "+hackathon.getStartDate()
                		+ "\n Hackathon End Date: "+hackathon.getEndDate()
                		+ "\n Hackathon Fee: $"+hackathon.getFee()
                		+ "\n\n Go to http://localhost:3000/payment/"+paymentId+"/"+teamId+" for payment and confirm your seat."
                        +"\n\n Happy Hacking,"
                		+"\n OpenHack Service");

                Transport.send(message);
                System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}
}