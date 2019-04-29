import React, { Component } from 'react';
import NavBar from '../Navbar/Navbar';
import Login from './Login';
import '../../css/LoginMain.css';
class LoginMain extends Component {
    state = {}
    render() {
        return (<div>
            <NavBar />
            <div  className='login-center'>
                <Login></Login>
                </div>
        </div>);
    }
}

export default LoginMain;