Team Members:

Name                  Email                                 Student ID
Darshil Kapadia       darshilpareshbhai.kapadia@sjsu.edu    013007280
Kavina Desai          kavina.desai@sjsu.edu                 013784017
Sayalee Bhusari       sayaleeshankar.bhusari@sjsu.edu       013006461
Shreyam Kela          shreyam.kela@sjsu.edu                 013775411

URL: http://3.86.236.128:3000

Instructional video showing the demo and walk through of the application - https://drive.google.com/drive/folders/1Gk4fihrDWvUvOVeqDGHqsQCeixxT0YXh?usp=sharing


Build instructions to run the application on Localhost:
    A) Run Spring Boot backend server - Go to folder path from terminal: /backend
         1. mvn install
         2. mvn spring-boot:run
    B) Run React frontend server - Go to folder path from terminal: /frontend
         1. npm install OR npm install --ignore-scripts
         2. npm start
         3. Go to the URL: localhost:3000
  

Bonus Features:
   A) Per hackathon expense reporting - Admin is able to add expenses for a hackathon before it is finalized. An expense has the format of: 
        1. Expense title. Example - event venue rental, judge compensation.
        2. Expense description. Example - Text explanation of the expense.
        3. Expense time. Example - Is a date before or up to the time of the reporting.
        4. Expense amount. Example - Float number. Always in USD.
   B) Invite non-members to participate in hackathons. Whoever gets the invitation will need to go through the registration process.
