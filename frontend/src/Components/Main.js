import React, { Component } from "react";
import { Route } from "react-router-dom";
import Navbar from "./Navbar/Navbar";
import Home from "./Challenges/Home";
import adminHackathonDetails from "./Admin/Hackathon/HackathonDetails";
import HackathonDetails from "./Hackathon/HackathonDetails";
//import HackathonCreate from './Admin/Hackathon/HackathonCreate';
import HackathonCreate from "./Hackathon/HackathonCreate";
// import HackathonCreate from './Admin/Hackathon/HackathonCreate';
//import adminHackathonDetails from "./Admin/Hackathon/HackathonDetails";
import "../App.css";
import Signup from "./Signup/Signup";
import Login from "./Login/Login";
import Profile from "./Profile/Profile";
import { Form } from "antd";
import HackathonRegister from "./Hackathon/HackathonRegister";
import OrganizationsAll from "./Organization/OrganizationAll";
import OrganizationDetails from "./Organization/OrganizationDetails";
import HackathonPayment from "./Hackathon/HackathonPayment";
import ViewSubmissions from "./Submission/ViewSubmissions";
//import HackathonPayment from './Hackathon/HackathonPayment';
//import ViewSubmissions from './Submission/ViewSubmissions';

class Main extends Component {
  render() {
    return (
      <div>
        <Route exact path="/" component={Navbar} />
        <Route exact path="/home" component={Home} />
        <Route exact path="/signup" component={Signup} />
        <Route exact path="/login" component={Login} />
        <Route exact path="/profile" component={Profile} />
        <Route exact path="/hackathon_details/:id" component={HackathonDetails} />
        {/* <Route exact path="/hackathon/create" component={HackathonCreate}></Route> */}
        <Route exact path="/admin/hackathon/create" component={HackathonCreate} />
        <Route exact path="/hackathon/register/:id" component={HackathonRegister} />
        <Route exact path="/hacker_organizations" component={OrganizationsAll} />
        <Route exact path="/organization_details/:user_id/:org_id" component={OrganizationDetails} />
        <Route exact path="/hackathon/payment/:paymentId" component={HackathonPayment} />
        <Route exact path="/hacker/gradeSubmissions/:hackathonId" component={ViewSubmissions} />
        <Route exact path="/admin/hackathon_details/:id" component={adminHackathonDetails} />
        {/* <Route exact path="/admin/hackathon/create" component={HackathonCreate}></Route> */}
      </div>
    );
  }
}

export default Main;
