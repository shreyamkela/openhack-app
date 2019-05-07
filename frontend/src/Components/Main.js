import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Navbar from './Navbar/Navbar'
import Home from './Challenges/Home'
import HackathonDetails from './Hackathon/HackathonDetails'
import HackathonCreate from './Hackathon/HackathonCreate';
import '../App.css'
import SignupMain from './Signup/SignupMain';
import Signup from './Signup/Signup';
import LoginMain from './Login/LoginMain';
import { Form } from 'antd';
import HackathonRegister from './Hackathon/HackathonRegister';
import OrganizationsAll from './Organization/OrganizationAll';
import OrganizationDetails from './Organization/OrganizationDetails'


class Main extends Component {

    render() {
        return (
            <div>
                <Route exact path="/" component={Navbar}></Route>
                <Route exact path="/home" component={Home}></Route>
                <Route exact path="/signup" component={SignupMain}></Route>
                <Route exact path="/login" component={LoginMain}></Route>
                <Route exact path="/hackathon_details/:id" component={HackathonDetails}></Route>
                <Route exact path="/hackathon/create" component={HackathonCreate}></Route>
                <Route exact path="/hackathon/register/:id" component={HackathonRegister}></Route>
                <Route exact path="/hacker_organizations" component={OrganizationsAll}></Route>
                <Route exact path="/organization_details/:user_id/:org_id" component={OrganizationDetails}></Route>
            </div>
        )
    }
}

export default Main;