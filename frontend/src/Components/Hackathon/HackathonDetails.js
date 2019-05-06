import React, { Component } from 'react'
import '../../App.css'
import { Link } from 'react-router-dom'
import NavBar from '../Navbar/Navbar';
import { Layout, Menu, Icon, Row, Col, Button, Modal, Divider } from 'antd';
import axios from 'axios'
import Title from 'antd/lib/typography/Title';
const { Content, Sider } = Layout;

// 4 states: nothing means no registration, 
// "payment pending" means payment is yet to be completed,
// "registred" means registered and paid.
// "closed" means cannot edit submission

class HackathonDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            cover_image: "",
            id: null,
            name: null,
            description: null,
            startDate: null,
            endDate: null,
            fee: null,
            teamSizeMin: null,
            teamSizeMax: null,
            discount: null,
            message: null,
            teamDetails: [],
            judgeDetails: [],
            sponsorDetails: [],
            aboutContentFlag: true,
            teamsContentFlag: false,
            judgesContentFlag: false,
            sponsorsContentFlag:false,
            visibleTeamModal: false,
            visibleSubmissionModal: false,
            submissionUrl: null, // IDK right now
            submissionButtonFlag:false,
            gradeButtonFlag:false,
            registerButtonFlag:false
        }
    }

    componentDidMount() {
        let body = {
            "userId": localStorage.getItem("userId")
        }
        let hackathonId = this.props.match.params.id
        console.log("from route", hackathonId)
        axios.defaults.withCredentials = true
        axios.post(`http://localhost:8080/hackathon/${hackathonId}`, body)
            .then(response => {
                if (response.status === 200) {
                    this.setState({
                        cover_image: "https://static-fastly.hackerearth.com/static/fight_club/images/Cover_1.jpg",
                        id: response.data.id,
                        name: response.data.name,
                        description: response.data.description,
                        startDate: response.data.startDate,
                        endDate: response.data.endDate,
                        fee: response.data.fee,
                        teamSizeMin: response.data.teamSizeMin,
                        teamSizeMax: response.data.teamSizeMax,
                        discount: response.data.discount,
                        message: "",//response.data.message,
                        teamDetails: response.data.teamDetails,
                        judgeDetails: response.data.judgeDetails,
                        sponsorDetails: response.data.sponsorDetails,
                        submissionUrl: response.data.submissionUrl
                    })
                }
            })
            .catch(err => {
                console.log(err)
            })
            
    }

    loadAboutContent = (e) => {
        this.setState({
            aboutContentFlag: true,
            teamsContentFlag: false,
            judgesContentFlag: false,
            sponsorsContentFlag:false
        })
    }
    loadJudgesContent = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag:false,
            judgesContentFlag: true
        })
    }
    loadTeamsContent = (e) => {
            this.setState({
                aboutContentFlag: false,
                teamsContentFlag: true,
                sponsorsContentFlag:false,
                judgesContentFlag: false
            })
    }

    loadSponsorsContent = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag:true,
            judgesContentFlag: false
        })
    }

    showTeamModal = () => {
        this.setState({
            visibleTeamModal: true
        })
    }

    showSubmissionModal = () => {
        this.setState({
            visibleSubmissionModal: true
        })
    }
    handleCancel = () => {
        this.setState({
            visibleTeamModal: false,
            visibleSubmissionModal: false
        })
    }

    submitWork = (e) => {
        console.log(this.state.submission)
        this.setState({
            visibleSubmissionModal: false
        })
    }
    handleSubmission = (e) => {
        this.setState({
            submission: e.target.value
        })
    }
    render() {
        var content = null
        var buttons = null
        var teamModalContent = null
        var registerButtonFlag = false
        var submissionButtonFlag = false
        var gradeButtonFlag = false
        console.log(new Date(this.state.startDate) >  new Date())
        if(new Date(this.state.startDate) >  new Date() || new Date(this.state.endDate) <  new Date()){
            submissionButtonFlag=true
        }
        if(new Date(this.state.startDate) < new Date()){
            registerButtonFlag = true
        }
        if(new Date(this.state.endDate) > Date.now()){
            gradeButtonFlag=true
        }
        if (this.state.aboutContentFlag) {
            content = <div>
                <p><b>Overview</b>: {this.state.description}</p>
                <p><b>Fee</b>: {this.state.fee}</p>
                <p><b>Sponsor Discount</b>: {this.state.discount}</p>
            </div>
        } else if (this.state.teamsContentFlag) {
            content = this.state.teamDetails && this.state.teamDetails.map(team => {
                return(
                    <div>
                        <a href="#" id={team.teamId}> 
                            <Title level={4}>{team && team.teamName}</Title>
                        </a>
                        <p>Members: {team && team.teamSize}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        }else if(this.state.judgesContentFlag){
            content = this.state.judgeDetails && this.state.judgeDetails.map(judge => {
                return(
                    <div>
                        <a href="#" id={judge.judgeId}> 
                            <Title level={4}>{judge && judge.judgeName}</Title>
                        </a>
                        <p>{judge && judge.judgeScreenName}, {judge && judge.judgeEmail}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        }else if(this.state.sponsorsContentFlag){
            content = this.state.sponsorDetails && this.state.sponsorDetails.map(sponsor => {
                return(
                    <div>
                        <a href="#" id={sponsor.sponsorId}> 
                            <Title level={4}>{sponsor && sponsor.sponsorName}</Title>
                        </a>
                        <p>{sponsor && sponsor.sponsorDescription}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        }
        if (this.state.message === "registered") {
            buttons = <div>
                <Button type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.showTeamModal}>View Team</Button><br />
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
                <Button type="primary" size="large" style={{ marginTop: "10px" }} onClick={this.showSubmissionModal}>Submit/Edit Work</Button>
                <Modal
                    title="Submission"
                    visible={this.state.visibleSubmissionModal}
                    onOk={this.submitWork}
                    onCancel={this.handleCancel}
                >
                    <input class="form-control" id="submission" onChange={this.handleSubmission} value={this.state.submission} disabled={submissionButtonFlag}/>
                </Modal>
            </div>
        } else if (this.state.message === "payment pending") {
            buttons = <div>
                <Button type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.showTeamModal}>View Team</Button><br />
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
                <Link to="/payment"><Button type="primary" size="large" style={{ marginTop: "10px" }} onClick={this.showSubmissionModal}>Pay</Button></Link>
            </div>
        }else if(this.state.message === "judge"){
            buttons = <div>
                <Link to="/hackathon/grade">
                    <Button type="primary" size="large" style={{ marginTop: "25%" }} disabled={gradeButtonFlag}>Grade Submission</Button>
                </Link>
            </div>
        } else {
            buttons = <div>
                <Link to="/hackathon/register/1"><Button type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.showTeamModal} disabled={registerButtonFlag}>Register</Button><br /></Link>
            </div>
        }
        return (
            <div>
                <NavBar></NavBar>
                <div style={{ backgroundImage: "url(" + this.state.cover_image + ")", height: "300px", position: "relative" }}>
                    <h3 class="hackathon-name"><b>{this.state.name}</b></h3>
                </div>
                <div style={{ backgroundColor: "#EAECEE", height: "200px" }}>
                    <div style={{ marginLeft: "10%" }}>
                        <Row type="flex">
                            <Col span={18}>
                                <p style={{ color: "#46535e", fontSize: "15px", paddingTop: "4%" }}> <Icon type="user" />  Allowed team size : {this.state.teamSizeMin} - {this.state.teamSizeMin}</p>
                                <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="calendar" /> Starts on : {this.state.startDate}</p>
                                <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="calendar" /> Ends on : {this.state.endDate}</p>
                            </Col>
                            <Col span={6}>
                                {buttons}
                            </Col>
                        </Row>

                    </div>
                </div>
                <div>
                    <Layout>
                        <Sider style={{ overflow: 'auto', height: '100%', left: 0 }}>
                            <div className="logo" />
                            <Menu mode="inline" defaultSelectedKeys={['1']}>
                                <Menu.Item key="1"
                                    onClick={this.loadAboutContent}
                                >
                                    <Icon type="info" />
                                    <span className="nav-text">About</span>
                                    {/* <Link to="/" className="nav-text">About</Link> */}
                                </Menu.Item>
                                <Menu.Item key="2"
                                    onClick={this.loadJudgesContent}
                                >
                                    <Icon type="user" />
                                    <span className="nav-text">Judges</span>
                                </Menu.Item>
                                <Menu.Item key="3"
                                    onClick={this.loadTeamsContent}
                                >
                                    <Icon type="team" />
                                    <span className="nav-text">Teams</span>
                                </Menu.Item>
                                <Menu.Item key="4"
                                    onClick={this.loadSponsorsContent}
                                >
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
                                <div style={{ padding: 14, background: '#fff'}}>
                                    {content}    
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