package com.example.cmpe275.openhack.controller;

import java.util.HashMap;
import java.util.Set;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cmpe275.openhack.dao.HackathonDao;
import com.example.cmpe275.openhack.dao.HackathonDaoImpl;
import com.example.cmpe275.openhack.dao.SubmissionDao;
import com.example.cmpe275.openhack.dao.SubmissionDaoImpl;
import com.example.cmpe275.openhack.entity.Hackathon;
import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.entity.Team;

@RestController
public class SubmissionController {
	private HackathonDao hackathonDao;
	private SubmissionDao submissionDao;

	public SubmissionController() {
		hackathonDao = new HackathonDaoImpl();
		submissionDao = new SubmissionDaoImpl();
	}

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
				System.out.println(
						"qqqq" + currentSubmission.getTeam().getId() + currentSubmission.getHackathon().getId());
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

}
