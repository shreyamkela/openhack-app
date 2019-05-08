package com.example.cmpe275.openhack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cmpe275.openhack.dao.HackathonDao;
import com.example.cmpe275.openhack.dao.HackathonDaoImpl;
import com.example.cmpe275.openhack.dao.SubmissionDao;
import com.example.cmpe275.openhack.dao.SubmissionDaoImpl;
import com.example.cmpe275.openhack.dao.TeamDao;
import com.example.cmpe275.openhack.dao.TeamDaoImpl;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.entity.Team;
import com.example.cmpe275.openhack.entity.User;

@RestController
public class SubmissionController {
	private HackathonDao hackathonDao;
	private SubmissionDao submissionDao;
	private TeamDao teamDao;

	public SubmissionController() {
		hackathonDao = new HackathonDaoImpl();
		submissionDao = new SubmissionDaoImpl();
		teamDao = new TeamDaoImpl();
	}
//	@Autowired
//	HackathonDaoImpl hackathonDao;
//	SubmissionDaoImpl submissionDao;

	@PostMapping("/addSubmission")
	@ResponseBody
	public Long addSubmission(@RequestBody HashMap<String, String> map) {
		System.out.println("\naddSubmission method called for the Submission");
		System.out.println("Submission data from post " + map);
		Submission submission = new Submission();

		try {
			// Check if Team Id and Hackathon Id is already Present in the Submission table. If it is present then the hacker is resubmitting the url. If not present then the hacker is making a new submission
			boolean newSubmission = true;
			Long hackathonId = new Long((String) map.get("hackathonId"));
			Hackathon hackathon = hackathonDao.findById(hackathonId);
			Set<Submission> allSubmissions = hackathon.getSubmissions();
			Long teamId = new Long((String) map.get("teamId"));

			System.out.println("Hackathon found:" + hackathon.getName());

			// Checking if this is resubmission
			for (Submission currentSubmission : allSubmissions) {
				if (currentSubmission.getTeam().getId() == teamId
						&& currentSubmission.getHackathon().getId() == hackathonId) {
					newSubmission = false;
					System.out.println("Submission found! Old URL: " + currentSubmission.getURL());
					currentSubmission.setURL(map.get("url"));
					submission = submissionDao.updateById(currentSubmission.getId(), currentSubmission);
					System.out.println("Resubmission successful! New URL, submissionId: " + currentSubmission.getURL()
					+ currentSubmission.getId());
				}
			}

			// If this is a new submission
			if (newSubmission == true) {
				submission.setURL(map.get("url"));
				submission.setHackathon(hackathon);
				Set<Team> teams = hackathon.getTeams();
				for (Team team : teams) {
					if (team.getId() == teamId) {
						System.out.println("Team found:" + team.getTeamName());
						submission.setTeam(team);
						team.setSubmitted(true);
						team.setGraded(false);
						Team updatedTeam = teamDao.updateTeam(team);
						
					}
				}
				submission = submissionDao.create(submission);
				System.out.println(
						"New submission successful! URL, submissionId: " + submission.getURL() + submission.getId());
			}

		} catch (Exception e) {
			System.out.println("Exception while creating/updating submission: " + e);
		}
		return submission.getId();
	}

	@PostMapping("/gradeSubmission/{submissionId}")
	@ResponseBody
	public Long gradeSubmission(@RequestBody HashMap<String, String> map,
			@PathVariable long submissionId) {
		System.out.println("\ngradeSubmission method called for the Submission");
		System.out.println("Submission data from post " + map);

		try {
			
			Submission submission = submissionDao.findById(submissionId);
			submission.setGrade(Float.parseFloat((String)map.get("grade")));
			Submission updatedSubmission = submissionDao.updateById(submissionId, submission);
			Team team = updatedSubmission.getTeam();
			team.setGraded(true);
			teamDao.updateTeam(team);
			return 1l;
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
			return 0l;
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
