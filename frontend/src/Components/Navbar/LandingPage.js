import React, { Component } from 'react'
import '../../App.css'
import { Redirect } from 'react-router'
import NavBar from '../Navbar/Navbar';
const username = localStorage.getItem("userName");

class LandingPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
        }
    }

    render(){
        let redirect = null
        if (localStorage.getItem("userId")) 
        {
            redirect = <Redirect to="/home" />
        }
        return(
            <div>
                {redirect}
                <NavBar></NavBar>
                <img src = "http://techmeetupsng.com/wp-content/uploads/2018/05/hacko.png" width = "100%" height = "500px"></img>
            </div>
        )
    }
}

export default LandingPage