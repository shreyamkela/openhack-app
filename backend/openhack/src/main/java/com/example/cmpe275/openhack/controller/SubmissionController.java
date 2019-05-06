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
			if (map.get("url") != null)
				submission.setURL(map.get("url"));

			if (map.get("hackathonId") != null) {
				Long hackathonId = new Long((String) map.get("hackathonId"));

				Hackathon hackathon = hackathonDao.findById(hackathonId);
				System.out.println("Hackathon found:" + hackathon.getName());
				submission.setHackathon(hackathon);

				if (map.get("teamId") != null) {
					Long teamId = new Long((String) map.get("teamId"));
					Set<Team> teams = hackathon.getTeams();
					for (Team team : teams) {
						if (team.getId() == teamId) {
							System.out.println("Team found:" + team.getTeamName());
							submission.setTeam(team);
						}
					}
				}
				submission = submissionDao.create(submission);
			}
		} catch (Exception e) {
			System.out.println("Exception while creating a submission" + e);
		}
		return submission.getId();
	}

}
