import React, { Component } from 'react';
import Signup from './Signup'
import NavBar from '../Navbar/Navbar';

class SignupMain extends Component {
    state = {}
    render() {
        return (<div>
            <NavBar></NavBar>
            <h3 align="center"><b> Sign Up</b></h3>
            <div className='signup-center'>
                <Signup />
            </div>

        </div>);
    }
}

export default SignupMain;