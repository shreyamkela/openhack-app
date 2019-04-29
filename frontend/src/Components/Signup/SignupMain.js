import React, { Component } from 'react';
import Signup from './Signup'
import NavBar from '../Navbar/Navbar';

class SignupMain extends Component {
    state = {}
    render() {
        return (<div>
            <NavBar></NavBar>
            <div className='signup-center'>
                <Signup />
            </div>

        </div>);
    }
}

export default SignupMain;