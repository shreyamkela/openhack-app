import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Navbar from './Navbar/Navbar'
import Home from './Challenges/Home'
import HackathonDetails from './Hackathon/HackathonDetails'
import '../App.css'
import SignupMain from './Signup/SignupMain';
import Signup from './Signup/Signup';
import LoginMain from './Login/LoginMain';

class Main extends Component {

    render() {
        return (
            <div>
                <Route exact path="/" component={Navbar}></Route>
                <Route exact path="/home" component={Home}></Route>
                <Route exact path="/signup" component={SignupMain}></Route>
                <Route exact path="/login" component={LoginMain}></Route>
                <Route exact path="/hackathon_details" component={HackathonDetails}></Route>
            </div>
        )
    }
}

export default Main;