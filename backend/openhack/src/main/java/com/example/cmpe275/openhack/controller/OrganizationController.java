package com.example.cmpe275.openhack.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.cmpe275.openhack.dao.OrganizationDao;
import com.example.cmpe275.openhack.dao.OrganizationDaoImpl;
import com.example.cmpe275.openhack.entity.Organization;

@Controller
public class OrganizationController {
	
	public OrganizationController() {
		
	}
	
//	@Autowired
	OrganizationDao orgdao = new OrganizationDaoImpl();
	
	@RequestMapping(value = "/hacker/createOrganization", method = RequestMethod.POST, produces = { "application/json"},
					consumes = {"application/JSON"})
	@ResponseBody
	public Map<Object, Object> createOrganization(HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception 
	{
		System.out.println("\ncreateOrganization method called!!");
		String jsonString = request.getParameter("json");
		System.out.println("\n"+ jsonString +"\n");
		Map<Object,Object> map = new HashMap<>();
		return map;
	}
}
