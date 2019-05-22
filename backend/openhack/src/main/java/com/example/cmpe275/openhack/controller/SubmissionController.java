package com.example.cmpe275.openhack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.entity.Team;
import com.example.cmpe275.openhack.entity.User;
import com.example.cmpe275.openhack.service.HackathonRepositoryService;
import com.example.cmpe275.openhack.service.OrganizationRepositoryService;
import com.example.cmpe275.openhack.service.PaymentRepositoryService;
import com.example.cmpe275.openhack.service.RequestRepositoryService;
import com.example.cmpe275.openhack.service.SubmissionRepositoryService;
import com.example.cmpe275.openhack.service.TeamRepositoryService;
import com.example.cmpe275.openhack.service.UserRepositoryService;

@RestController
public class SubmissionController {
//	private HackathonDao hackathonDao;
//	private SubmissionDao submissionDao;
//	private TeamDao teamDao;

	
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
	
	public SubmissionController() {
//		hackathonDao = new HackathonDaoImpl();
//		submissionDao = new SubmissionDaoImpl();
//		teamDao = new TeamDaoImpl();
	}

	@PostMapping("/addSubmission")
	@ResponseBody
	public Map<Object,Object> addSubmission(@RequestBody HashMap<String, String> map,HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("\naddSubmission method called for the Submission");
		System.out.println("Submission data from post " + map);
		Map<Object,Object> responseBody = new HashMap<>();
		Submission submission = new Submission();

		try {
			// Check if Team Id and Hackathon Id is already Present in the Submission table. If it is present then the hacker is resubmitting the url. If not present then the hacker is making a new submission
			Long hackathonId = new Long((String) map.get("hackathonId"));
			Hackathon hackathon = hackathonDao.findById(hackathonId);
			Long teamId = new Long((String) map.get("teamId"));
			Team team = teamDao.getTeamById(teamId);
			Submission searchedSubmission = submissionDao.findByTeamIdAndHackathonId(teamId, hackathonId);
			if(searchedSubmission==null) {
				submission.setURL(map.get("url"));
				submission.setHackathon(hackathon);
				submission.setTeam(team);
				submissionDao.create(submission);
				team.setSubmitted(true);
				teamDao.updateTeam(team);
				responseBody.put("msg", "submitted");
				return responseBody;
			}else {
				searchedSubmission.setURL(map.get("url"));
				submissionDao.update(searchedSubmission);
				responseBody.put("msg", "submitted");
				return responseBody;
			}
		} catch (Exception e) {
			System.out.println("Exception while creating/updating submission: " + e);
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
			responseBody.put("msg",e);
			return responseBody;
		}
		
	}

	@PostMapping("/gradeSubmission/{submissionId}")
	@ResponseBody
	
	public Map<Object,Object> gradeSubmission(@RequestBody HashMap<String, String> map,
			@PathVariable long submissionId,
			HttpServletResponse response,
			HttpServletRequest request) {
		System.out.println("\ngradeSubmission method called for the Submission");
		System.out.println("Submission data from post " + map);
		Map<Object,Object> responseBody = new HashMap<>();
		try {
			
			Submission submission = submissionDao.findById(submissionId);
			submission.setGrade(Float.parseFloat((String)map.get("grade")));
			Submission updatedSubmission = submissionDao.update(submission);
			Team team = updatedSubmission.getTeam();
			team.setGraded(true);
			teamDao.updateTeam(team);
			responseBody.put("msg", "graded successfully");
			return responseBody;
//			Long hackathonId = new Long((String) map.get("hackathonId"));
//			Hackathon hackathon = hackathonDao.findById(hackathonId);
//			Set<Submission> allSubmissions = hackathon.getSubmissions();
//			Long teamId = new Long((String) map.get("teamId"));
//			System.out.println("Hackathon found:" + hackathon.getName());
//
//			for (Submission currentSubmission : allSubmissions) {
//				if (currentSubmission.getTeam().getId() == teamId
//						&& currentSubmission.getHackathon().getId() == hackathonId) {
//					System.out.println("Submission found! Initial Grade: " + currentSubmission.getGrade());
//					Float grade = new Float((String) map.get("grade"));
//					currentSubmission.setGrade(grade);
//					submission = submissionDao.updateById(currentSubmission.getId(), currentSubmission);
//					System.out.println("Resubmission successful! New Grade: " + currentSubmission.getGrade()
//					+ " submissionId: " + currentSubmission.getId());
//				}
//			}

		} catch (Exception e) {
			System.out.println("Exception while creating/updating submission: " + e);
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
			responseBody.put("msg",e);
			return responseBody;
		}
	}


	@GetMapping("/submission/{hackathonId}")
	@ResponseBody
	
	public Map<Object,Object> getAllSubmissions(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(name="hackathonId") long hackathonId){
		Map<Object,Object> responseBody = new HashMap<>();
		List<Map<Object,Object>> submissionDetails = new ArrayList<>();
		try {
			List<Submission> submissions = submissionDao.findAll();
			for (Submission submission:submissions) {
				if(submission.getHackathon().getId() == hackathonId) {
					Map<Object,Object> submissionTemp = new HashMap<>();
					submissionTemp.put("submissionId",submission.getId());
					submissionTemp.put("submissionUrl",submission.getURL());
					submissionTemp.put("grade", submission.getGrade());
					submissionTemp.put("hackathonName", submission.getHackathon().getName());
					submissionTemp.put("teamName", submission.getTeam().getTeamName());
					List<Map<Object,Object>> memberDetails = new ArrayList<>();
							for(User member : submission.getTeam().getMembers()) {
								Map<Object,Object> teamMembers = new HashMap<>();
								teamMembers.put("memberId", member.getId());
								teamMembers.put("memberName", member.getName());
								memberDetails.add(teamMembers);
							}
					submissionTemp.put("memberDetails", memberDetails);
					submissionDetails.add(submissionTemp);
				}	
			}
			responseBody.put("submissionDetails", submissionDetails);
			return responseBody;
		}catch(Exception e){
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR  );
			responseBody.put("msg",e);
			return responseBody;
		}

	}
}

/*Long hackathonId = new Long((String) map.get("hackathonId"));
Hackathon hackathon = hackathonDao.findById(hackathonId);
Long teamId = new Long((String) map.get("teamId"));
Team team = teamDao.getTeamById(teamId);
submission.setURL(map.get("url"));
submission.setHackathon(hackathon);
submission.setTeam(team);
Submission addedSubmission = submissionDao.create(submission);*/