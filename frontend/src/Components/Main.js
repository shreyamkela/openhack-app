import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Navbar from './Navbar/Navbar'
import Home from './Challenges/Home'
import HackathonDetails from './Hackathon/HackathonDetails'
import HackathonCreate from './Hackathon/HackathonCreate';
import '../App.css'
import Signup from './Signup/Signup';
import Login from './Login/Login';
import Profile from './Profile/Profile';
import { Form } from 'antd';
import HackathonRegister from './Hackathon/HackathonRegister';


class Main extends Component {

    render() {
        return (
            <div>
                <Route exact path="/" component={Navbar}></Route>
                <Route exact path="/home" component={Home}></Route>
                <Route exact path="/signup" component={Signup}></Route>
                <Route exact path="/login" component={Login}></Route>
                <Route exact path="/profile" component={Profile}></Route>
                <Route exact path="/hackathon_details/:id" component={HackathonDetails}></Route>
                <Route exact path="/hackathon/create" component={HackathonCreate}></Route>
                <Route exact path="/hackathon/register/:id" component={HackathonRegister}></Route>
            </div>
        )
    }
}

export default Main;