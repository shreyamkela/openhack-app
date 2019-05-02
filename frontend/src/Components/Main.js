import React,{Component} from 'react';
import {Route} from 'react-router-dom'; 
import Navbar from './Navbar/Navbar'
import Home from './Challenges/Home'
import HackathonDetails from './Hackathon/HackathonDetails'
import HackathonCreate from './Hackathon/HackathonCreate';
import '../App.css'
import { Form } from 'antd';
import HackathonRegister from './Hackathon/HackathonRegister';


class Main extends Component{

    render(){
        return(
            <div>
                <Route exact path="/" component={Navbar}></Route>
                <Route exact path="/home" component={Home}></Route>
                <Route exact path="/hackathon_details/:id" component={HackathonDetails}></Route>
                <Route exact path="/hackathon/create" component={HackathonCreate}></Route>
                <Route exact path="/hackathon/register/:id" component={HackathonRegister}></Route>
            </div>
        )
    }
}

export default Main;