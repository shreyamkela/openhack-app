import React, { Component } from 'react'
import '../../App.css'
import {Link} from 'react-router-dom'   
import NavBar from '../Navbar/Navbar';
import { Layout, Menu, Icon } from 'antd';

const { Header, Content, Footer, Sider} = Layout;

class AboutPage extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return (
            <div>
                <div style={{ padding: 14, background: '#fff', textAlign: 'center' }}>
                    ...
                    <br />
                    Really
                    <br />...<br />...<br />...<br />
                    long About page
                    <br />...<br />...<br />...<br />...<br />...<br />...
                    <br />...<br />...<br />...<br />...<br />...<br />...
                    <br />...<br />...<br />...<br />...<br />...<br />...
                    <br />...<br />...<br />...<br />...<br />...<br />...
                    <br />...<br />...<br />...<br />...<br />...<br />
                    content
                </div>
            </div>
        )
    }
}

export default AboutPage;