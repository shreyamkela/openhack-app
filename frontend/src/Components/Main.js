import React,{Component} from 'react';
import {Route} from 'react-router-dom'; 
import Navbar from './Navbar/Navbar'
import Home from './Challenges/Home'
import '../App.css'

class Main extends Component{

    render(){
        return(
            <div>
                <Route exact path="/" component={Navbar}></Route>
                <Route exact path="/home" component={Home}></Route>
            </div>
        )
    }
}

export default Main;