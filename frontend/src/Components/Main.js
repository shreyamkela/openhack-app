import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Navbar from './Navbar/Navbar'
import Home from './Challenges/Home'
import HackathonDetails from './Hackathon/HackathonDetails'
import HackathonCreate from './Hackathon/HackathonCreate';
// import HackathonCreate from './Admin/Hackathon/HackathonCreate';
import adminHackathonDetails from './Admin/Hackathon/HackathonDetails'
import '../App.css'
import Signup from './Signup/Signup';
import Login from './Login/Login';
import Profile from './Profile/Profile';
import { Form } from 'antd';
import HackathonRegister from './Hackathon/HackathonRegister';
import OrganizationsAll from './Organization/OrganizationAll';
import OrganizationDetails from './Organization/OrganizationDetails'
import HackathonPayment from './Hackathon/HackathonPayment';
import ViewSubmissions from './Submission/ViewSubmissions';
import LandingPage from './Navbar/LandingPage';
import RegistrationPaymentReport from './Report/RegistrationPaymentReport';
//import HackathonPayment from './Hackathon/HackathonPayment';
//import ViewSubmissions from './Submission/ViewSubmissions';

class Main extends Component {

  render() {
    return (
      <div>
        {/* <Route exact path="/" component={Navbar}></Route> */}
        <Route exact path="/" component={LandingPage}></Route>
        <Route exact path="/home" component={Home}></Route>
        <Route exact path="/signup" component={Signup}></Route>
        <Route exact path="/login" component={Login}></Route>
        <Route exact path="/profile" component={Profile}></Route>
        <Route exact path="/hackathon_details/:id" component={HackathonDetails}></Route>
        {/* <Route exact path="/hackathon/create" component={HackathonCreate}></Route> */}
        <Route exact path="/admin/hackathon/create" component={HackathonCreate}></Route>
        <Route exact path="/hackathon/register/:id" component={HackathonRegister}></Route>
        <Route exact path="/hacker_organizations" component={OrganizationsAll}></Route>
        <Route exact path="/organization_details/:user_id/:org_id" component={OrganizationDetails}></Route>
        <Route exact path="/hackathon/payment/:paymentId" component={HackathonPayment}></Route>
        <Route exact path="/hacker/gradeSubmissions/:hackathonId" component={ViewSubmissions}></Route>
        <Route exact path="/admin/hackathon_details/:id" component={adminHackathonDetails}></Route>
        <Route exact path="/admin/registrationPaymentReport" component={RegistrationPaymentReport}></Route>
        {/* <Route exact path="/admin/hackathon/create" component={HackathonCreate}></Route> */}
      </div>
    )
  }
}

export default Main;
