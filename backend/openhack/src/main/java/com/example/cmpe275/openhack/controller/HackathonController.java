package com.example.cmpe275.openhack.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.cmpe275.openhack.controller.helpers.HackathonResultsTeam;
import com.example.cmpe275.openhack.entity.Expense;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Organization;
import com.example.cmpe275.openhack.entity.Payment;
import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.entity.Team;
import com.example.cmpe275.openhack.entity.User;
import com.example.cmpe275.openhack.repository.HackathonRepository;
import com.example.cmpe275.openhack.service.ExpenseRepositoryService;
import com.example.cmpe275.openhack.service.HackathonRepositoryService;
import com.example.cmpe275.openhack.service.OrganizationRepositoryService;
import com.example.cmpe275.openhack.service.PaymentRepositoryService;
import com.example.cmpe275.openhack.service.RequestRepositoryService;
import com.example.cmpe275.openhack.service.SubmissionRepositoryService;
import com.example.cmpe275.openhack.service.TeamRepositoryService;
import com.example.cmpe275.openhack.service.UserRepositoryService;

@RequestMapping("/hackathon")
@Controller
public class HackathonController {

	
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
	@Autowired 
	ExpenseRepositoryService expenseDao;
	
	public HackathonController() {
		// TODO Auto-generated constructor stub
//		userDao = new UserDaoImpl();
//		hackathonDao = new HackathonDaoImpl();
//		organizationDao = new OrganizationDaoImpl();
//		teamDao = new TeamDaoImpl();
//		paymentDao = new PaymentDaoImpl();
	}
	// @Autowired
	// UserDaoImpl userDao;
	// HackathonDaoImpl hackathonDao;
	// OrganizationDaoImpl organizationDao;
	// TeamDaoImpl teamDao;
	// PaymentDaoImpl paymentDao;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	
	public Map<Object, Object> createHackathon(@RequestBody HashMap<Object, Object> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("\n Hackathon to be created");
		Map<Object, Object> responseObject = new HashMap<>();
		String name = (String) map.get("name");
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse((String) map.get("startDate"));
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse((String) map.get("endDate"));
		String description = (String) map.get("description");
		int teamSizeMin = (int) map.get("teamSizeMin");
		int teamSizeMax = (int) map.get("teamSizeMax");
		double fee = new Double((String) map.get("fee"));
		List<Integer> judgesId = (List<Integer>) map.get("judgesId");
		Hackathon hackathon = new Hackathon();
		hackathon.setName(name);
		hackathon.setDescription(description);
		hackathon.setStartDate(startDate);
		hackathon.setEndDate(endDate);
		hackathon.setTeamSizeMin(teamSizeMin);
		hackathon.setTeamSizeMax(teamSizeMax);
		hackathon.setFee(fee);
		if (map.get("discount") != null) {
			double discount = new Double((String) map.get("discount"));
			hackathon.setDiscount(discount);
		} else {
			hackathon.setDiscount(new Double(0));
		}
		Set<User> judges = new HashSet<>();
		for (int i = 0; i < judgesId.size(); i++) {
			User temp = userDao.findUserbyID(judgesId.get(i));
			judges.add(temp);
		}
		hackathon.setJudges(judges);
		if (map.get("sponsorsId") != null) {
			List<Integer> sponsorsId = (List<Integer>) map.get("sponsorsId");
			Set<Organization> sponsors = new HashSet<>();
			for (int i = 0; i < sponsorsId.size(); i++) {
				Organization temp = organizationDao.findOrganizationById(sponsorsId.get(i));
				sponsors.add(temp);
			}
			hackathon.setSponsors(sponsors);
		}
		try {
			hackathonDao.create(hackathon);
			System.out.println("Successfully created");
			responseObject.put("msg", "Successfully created");
		} catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseObject.put("msg", e.getMessage());
		}
		return responseObject;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	@ResponseBody
	
	public Map<Object, Object> getHackathonDetails(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<Object, Object> map, @PathVariable(name = "id") long hackathonId) {
		System.out.print("Getting hackathon details");
		long userId = new Long((String) map.get("userId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		User user = userDao.findUserbyID(userId);
		return createHackathonResponseBody(hackathon, user);
	}

	public Map<Object, Object> createHackathonResponseBody(Hackathon hackathon, User user) {
		System.out.println("\n\ncreateHackathonResponseBody for user : " + user.toString());
		Map<Object, Object> responseBody = new HashMap<>();
		String message = "";
		boolean isFinalize = false;
		Team userTeam = null;
		String submissionUrl = "";
		String winnerTeam = "";
		responseBody.put("id", hackathon.getId());
		responseBody.put("name", hackathon.getName());
		responseBody.put("description", hackathon.getDescription());
		responseBody.put("startDate", hackathon.getStartDate());
		responseBody.put("endDate", hackathon.getEndDate());
		responseBody.put("fee", hackathon.getFee());
		responseBody.put("teamSizeMin", hackathon.getTeamSizeMin());
		responseBody.put("teamSizeMax", hackathon.getTeamSizeMax());
		responseBody.put("discount", hackathon.getDiscount());
		Set<Team> teams = hackathon.getTeams();
		Set<User> judges = hackathon.getJudges();
		Set<Organization> sponsors = hackathon.getSponsors();
		Set<Submission> submissions = hackathon.getSubmissions();
		List<Map<Object, Object>> teamDetails = new ArrayList<>();
		List<Map<Object, Object>> judgeDetails = new ArrayList<>();
		List<Map<Object, Object>> sponsorDetails = new ArrayList<>();
		List<Map<Object, Object>> submissionDetails = new ArrayList<>();
		for (Team team : teams) {
			Map<Object, Object> temp = new HashMap<>();
			temp.put("teamId", team.getId());
			temp.put("teamSize", team.getMembers().size());
			temp.put("teamName", team.getTeamName());
			teamDetails.add(temp);
			if (team.getMembers().contains(user)) {
				userTeam = team;
				if (team.getPaymentStatus()) {
					message = "registered";
				} else {
					message = "payment pending";
				}

			}
		}
		for (User judge : judges) {
			Map<Object, Object> temp = new HashMap<>();
			temp.put("judgeId", judge.getId());
			temp.put("judgeName", judge.getName());
			temp.put("judgeEmail", judge.getEmail());
			temp.put("judgeScreenName", judge.getScreenName());
			judgeDetails.add(temp);
			if (judge.getId() == user.getId()) {
				message = "judge";
			}
		}
		for (Organization sponsor : sponsors) {
			Map<Object, Object> temp = new HashMap<>();
			temp.put("sponsorId", sponsor.getId());
			temp.put("sponsorName", sponsor.getName());
			temp.put("sponsorDescription", sponsor.getDescription());
			sponsorDetails.add(temp);
		}
		if (userTeam != null) {
			List<Map<Object, Object>> userTeamDetails = new ArrayList<>();
			for (User member : userTeam.getMembers()) {
				Map<Object, Object> temp = new HashMap<>();
				temp.put("name", member.getName());
				temp.put("title", member.getTitle());
				userTeamDetails.add(temp);
				System.out.println(temp);
			}
			responseBody.put("userTeamId", userTeam.getId());
			responseBody.put("userTeam", userTeamDetails);
			for (Submission submission : submissions) {
				if (submission.getTeam().getId() == userTeam.getId()) {
					submissionUrl = submission.getURL();
				}
			}
		}
		if (user.getUsertype().equals("admin")) {
			message = "admin";
		}

		if (hackathon.getIsFinalized()) {
			isFinalize = true;
			if (hackathon.getWinner() != null)
				winnerTeam = hackathon.getWinner().getTeamName();

		}
		responseBody.put("teamDetails", teamDetails);
		responseBody.put("judgeDetails", judgeDetails);
		responseBody.put("sponsorDetails", sponsorDetails);
		responseBody.put("submissionUrl", submissionUrl);
		responseBody.put("message", message);
		responseBody.put("isFinalize", isFinalize);
		responseBody.put("winnerTeam", winnerTeam);
		return responseBody;
	}

	@RequestMapping(value = "register/{id}", method = RequestMethod.POST)
	@ResponseBody
	
	public Map<Object, Object> registerHackathon(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "id") long hackathonId, @RequestBody Map<Object, Object> requestBody) {

		Map<Object, Object> responseObject = new HashMap<>();
		String teamName = (String) requestBody.get("teamName");
		String idea = (String) requestBody.get("idea");
		final List<Integer> userIds = (List<Integer>) requestBody.get("userIds");
		long leadId = new Long((String) requestBody.get("leadId"));

		Team team = new Team();
		team.setTeamName(teamName);
		team.setIdea(idea);
		team.setGraded(false);
		team.setSubmitted(false);
		team.setPaymentStatus(false);
		final Hackathon hackathon = hackathonDao.findById(hackathonId);
		team.setParticipatedHackathon(hackathon);
		final User teamLead = userDao.findUserbyID(leadId);
		team.setTeamLead(teamLead);
		Set<User> members = new HashSet<>();
		List<String> userEmails = new ArrayList<>();
		for (int i = 0; i < userIds.size(); i++) {
			User temp = userDao.findUserbyID(new Long(userIds.get(i)));
			members.add(temp);
			userEmails.add(temp.getEmail());
		}
		team.setMembers(members);
		try {
			final Team createdTeam = teamDao.createTeam(team);
			responseObject.put("msg", "Successfully registered");
//			ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
//			emailExecutor.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
						for (int i = 0; i < userIds.size(); i++) {
							boolean isSponsor = false;
							User temp = userDao.findUserbyID(userIds.get(i));
							Set<Organization> sponsors = hackathon.getSponsors();
							for (Organization sponsor : sponsors) {
								if (sponsor.getMembers().contains(userDao.findUserbyID(userIds.get(i)))) {
									isSponsor = true;
								}
							}
							Payment payment = new Payment();
							if (!isSponsor) {
								payment.setFee(hackathon.getFee());
							} else {
								payment.setFee(hackathon.getFee() * (1 - (hackathon.getDiscount() / 100)));
							}
							payment.setTeamId(createdTeam.getId());
							payment.setMemberId(userIds.get(i));
							payment.setStatus(false);
							System.out.println("Payment is to be created");
							Payment createdPayment = paymentDao.createPayment(payment);
							System.out.println("Payment created");
							sendPaymentEmail(temp.getEmail(), hackathon, teamLead, createdPayment.getId(),
									createdTeam.getId());
						}
//					} catch (Exception e) {
//						System.out.println("error encountered: " + e.getMessage());
//					}
//				}
//			});
//			emailExecutor.shutdown();
			return responseObject;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseObject.put("msg", "Team Name already taken");
			return responseObject;
		}

	}

	@RequestMapping(value = "/getAllHackathons/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAllHackathons(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "userId") long userId) {

		Map<Object, Object> responseBody = new HashMap<>();
		try {
			System.out.println("Before finding");
			List<Hackathon> hackathons = hackathonDao.findAll();
			System.out.println("hackathons are: "+hackathons.size());
			System.out.println("After finding");
			User user = userDao.findUserbyID(userId);
			List<Map<Object, Object>> hackathonDetails = new ArrayList<>();
			for (Hackathon hackathon : hackathons) {
				hackathonDetails.add(createSmallHackathonResponseBody(hackathon, user));
			}
			responseBody.put("hackathonDetails", hackathonDetails);
			return responseBody;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseBody.put("msg", e);
			return responseBody;
		}
	}

	public void sendPaymentEmail(String userEmail, Hackathon hackathon, User teamLead, long paymentId, long teamId) {

		final String username = "openhackservice@gmail.com";
		final String password = "openhack123";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
			message.setSubject("Registered to Hackathon: Payment Required");
			message.setText("Dear " + userEmail + ", " + "\n\n Congratulations!!! You have been invited by ."
					+ teamLead.getName() + "(" + teamLead.getEmail() + ") for the following hackathon event."
					+ "\n\n Hackathon Name: " + hackathon.getName() + "\n Hackathon Description: "
					+ hackathon.getDescription() + "\n Hackathon Start Date: " + hackathon.getStartDate()
					+ "\n Hackathon End Date: " + hackathon.getEndDate() + "\n Hackathon Fee: $" + hackathon.getFee()
					+ "\n\n Go to http://3.86.236.128:3000/hackathon/payment/" + paymentId
					+ " for payment and confirm your seat." + "\n\n Happy Hacking," + "\n OpenHack Service");

			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public Map<Object, Object> createSmallHackathonResponseBody(Hackathon hackathon, User user) {
		Map<Object, Object> responseBody = new HashMap<>();
		String message = "";
		responseBody.put("id", hackathon.getId());
		responseBody.put("name", hackathon.getName());
		responseBody.put("description", hackathon.getDescription());
		responseBody.put("startDate", hackathon.getStartDate());
		responseBody.put("endDate", hackathon.getEndDate());
		responseBody.put("fee", hackathon.getFee());
		responseBody.put("teamSizeMin", hackathon.getTeamSizeMin());
		responseBody.put("teamSizeMax", hackathon.getTeamSizeMax());
		responseBody.put("discount", hackathon.getDiscount());
		if (hackathon.getJudges().contains(user)) {
			message = "judge";
		}
		responseBody.put("message", message);
		return responseBody;
	}

	@RequestMapping(value = "open", method = RequestMethod.POST, produces = { "application/json" }, consumes = {
			"application/JSON" })
	@ResponseBody
	
	public Hackathon openHackathon(HttpServletRequest request, HttpServletResponse response,
			@RequestBody HashMap<Object, Object> map) throws ParseException {
		System.out.println("POST /hackathon/open - Open hackathon - Request Body: " + map);
		Long hackathonId = new Long((String) map.get("hackathonId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		System.out.println("Hackathon name:" + hackathon.getName());
		System.out.println(
				"\n - - - - - Priting the current date obtained from the frontend" + map.get("currentDate") + "\n");
		long new_miliseconds = Long.parseLong((String) String.valueOf(map.get("currentDate")));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new_miliseconds);
		System.out.println();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date final_new_date = formatter.parse(formatter.format(cal.getTime()));
		System.out.println(" <<<<<<<<<<<<<<<<<<< The new date being set is " + final_new_date);
		hackathon.setStartDate(final_new_date);
		try {
			Hackathon updatedHackathon = hackathonDao.updateById(hackathonId, hackathon);
			System.out.println("New start date: " + updatedHackathon.getStartDate());
			return null;
		} catch (Exception e) {
			System.out.println("Some EXCEPTION");
		}

		return null;
	}

	@RequestMapping(value = "close", method = RequestMethod.POST, produces = { "application/json" }, consumes = {
			"application/JSON" })
	@ResponseBody
	
	public Hackathon closeHackathon(HttpServletRequest request, HttpServletResponse response,
			@RequestBody HashMap<Object, Object> map) throws ParseException {
		System.out.println("POST /hackathon/close - Close hackathon - Request Body: " + map);
		Long hackathonId = new Long((String) map.get("hackathonId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		System.out.println("Hackathon name:" + hackathon.getName());

		System.out.println(
				"\n - - - - - Priting the current date obtained from the frontend" + map.get("currentDate") + "\n");
		long new_miliseconds = Long.parseLong((String) String.valueOf(map.get("currentDate")));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new_miliseconds);
		System.out.println();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date final_new_date = formatter.parse(formatter.format(cal.getTime()));

		// Check whether submissions for all teams have been received. If all
		// submissions have not been received then cannot close hackathon before the
		// original end date
		Set<Team> allTeams = hackathon.getTeams();
		for (Team currentTeam : allTeams) {
			if (currentTeam.getSubmitted() == false) {
				System.out.println("No submission found for team: " + currentTeam.getId()
						+ ", therefore cannot close hackathon before the original end date!");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Throw 400
				return null;
			}
		}

		// If all submissions have been received
		hackathon.setEndDate(final_new_date);

		try {
			Hackathon updatedHackathon = hackathonDao.updateById(hackathonId, hackathon);
			System.out.println("New end date: " + updatedHackathon.getEndDate());
			return null;
		} catch (Exception e) {
			System.out.println("Some EXCEPTION");
		}

		return null;
	}

	@RequestMapping(value = "/finalize", method = RequestMethod.POST, produces = { "application/json" }, consumes = {
			"application/JSON" })
	@ResponseBody
	
	public Map<Object, Object> finalizeHackathon(HttpServletRequest request, HttpServletResponse response,
			@RequestBody HashMap<Object, Object> map) {
		System.out.println("\nPOST /hackathon/finalize - Finalize hackathon - Request Body: " + map);
		Long hackathonId = new Long((String) map.get("hackathonId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		Set<Submission> allSubmissions = hackathon.getSubmissions();
		Map<Object, Object> responseBody = new HashMap<>();
		System.out.println("Hackathon name:" + hackathon.getName());
		// TODO Check whether grades for all teams have been assigned. If all grades
		// have not been assigned then cannot finalize hackathon
		Set<Team> allTeams = hackathon.getTeams();
		Team winner = null; // TODO check this new
		Float highestGrade = (float) 0;
		for (Team currentTeam : allTeams) {
			if (currentTeam.getGraded() == false) { // TODO add getGraded() and setGraded() methods and graded boolean
													// flag
				System.out.println("No grade found for team: " + currentTeam.getId() + ". Cannot finalize hackathon!");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Throw 400
				return responseBody;
			} else {
				for (Submission submission : allSubmissions) {
					if (currentTeam == submission.getTeam() && highestGrade <= (float) submission.getGrade()) {
						// TODO check for tie? would have to remove equality as well in (highestGrade <=
						// currentSubmission.getGrade())
						highestGrade = submission.getGrade();
						winner = currentTeam;
						hackathon.setWinner(currentTeam);
						hackathon.setIsFinalized(true);
					}
				}
			}
		}
		try {
			hackathonDao.updateById(hackathonId, hackathon);
			sendFinalizeMail(hackathon,hackathon.getJudges(),hackathon.getTeams(),winner);
			responseBody.put("msg", "Finalized");
			responseBody.put("winner", winner.getTeamName());
			System.out.println("Winner Team: " + winner);
			System.out.println(" - - - - - - - Returning : " + winner.getTeamName());
			return responseBody;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Throw 500
			responseBody.put("msg", e);
			return responseBody;
		}
	}
	
	
	@RequestMapping(value = "/results", method = RequestMethod.POST, produces = { "application/json" }, consumes = {
	"application/JSON" })
	@ResponseBody
	
	public Map<Object, Object> hackathonResults(HttpServletRequest request, HttpServletResponse response,
		@RequestBody HashMap<Object, Object> map) {
		System.out.println("\nPOST /hackathon/results - Hackathon Results - Request Body: " + map);
		Long hackathonId = new Long((String) map.get("hackathonId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		Set<Submission> allSubmissions = hackathon.getSubmissions();
		Map<Object, Object> responseBody = new HashMap<>();
		System.out.println("Hackathon name:" + hackathon.getName());
		// TODO Return the TeamName, Members, and Grade (if assigned), of all the teams that have participated in the hackathon. The sorting according to the grades is to be done at the frontend
		Set<Team> allTeams = hackathon.getTeams();
		ArrayList<HackathonResultsTeam> resultsData = new ArrayList<>(); // HackathonResultsTeam is the class containing the TeamName, Members, and Grade for a particular team
		
		for (Team currentTeam : allTeams) {
			if (currentTeam.getGraded() == true) { // If a grade is assigned then only consider this team to be included into resultsData
				HackathonResultsTeam newHackathonResultsTeam = new HackathonResultsTeam();
				newHackathonResultsTeam.setTeamName(currentTeam.getTeamName());
				ArrayList<String> teamMembers = new ArrayList<>();
				for(User currentUser : currentTeam.getMembers()) { // All team member names for this team
					teamMembers.add(currentUser.getName());
				}
				newHackathonResultsTeam.setTeamMembers(teamMembers);
				for (Submission submission : allSubmissions) {
					if (currentTeam == submission.getTeam()) {
						// TODO grade for this team
						newHackathonResultsTeam.setGrade(submission.getGrade());
					}
				}	
				resultsData.add(newHackathonResultsTeam);
			}
		}
		
		try {
			responseBody.put("hackathonResults", resultsData);
			return responseBody;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Throw 500
			responseBody.put("msg", e);
			return responseBody;
		}
	}
	
	
	
	
	public void sendInviteEmail(String inviteEmail, Hackathon hackathon) {

		final String username = "openhackservice@gmail.com";
		final String password = "openhack123";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(inviteEmail));
			message.setSubject("OpenHack Invitation - You have been invited to participate!");
			message.setText("Dear " + inviteEmail + "," + "\n\nYou are invited to participate in the following hackathon event: "
					+ "\n\n Hackathon Name: " + hackathon.getName() + "\n Hackathon Description: "
					+ hackathon.getDescription() + "\n\nPlease go to http://3.86.236.128:3000/hackathon_details/" + hackathon.getId() + "/"
					+ " , and register as a new user. Then you can use the same link to participate in the above hackathon. You can also see all the on-going hackathons on the homepage, after you have successfully signed up." + "\n\n Happy Hacking," + "\n OpenHack Service");

			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@RequestMapping(value = "/invite", method = RequestMethod.POST, produces = { "application/json" }, consumes = {
	"application/JSON" })
	@ResponseBody
	public Map<Object, Object> hackathonInvite(HttpServletRequest request, HttpServletResponse response,
		@RequestBody HashMap<Object, Object> map) {
		System.out.println("\nPOST /hackathon/invite - Send Invitation - Request Body: " + map);
		Map<Object, Object> responseBody = new HashMap<>();
		Long hackathonId = new Long((String) map.get("hackathonId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		String inviteEmail = (String) map.get("inviteEmail");

		List<User> allUsers = userDao.findAllUsers();
		// Check if invite email is already registered
		for (User currentUser : allUsers) {
			if (currentUser.getEmail().toString().equals(inviteEmail)) { // Checking equality in string in Java - https://www.geeksforgeeks.org/compare-two-strings-in-java/
				System.out.println("Invite email is already registered: " + currentUser.getEmail() + ". Cannot send invitation!");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Throw 400
				responseBody.put("msg", "Invite email is already registered. Cannot send invitation!");
				return responseBody;
			}
		}
		
		// If invite email is not registered, then send the invite
		try {
			sendInviteEmail(inviteEmail, hackathon);
			responseBody.put("msg", "Success");
			return responseBody;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Throw 500
			responseBody.put("msg", e);
			return responseBody;
		}
	}
	
	public void sendFinalizeMail(Hackathon hackathon,Set<User> judges, Set<Team> teams,Team winner) {
		for(User judge:judges) {
			final String username = "openhackservice@gmail.com";
			final String password = "openhack123";

			Properties prop = new Properties();
			prop.put("mail.smtp.host", "smtp.gmail.com");
			prop.put("mail.smtp.port", "587");
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.starttls.enable", "true"); // TLS

			Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(judge.getEmail()));
				message.setSubject(""+hackathon.getName()+" Hackathon Finalized");
				message.setText("Dear " + judge.getName() + ", " + "\n\n The Hackathon event: "+hackathon.getName()+"has been finalized."
						+"\n\n Thank you for being the judge and wish to see ou judge many more hackathons."
						+ "\n\n Hackathon Name: " + hackathon.getName() + "\n Hackathon Description: "
						+ hackathon.getDescription() + "\n Hackathon Start Date: " + hackathon.getStartDate()
						+ "\n Hackathon End Date: " + hackathon.getEndDate() + "\n Hackathon Fee: $" + hackathon.getFee()
						+ "\n\n Go to http://3.86.236.128:3000/hackathon_details/" + hackathon.getId()+ "/results to view the results" 
						+ "\n\n Happy Hacking," + "\n OpenHack Service");
				Transport.send(message);
				System.out.println("Done");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		for(Team team:teams) {
			if(team.getId() == winner.getId()) {
				Set<User> members = team.getMembers();
				for(User member: members) {
					final String username = "openhackservice@gmail.com";
					final String password = "openhack123";

					Properties prop = new Properties();
					prop.put("mail.smtp.host", "smtp.gmail.com");
					prop.put("mail.smtp.port", "587");
					prop.put("mail.smtp.auth", "true");
					prop.put("mail.smtp.starttls.enable", "true"); // TLS

					Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					});

					try {

						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(username));
						message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(member.getEmail()));
						message.setSubject("Congratulations: "+hackathon.getName()+" Hackathon Winner");
						message.setText("Dear " + member.getName() + ", " + "\n\n The Hackathon event: "+hackathon.getName()+"has been finalized."
								+"\n\n Congratulations on winning the hackathon. Wish to see you participate in our future events as well."
								+ "\n\n Hackathon Name: " + hackathon.getName() + "\n Hackathon Description: "
								+ hackathon.getDescription() + "\n Hackathon Start Date: " + hackathon.getStartDate()
								+ "\n Hackathon End Date: " + hackathon.getEndDate()
								+ "\n\n Go to http://3.86.236.128:3000/hackathon_details/" + hackathon.getId()+ "/results to view the results" 
								+ "\n\n Happy Hacking," + "\n OpenHack Service");
						Transport.send(message);
						System.out.println("Done");
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}else {
				for(User member:team.getMembers()) {
					final String username = "openhackservice@gmail.com";
					final String password = "openhack123";

					Properties prop = new Properties();
					prop.put("mail.smtp.host", "smtp.gmail.com");
					prop.put("mail.smtp.port", "587");
					prop.put("mail.smtp.auth", "true");
					prop.put("mail.smtp.starttls.enable", "true"); // TLS

					Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					});

					try {
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(username));
						message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(member.getEmail()));
						message.setSubject(""+hackathon.getName()+" Hackathon Finalized");
						message.setText("Dear " + member.getName() + ", " + "\n\n The Hackathon event: "+hackathon.getName()+"has been finalized."
								+"\n\n Thank you for being the participant and wish to see you participate in many more hackathons."
								+ "\n\n Hackathon Name: " + hackathon.getName() + "\n Hackathon Description: "
								+ hackathon.getDescription() + "\n Hackathon Start Date: " + hackathon.getStartDate()
								+ "\n Hackathon End Date: " + hackathon.getEndDate() + "\n Hackathon Fee: $" + hackathon.getFee()
								+ "\n\n Go to http://3.86.236.128:3000/hackathon_details/" + hackathon.getId()+ "/results to view the results" 
								+ "\n\n Happy Hacking," + "\n OpenHack Service");
						Transport.send(message);
						System.out.println("Done");
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
	}

	@RequestMapping(value = "/addExpense", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addHackathon(@RequestBody HashMap<Object, Object> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("\n Expense to be created");
		Map<Object, Object> responseObject = new HashMap<>();
		String title = (String) map.get("title");
		long new_miliseconds = Long.parseLong((String) String.valueOf(map.get("time")));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new_miliseconds);
		System.out.println();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date final_new_date = formatter.parse(formatter.format(cal.getTime()));
		
//		Date time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse((String) map.get("time"));
		String description = (String) map.get("description");
		Float amount = Float.parseFloat((String) map.get("amount"));
		Long hackathonId = new Long((String) map.get("hackathonId"));
		Hackathon hackathon = hackathonDao.findById(hackathonId);
		Expense expense = new Expense();
		expense.setTitle(title);
		expense.setDescription(description);
		expense.setTime(final_new_date);
		expense.setAmount(amount);
		expense.setHackathon(hackathon);
		try {
			expenseDao.create(expense);
			System.out.println("Successfully created");
			responseObject.put("msg", "Successfully created");
		} catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseObject.put("msg", e.getMessage());
		}
		return responseObject;
	}
	@RequestMapping(value = "/expenseDetails/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getExpenseDetails(HttpServletRequest request,HttpServletResponse response
			,@PathVariable(name = "id") long hackathonId) throws Exception {
		System.out.println("\nAll Expenses for Hackathon");
		
		Map<Object, Object> responseBody = new HashMap<>();
		try {
			System.out.println("Before finding");
			List<Expense> expenses = expenseDao.findExpenseByHackathonId(hackathonId);
			System.out.println("hackathons are: "+expenses.size());
			System.out.println("After finding");
			List<Map<Object,Object>> expenseDetails=new ArrayList<>();
			float totalExpense=0;
			for (Expense expense : expenses) {
				Map<Object,Object> temp = new HashMap<>();
				temp.put("title",expense.getTitle());
				temp.put("description", expense.getDescription());
				temp.put("amount",expense.getAmount());
				temp.put("time", expense.getTime());
				expenseDetails.add(temp);
				totalExpense=totalExpense+expense.getAmount();
			}
			System.out.println("totalExpense"+totalExpense);
			responseBody.put("totalExpense", totalExpense);
			responseBody.put("expenseDetails", expenseDetails);
			return responseBody;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseBody.put("msg", e);
			return responseBody;
		}		
	}
}
