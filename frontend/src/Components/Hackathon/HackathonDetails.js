import React, { Component } from 'react'
import '../../App.css'
import { Link } from 'react-router-dom'
import NavBar from '../Navbar/Navbar';
import { Layout, Menu, Icon, Row, Col, Button, Modal, Divider, Avatar, Form, Input, Skeleton } from 'antd';
import axios from 'axios'
import Title from 'antd/lib/typography/Title';
import swal from 'sweetalert';
import {Redirect} from 'react-router'
import Swal from 'sweetalert2';
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
            userTeam: [],
            userTeamId: null,
            isFinalize: false,
            winnerTeam: null,
            aboutContentFlag: true,
            teamsContentFlag: false,
            judgesContentFlag: false,
            sponsorsContentFlag: false,
            visibleTeamModal: false,
            visibleSubmissionModal: false,
            submissionUrl: null, // IDK right now
            submissionButtonFlag: false,
            gradeButtonFlag: false,
            registerButtonFlag: false,
            isLoaded: false
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
                        userTeam: response.data.userTeam,
                        userTeamId: response.data.userTeamId,
                        discount: response.data.discount,
                        message: response.data.message,
                        teamDetails: response.data.teamDetails,
                        judgeDetails: response.data.judgeDetails,
                        sponsorDetails: response.data.sponsorDetails,
                        submissionUrl: response.data.submissionUrl,
                        isFinalize: response.data.isFinalize,
                        winnerTeam: response.data.winnerTeam,
                        isLoaded: true
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
            sponsorsContentFlag: false
        })
    }
    loadJudgesContent = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag: false,
            judgesContentFlag: true
        })
    }
    loadTeamsContent = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: true,
            sponsorsContentFlag: false,
            judgesContentFlag: false
        })
    }

    loadSponsorsContent = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag: true,
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
        Swal.fire({
            title: 'Submission in progress',
            text: 'Please Wait...',
            showCancelButton: false,
            showConfirmButton: false,
            type:'info'
          })
        console.log(this.state.submissionUrl)
        let body = {
            "hackathonId": this.props.match.params.id,
            "teamId": this.state.userTeamId,
            "url": this.state.submissionUrl
        }

        axios.post("http://localhost:8080/addSubmission", body)
            .then(response => {
                if(response.status===200){
                    Swal.close()
                    swal("Submitted work","success")
                    window.location.reload();      
                }
            })
    }
    handleSubmission = (e) => {
        this.setState({
            submissionUrl: e.target.value
        })
    }
    routeToResults = () => {
        this.props.history.push(`/hackathon_details/${this.props.match.params.id}/results`);
    }
    render() {
        var content = null
        var buttons = null
        var teamModalContent = null
        var registerButtonFlag = false
        var submissionButtonFlag = false
        var gradeButtonFlag = false
        var redirect = null
        var winnerTeam = null
        var loaded = null
        var detailsContent = null
        if (this.state.winnerTeam != null) {
            winnerTeam = `${this.state.winnerTeam}`
        }
        if (!this.state.isLoaded) {
            loaded = <Skeleton active />
            detailsContent = null
        } else {
            loaded = null
            detailsContent = <Col span={18}>
                <p style={{ color: "#46535e", fontSize: "15px", paddingTop: "4%" }}> <Icon type="user" />  Allowed team size : {this.state.teamSizeMin} - {this.state.teamSizeMax}</p>
                <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="calendar" /> Starts on : {new Date(this.state.startDate).toDateString()}</p>
                <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="calendar" /> Ends on : {new Date(this.state.endDate).toDateString()}</p>
                <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="user" /> Winner Team Name : <b>{winnerTeam}</b></p>
            </Col>
        }
        if (!localStorage.getItem("userId")) {
            redirect = <Redirect to="/login" />
        }


        const { getFieldDecorator } = this.props.form
        console.log(new Date(this.state.startDate) > new Date())
        if (new Date(this.state.startDate) > new Date() || new Date(this.state.endDate) < new Date()) {
            submissionButtonFlag = true
        }
        if (new Date(this.state.startDate) < new Date()) {
            registerButtonFlag = true
        }
        if (new Date(this.state.endDate) > Date.now()) {
            gradeButtonFlag = true
        }
        if (this.state.isFinalize) {
            submissionButtonFlag = true
            registerButtonFlag = true
            gradeButtonFlag = true
        }
        if (this.state.aboutContentFlag) {
            content = <div>
                <p><b>Overview</b>: {this.state.description}</p>
                <p><b>Fee</b>: ${this.state.fee}</p>
                <p><b>Sponsor Discount</b>: {this.state.discount}%</p>
            </div>
        } else if (this.state.teamsContentFlag) {
            content = this.state.teamDetails && this.state.teamDetails.map(team => {
                return (
                    <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop: "10px", boxShadow: "5px 5px #FAFAFA", padding: "8px", marginLeft: "20px" }}>
                        <a href="#" id={team.teamId}>
                            <Title level={4} style={{ marginLeft: "20px" }}>Team name : {team && team.teamName}</Title>
                        </a>
                        <p style={{ marginLeft: "20px", fontSize: "15px" }}>Members: {team && team.teamSize}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        } else if (this.state.judgesContentFlag) {
            content = this.state.judgeDetails && this.state.judgeDetails.map(judge => {
                return (
                    <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop: "10px", boxShadow: "5px 5px #FAFAFA", padding: "8px", marginLeft: "20px" }}>
                        <a href="#" id={judge.judgeId}>
                            <Title level={4} style={{ marginLeft: "20px" }}> Name : {judge && judge.judgeName}</Title>
                        </a>
                        <p style={{ marginLeft: "20px", fontSize: "15px" }}>Screen name: {judge && judge.judgeScreenName}</p>
                        <p style={{ marginLeft: "20px", fontSize: "15px" }}>Email id: {judge && judge.judgeEmail}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        } else if (this.state.sponsorsContentFlag) {
            content = this.state.sponsorDetails && this.state.sponsorDetails.map(sponsor => {
                return (
                    <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop: "10px", boxShadow: "5px 5px #FAFAFA", padding: "8px", marginLeft: "20px" }}>
                        <a href="#" id={sponsor.sponsorId}>
                            <Title level={4} style={{ marginLeft: "20px" }}> Organization Name : {sponsor && sponsor.sponsorName}</Title>
                        </a>
                        <p style={{ marginLeft: "20px", fontSize: "15px" }}>Description : {sponsor && sponsor.sponsorDescription}</p>

                        <Divider></Divider>
                    </div>
                )
            })
        }
        if (this.state.userTeam) {
            console.log(this.state.userTeam)
            teamModalContent = this.state.userTeam.map(member => {
                return (
                    <Col span={6}>
                        <Avatar shape="square" icon="user" />
                        <p><b>{member.name}</b></p>
                        <p>{member.title}</p>
                    </Col>
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
                <Button type="primary" size="large" style={{ marginTop: "10px" }} onClick={this.showSubmissionModal} disabled={submissionButtonFlag}>Submit/Edit Work</Button>
                <Modal
                    title="Submission"
                    visible={this.state.visibleSubmissionModal}
                    onCancel={this.handleCancel}
                    footer={[
                        <Button key="back" onClick={this.handleCancel}>Back</Button>,
                    ]}
                >
                    <Form>
                        <Form.Item
                            label="Submission URL"
                        >
                            {getFieldDecorator('submissionUrl', {
                                rules: [{ required: true, message: "URL cannot be empty" }],
                            })(
                                <input type="text" disabled={submissionButtonFlag} value={this.state.submissionUrl} onChange={this.handleSubmission} placeholder={this.state.submissionUrl} />
                            )}
                        </Form.Item>
                        <Form.Item>
                            <Button
                                type="primary"
                                htmlType="submit"
                                disabled={submissionButtonFlag}
                                onClick={this.submitWork}
                            >
                                Submit
                            </Button>
                        </Form.Item>
                    </Form>
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
                    style={{ height: "300px" }}
                >
                    <div style={{ "height": "50px" }}>
                        <Row type="flex">
                            {teamModalContent}
                        </Row>
                    </div>
                </Modal>
                <p class="text-warning large">Team Payment Due</p>
            </div>
        } else if (this.state.message === "judge") {
            buttons = <div>
                <Link to={`/hacker/gradeSubmissions/${this.props.match.params.id}`}>
                    <Button type="primary" size="large" style={{ marginTop: "25%" }} disabled={gradeButtonFlag}>Grade Submission</Button>
                </Link>
            </div>
        } else {
            buttons = <div>
                <Link to={`/hackathon/register/${this.props.match.params.id}`}><Button type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.showTeamModal} disabled={registerButtonFlag}>Register</Button><br /></Link>
            </div>
        }

        let resultsButton = null
        var enddateconv = new Date(this.state.endDate)
        var end_sec = enddateconv.getTime()
        let currentDate = Date.now()
        if (currentDate > end_sec) {
            resultsButton = <Button className="mx-2" type="primary" shape="round" size="large" style={{ marginTop: "5%" }} onClick={this.routeToResults}>Results</Button>
        } else {
            resultsButton = <Button className="mx-2" type="primary" shape="round" size="large" style={{ marginTop: "5%" }} disabled>Results</Button>
        }
        return (
            <div>
                {redirect}
                <NavBar></NavBar>
                <div style={{ backgroundImage: "url(" + this.state.cover_image + ")", height: "300px", position: "relative" }}>
                    <h3 class="hackathon-name"><b>{this.state.name}</b></h3>
                </div>
                <div style={{ backgroundColor: "#EAECEE", height: "200px" }}>
                    <div style={{ marginLeft: "10%" }}>

                        <Row type="flex">
                            {loaded}
                            {detailsContent}
                            <Col span={6}>
                                {buttons}
                                {resultsButton}
                            </Col>
                        </Row>

                    </div>
                </div>
                <div>
                    <Layout style={{ minHeight: "330px" }}>
                        <Sider style={{ overflow: 'auto', height: '100%', left: 0, marginBottom: "20px" }}>
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
                                <br></br>
                            </Menu>
                        </Sider>
                        <br></br>
                        <Layout style={{ marginLeft: 10 }} >
                            <Content style={{ margin: '0px 16px 0', overflow: 'initial' }}>
                                <div style={{ padding: 14, background: '#fff', minHeight: "300px" }}>
                                    {loaded}
                                    {content}
                                </div>
                            </Content>
                        </Layout>
                    </Layout>
                </div>
            </div>
        )
    }
}

export default Form.create()(HackathonDetails);