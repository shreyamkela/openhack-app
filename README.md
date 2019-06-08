# OpenHack (Prototype Of HackerEarth)
Hackathon Event Management System - Project for CmpE 275 Course Lab in the Spring 19 semester at San Jose State University

## Project Description 
Openhack is a Hackathon Management System inspired by the HackerEarth platform (www.hackerearth.com). Our application presents to the users a collaborative environment where teams can participate in hackathons, make submissions, and get results based on their performance. We bring together developers, tech volunteers, organizations, social actors, and sponsors to create open-source code for all of humanity, through hackathons. 

### Demo of the application
Video - https://drive.google.com/drive/folders/1Gk4fihrDWvUvOVeqDGHqsQCeixxT0YXh?usp=sharing

![Alt text](https://github.com/shreyamkela/openhack-app/blob/master/homepage-thumbnail.jpg?raw=true "Title")

### Core features
- Our application allows user to sign up as an admin or as a hacker (participant). Any user with the sjsu.edu email id will automatically become the admin. All other email ids used for signup will become hackers. 
- Admin can create and update hackathons, can view all the details about hackathons, the participants, payment reports of participants, and so on. The admin looks over the operation of the hackathons and can open, close, and finalize any hackathon. 
- A hacker can view all the previous, on-going, and upcoming hackathons and can register to any upcoming hackathons with their team, and submit the work which is graded by the jury of that hackathon. 
- A hacker can also create organizations and other users will send request to join the organization and the owner of the organization can choose to accept or decline the join request.
- A hacker can view the leaderboard (results reports) of the hackathons that they have participated in. Congratulatory emails are sent to the winners of the hackathons and can be seen at the top of our leaderboard.
- A user can invite non-members to participate in hackathons. Whoever gets the invitation needs to go through the registration process.

### Build instructions to run the application on Localhost
- Run Spring Boot backend server - Go to folder path from terminal: /backend
    - ``` mvn install ```
    - ``` mvn spring-boot:run ```
- Run React frontend server - Go to folder path from terminal: /frontend        
    - ``` npm install OR npm install --ignore-scripts ```
    - ``` npm start ```
    - ``` Go to the URL: localhost:3000 ```
         
### Technologies utilized 
Spring, Spring Boot, Java, React, JavaScript, HTML5, CSS3, Bootstrap, Ant Design, AWS EC2 S3 RDS, JPA, ORM, AOP, Hibernate, JavaMail API, REST
         
### Team Members:
Darshil Kapadia, Shreyam Kela, Sayalee Bhusari, Kavina Desai
