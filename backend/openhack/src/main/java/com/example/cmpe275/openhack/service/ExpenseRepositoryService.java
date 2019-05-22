package com.example.cmpe275.openhack.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cmpe275.openhack.entity.Expense;
import com.example.cmpe275.openhack.entity.Payment;
import com.example.cmpe275.openhack.entity.Submission;
import com.example.cmpe275.openhack.entity.User;
import com.example.cmpe275.openhack.repository.ExpenseRepository;

@Service
public class ExpenseRepositoryService {
	
	@Autowired
	ExpenseRepository expenseRepository;
	
	@Autowired
	EntityManager em;
	
	public Expense create(Expense expense) {
		
		Expense createdExpense = expenseRepository.save(expense);
		return createdExpense;
	
}
	public List<Expense> findAllExpenses(){
		return expenseRepository.findAll();
	}
	
	public List<Expense> findExpenseByHackathonId(long id) {
		List<Expense> result = new ArrayList<>();
		List<Expense> expenses = findAllExpenses();
		for(Expense expense:expenses) {
			if(expense.getHackathon().getId()== id) {
				result.add(expense);
			}
		}
		
		return result;
	}

	
}
