import React, { Component } from 'react'
import '../../App.css'
import {Link} from 'react-router-dom'   
import NavBar from '../Navbar/Navbar';
import { Layout, Menu, Icon, Row, Col, Button, Modal } from 'antd';

const {  Content, Sider} = Layout;

// 4 states: nothing means no registration, 
// "payment pending" means payment is yet to be completed,
// "registred" means registered and paid.
// "closed" means cannot edit submission

class HackathonDetails extends Component{
    constructor(props){
        super(props);
        this.state={
            cover_image : "",
            hackathon_name : "",
            min_size : "",
            max_size : "",
            start_date : "",
            end_date : "",
            aboutContent:[],
            aboutContentFlag:true,
            teamsContent : [],
            teamsContentFlag : false,
            judgesContent:[],
            judgesContentFlag: false,
            message:null,
            visibleTeamModal:false,
            visibleSubmissionModal:false,
            submission:null
        }
    }

    componentDidMount(){
        this.setState({
            cover_image : "https://static-fastly.hackerearth.com/static/fight_club/images/Cover_1.jpg",
            hackathon_name : "CodeArena Hackathon 2019",
            min_size : "2",
            max_size : "4",
            start_date : "08 Mar 2019, 12:00 PM PT",
            end_date : "25 Apr 2019, 11:59 PM PT",
            aboutContent : [
                {
                    "title" : "About title",
                    "desc" : "The Description  of about"
                },
                {
                    "title" : "About title",
                    "desc" : "The Description  of about"
                }
            ],
            message:"payment pending"
        })
    }

    loadAboutContent = (e) => {
        this.setState({
            aboutContentFlag:true,
            teamsContentFlag:false,
            judgesContentFlag:false
        })
    }

    loadTeamsContent = (e) => {
        this.setState({
            teamsContent:[
                {
                    "teamName":"XYZ",
                    "size":"4",
                    "members":[
                        {
                            "name":"abc",
                            "surname":"pqr"
                        },
                        {
                            "name":"abc",
                            "surname":"pqr"
                        },
                        {
                            "name":"abc",
                            "surname":"pqr"
                        }
                    ]
                },
                {
                    "teamName":"XYZ",
                    "size":"4",
                    "members":[
                        {
                            "name":"abc",
                            "surname":"pqr"
                        },
                        {
                            "name":"abc",
                            "surname":"pqr"
                        },
                        {
                            "name":"abc",
                            "surname":"pqr"
                        }
                    ]
                },
                {
                    "teamName":"XYZ",
                    "size":"4",
                    "members":[
                        {
                            "name":"abc",
                            "surname":"pqr"
                        },
                        {
                            "name":"abc",
                            "surname":"pqr"
                        },
                        {
                            "name":"abc",
                            "surname":"pqr"
                        }
                    ]
                }
            ]
        },() => {
            this.setState({
                aboutContentFlag:false,
                teamsContentFlag:true,
                judgesContentFlag:false
            })
        })
    }

    showTeamModal = () => {
        this.setState({
            visibleTeamModal:true
        })
    }

    showSubmissionModal = () =>{
        this.setState({
            visibleSubmissionModal:true
        })
    }
    handleCancel = () => {
        this.setState({
            visibleTeamModal:false,
            visibleSubmissionModal:false
        })
    }

    submitWork = (e) => {
        console.log(this.state.submission)
        this.setState({
            visibleSubmissionModal:false
        })
    }
    handleSubmission = (e) => {
        this.setState({
            submission:e.target.value
        })
    }
    render(){
        var content = null
        var buttons = null
        var teamModalContent = null
        if(this.state.aboutContentFlag){
            content = <div>
                <h3>About </h3>
            </div>
        }else if(this.state.teamsContentFlag){
            content = <div>
                <h3>Team1</h3>
                <p>{this.state.teamsContent[0].name}</p>
                <h3>Team2</h3>
                <p>{this.state.teamsContent[1].name}</p>
            </div>
        }
        if(this.state.team){
            teamModalContent = <p>Team Name</p>
        }
        if(this.state.message === "registered"){
            buttons = <div>
                <Button type="primary" size="large" style={{marginTop:"20%"}} onClick={this.showTeamModal}>View Team</Button><br/>
                <Modal
                    title="Your Team"
                    visible={this.state.visibleTeamModal}
                    onCancel={this.handleCancel}
                    footer={[
                        <Button key="back" onClick={this.handleCancel}>Back</Button>,
                      ]}
                >
                <p>{teamModalContent}</p>
                </Modal>
                <Button type="primary" size="large" style={{marginTop:"10px"}} onClick={this.showSubmissionModal}>Submit/Edit Work</Button>
                <Modal
                    title="Submission"
                    visible={this.state.visibleSubmissionModal}
                    onOk={this.submitWork}
                    onCancel={this.handleCancel}
                >
                <input class="form-control" id="submission" onChange={this.handleSubmission} value={this.state.submission}/>
                </Modal>
            </div>
        }else if(this.state.message==="payment pending"){
            buttons = <div>
            <Button type="primary" size="large" style={{marginTop:"20%"}} onClick={this.showTeamModal}>View Team</Button><br/>
            <Modal
                title="Your Team"
                visible={this.state.visibleTeamModal}
                onCancel={this.handleCancel}
                footer={[
                    <Button key="back" onClick={this.handleCancel}>Back</Button>,
                  ]}
            >
            <p>{teamModalContent}</p>
            </Modal>
            <Link to="/payment"><Button type="primary" size="large" style={{marginTop:"10px"}} onClick={this.showSubmissionModal}>Pay</Button></Link>
        </div>
        }
        else if(this.state.message === "closed"){
            buttons = <div>
                <Button type="primary" size="large" style={{marginTop:"20%"}} onClick={this.showTeamModal}>View Team</Button><br/>
                <Modal
                    title="Your Team"
                    visible={this.state.visibleTeamModal}
                    onCancel={this.handleCancel}
                    footer={[
                        <Button key="back" onClick={this.handleCancel}>Back</Button>,
                      ]}
                >
                <p>{teamModalContent}</p>
                </Modal>
                <Button type="primary" size="large" style={{marginTop:"10px"}} onClick={this.showSubmissionModal}>View Submission/Grade</Button>
                <Modal
                    title="Submission"
                    visible={this.state.visibleSubmissionModal}
                    onCancel={this.handleCancel}
                    footer={[
                        <Button key="back" onClick={this.handleCancel}>Back</Button>,
                      ]}
                >
                <input class="form-control" id="submission" onChange={this.handleSubmission} value={this.state.submission} disabled={true}/>
                <p>Grades: {this.state.grades}</p>
                </Modal>
            </div>
        }else{
            buttons = <div>
                    <Link to="/hackathon/register/1"><Button type="primary" size="large" style={{marginTop:"20%"}} onClick={this.showTeamModal}>Register</Button><br/></Link>
                </div>
        }
        return (
            <div>
                <NavBar></NavBar>
                <div style={ { backgroundImage: "url(" + this.state.cover_image + ")", height:"300px", position:"relative"} }>
                    <h3 class="hackathon-name"><b>{this.state.hackathon_name}</b></h3>
                </div>
                <div style={{ backgroundColor: "#EAECEE" , height:"200px"  }}>
                    <div style={{ marginLeft : "10%" }}>
                        <Row type="flex">
                            <Col span={18}>
                                <p style= {{color:"#46535e", fontSize:"15px", paddingTop: "4%"}}> <Icon type="user" />  Allowed team size : {this.state.min_size} - {this.state.max_size}</p>
                                <p style= {{color:"#46535e", fontSize:"15px"}}><Icon type="calendar" /> Starts on : {this.state.start_date}</p>
                                <p style= {{color:"#46535e", fontSize:"15px"}}><Icon type="calendar" /> Ends on : {this.state.end_date}</p>
                            </Col>
                            <Col span={6}>
                                {buttons}
                            </Col>
                        </Row>
                        
                    </div>
                </div>
                <div>
                <Layout>
                <Sider style={{ overflow: 'auto', height: '100%',  left: 0}}>
                        <div className="logo" />
                        <Menu mode="inline" defaultSelectedKeys={['1']}>
                            <Menu.Item key="1"
                                onClick = {this.loadAboutContent}
                            >
                                <Icon type="info" />
                                <span className="nav-text">About</span>
                                {/* <Link to="/" className="nav-text">About</Link> */}
                            </Menu.Item>
                            <Menu.Item key="2">
                                <Icon type="user" />
                                <span className="nav-text">Judges</span>
                            </Menu.Item>
                            <Menu.Item key="3"
                                onClick = {this.loadTeamsContent}
                            >
                                <Icon type="team" />
                                <span className="nav-text">Teams</span>
                            </Menu.Item>
                            <Menu.Item key="4">
                                <Icon type="dollar" />
                                <span className="nav-text">Sponsors</span>
                            </Menu.Item>
                            <Menu.Item key="5">
                                <Icon type="cloud-o" />
                                <span className="nav-text">Discussions</span>
                            </Menu.Item>
                        </Menu>
                    </Sider>
                    <Layout style={{ marginLeft: 10 }}>
                        <Content style={{ margin: '0px 16px 0', overflow: 'initial' }}>
                            <div style={{ padding: 14, background: '#fff', textAlign: 'center' }}>
                            ...
                            <br />
                            {content}
                            <br />...<br />...<br />...<br />
                            long
                            <br />...<br />...<br />...<br />...<br />...<br />...
                            <br />...<br />...<br />...<br />...<br />...<br />...
                            <br />...<br />...<br />...<br />...<br />...<br />
                            content
                            </div>
                        </Content>
                    </Layout>
                </Layout>
                ); 
                </div>
            </div>
        )
    }
}

export default HackathonDetails;