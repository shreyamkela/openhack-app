package com.example.cmpe275.openhack.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;

public class ValidationAspect {

	@Before("execution(* com.example.cmpe275.openhack.controller(..))")
	public void checkValidationsAdvice(JoinPoint joinPoint) {
			try {
				int numberOfArgs =joinPoint.getArgs().length;
				for(int i=0;i<numberOfArgs;i++){
					if(joinPoint.getArgs()[i]==null){
						throw new IllegalArgumentException();
					}
				}
				
			} catch (IllegalArgumentException e) {
				System.out.println("Enter correct parameters ");
				throw e;
			}
		
	}
}
