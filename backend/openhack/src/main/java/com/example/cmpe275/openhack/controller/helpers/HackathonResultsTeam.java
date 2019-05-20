package com.example.cmpe275.openhack.controller.helpers;

import java.util.ArrayList;

// This class is being used in displaying the results report of the hackathon, through the /hackathon/results route
public class HackathonResultsTeam {
	
	private String teamName;
	private ArrayList<String> teamMembers;
	private Float grade;
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public ArrayList<String> getTeamMembers() {
		return teamMembers;
	}
	public void setTeamMembers(ArrayList<String> teamMembers) {
		this.teamMembers = teamMembers;
	}
	public Float getGrade() {
		return grade;
	}
	public void setGrade(Float grade) {
		this.grade = grade;
	}
}
