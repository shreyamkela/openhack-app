import React, { Component } from 'react'
import '../../../App.css'
import { Link } from 'react-router-dom'
import NavBar from '../../Navbar/Navbar';
import { Layout, Menu, Icon, Row, Col, Button, Modal, Divider, Avatar, Form, Input, message } from 'antd';
import axios from 'axios'
import Title from 'antd/lib/typography/Title';
import swal from 'sweetalert';
import {Redirect} from 'react-router'
import API from '../../../utils/API'
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
            userTeam:[],
            userTeamId:null,
            aboutContentFlag: true,
            teamsContentFlag: false,
            judgesContentFlag: false,
            sponsorsContentFlag:false,
            visibleTeamModal: false,
            visibleSubmissionModal: false,
            submissionUrl: null, // IDK right now
            submissionButtonFlag:false,
            gradeButtonFlag:false,
            registerButtonFlag:false,
            hackathonId: this.props.match.params.id,
            winner: ""
        }
    }

    componentDidMount() {

        console.log("Printing the userID that was stored in the local storage", localStorage.getItem("userId"))
        let body = {
            "userId": localStorage.getItem("userId")
            // "userId": "5"
        }
        let hackathonId = this.props.match.params.id
        console.log("from route", hackathonId)
        axios.defaults.withCredentials = true
        axios.post(`http://localhost:8080/hackathon/${hackathonId}`, body)
            .then(response => {
                if (response.status === 200) {
                    console.log("\n\nPrinting the whole response data\n")
                    console.log(JSON.stringify(response.data))
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
                        userTeam:response.data.userTeam,
                        userTeamId:response.data.userTeamId,
                        discount: response.data.discount,
                        message: response.data.message,
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
        console.log(this.state.submissionUrl)
        let body = {
            "hackathonId":this.props.match.params.id,
            "teamId":this.state.userTeamId,
            "url":this.state.submissionUrl
        }

        axios.post("http://localhost:8080/addSubmission",body)
            .then(response => {
                if(response.status===200){
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


    handleHackathonOpen = async () => {// TODO
        
        console.log("Open hackathon: ", this.state.hackathonId)
        console.log("Start date, end date: ",this.state.startDate, this.state.endDate)
        if (this.state.startDate === undefined || this.state.endDate === undefined) {
            message.error("Unable to open hackathon at the moment. Please refresh the page and try again.")
        } 
        else {
            var startdateconv = new Date(this.state.startDate);
            var start_sec = startdateconv.getTime() 
            var enddateconv = new Date(this.state.endDate)
            var end_sec = enddateconv.getTime() 
            let currentDate = Date.now()
            console.log("\nstart_sec : "+start_sec+ "end_sec : "+end_sec+ "curr_sec : "+currentDate)
            if (currentDate > start_sec && currentDate < end_sec) { // Cannot open as already open as past start date
                message.warning("Hackathon already open!");
            } else if (currentDate > end_sec) { // If hackathon already closed, then cannot open it again - Dont need to go to backend, just check whether end date is less than today date. If end date is less then it has already been closed 
                // TODO DO WE HAVE TO HANDLE THIS - IF AFTER CLOSING A HACKATHON, NO GRADE HAS BEEN ASSIGNED YET, THEN IT CAN BE OPENED AGAIN?
                message.warning("Hackathon cannot be reopened past the end or closing date!");
            }
            else if (currentDate < start_sec) {
                // If hackathon is opened before the starting date then change the starting date at the backend and open the hackathon
                let body = {
                    "hackathonId": this.state.hackathonId,
                    "currentDate": currentDate,
                    "userId": localStorage.getItem("userId")
                }
                try {
                    let response = await API.post(`hackathon/open`, body);
                    console.log("Response: ", response.data);
                    message.success("Hackathon open for submission!")
                } catch (error) {
                    console.log(error.response);
                    message.error("Unable to open hackathon at the moment. Please refresh the page and try again.")
                }
            }
        }
    }

    handleHackathonClose = async () => {// TODO
        console.log("Close hackathon: ", this.state.hackathonId)
        console.log("Start date, end date: ",this.state.startDate, this.state.endDate)

        if (this.state.startDate === undefined || this.state.endDate === undefined) {
            message.error("Unable to close hackathon at the moment. Please refresh the page and try again.")
        } else {
            var startdateconv = new Date(this.state.startDate);
            var start_sec = startdateconv.getTime() 
            var enddateconv = new Date(this.state.endDate)
            var end_sec = enddateconv.getTime() 
            let currentDate = Date.now()
            
            console.log("\nstart_sec : "+start_sec+ "end_sec : "+end_sec+ "curr_sec : "+currentDate)
            if (currentDate > end_sec) { // Past end date, hackathon is already closed
                message.warning("Hackathon already closed!");
            } else if (currentDate < start_sec) { // Cannot close hackathon before the starting date
                message.warning("Cannot close hackathon before the starting date!");
            } else if (currentDate > start_sec && currentDate < end_sec) { // If hackathon is closed between the starting and ending date then change the end date at the backend
                console.log("Close hackathon for submission - change end date to current date: ", this.state.hackathonId)
                // NOTE Grade can only be assigned after the end date/close for submission date
                // NOTE Check whether submissions for all teams have been received. If all submissions have not been received then cannot close hackathon before the original end date
                // Change end date at the backend
                let body = {
                    "hackathonId": this.state.hackathonId,
                    "currentDate": currentDate,
                    "userId": localStorage.getItem("userId")
                }
                try {
                    let response = await API.post(`hackathon/close`, body);
                    console.log("Response: ", response.data);
                    message.success("Hackathon closed for submission!")
                } catch (error) {
                    console.log(error.response);
                    console.log("Error status code: ", error.status);
                    if (error.status === 400) {
                        message.warning("All team submissions not received, therefore unable to close hackathon before the original end date.")
                    } else {
                        message.error("Unable to close hackathon at the moment. Please refresh the page and try again.")
                    }
                }
            }

        }

    }

    handleHackathonFinalize = async () => { // TODO
        console.log("Finalize hackathon: ", this.state.hackathonId)
        console.log("Start date, end date: ",this.state.startDate, this.state.endDate)

        if (this.state.startDate === undefined || this.state.endDate === undefined) {
            message.error("Unable to finalize hackathon at the moment. Please refresh the page and try again.")
        } else {
            var startdateconv = new Date(this.state.startDate);
            var start_sec = startdateconv.getTime() 
            var enddateconv = new Date(this.state.endDate)
            var end_sec = enddateconv.getTime() 
            let currentDate = Date.now()

            if (currentDate < end_sec) {        // Cannot finalize before the end date
                message.warning("Cannot finalize hackathon before closing for submission!");
            } else {        // Declare the winner by comparing all grades
                console.log("Finalize hackathon - declare the winner: ", this.state.hackathonId)
                // NOTE If all team grades have been received, then finalize and compare the grades and get the winner. Else throw error
                // TODO check for tie?
                let params = {
                    "hackathonId": this.state.hackathonId,
                    "userId": localStorage.getItem("userId")
                }
                try {
                    let response = await API.post(`hackathon/finalize`, params ) // TODO check params
                    console.log("Response: ", response.data);
                    message.success("Hackathon finalized! The winner is Team:", response.data.teamName, "!")
                    this.setState({ winner: response.data })
                } catch (error) {
                    console.log(error.response);
                    console.log("Error status code: ", error.status);
                    if (error.status === 400) {
                        message.warning("All team grades not received, therefore unable to finalize hackathon.")
                    } else {
                        message.error("Unable to finalize hackathon at the moment. Please refresh the page and try again.")
                    }
                }
            }
        }
    }


    render() {
        var content = null
        var buttons = null
        var teamModalContent = null
        var registerButtonFlag = false
        var submissionButtonFlag = false
        var gradeButtonFlag = false
        var redirect = null
        if(!localStorage.getItem("userId")){
            redirect = <Redirect to="/login"/>
        }
        const {getFieldDecorator} = this.props.form
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
                <p><b>Fee</b>: ${this.state.fee}</p>
                <p><b>Sponsor Discount</b>: {this.state.discount}%</p>
            </div>
        } else if (this.state.teamsContentFlag) {
            content = this.state.teamDetails && this.state.teamDetails.map(team => {
                // return(
                //     <div>
                //         <a href="#" id={team.teamId}> 
                //             <Title level={4}>Team name : {team && team.teamName}</Title>
                //         </a>
                //         <p>Members: {team && team.teamSize}</p>
                //         <Divider></Divider>
                //     </div>
                // )
                return(
                    <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop:"10px", boxShadow:"5px 5px #FAFAFA", padding:"8px", marginLeft:"20px" }}>
                        <a href="#" id={team.teamId}> 
                            <Title level={4} style = {{marginLeft : "20px"}}>Team name : {team && team.teamName}</Title>
                        </a>
                        <p style = {{marginLeft : "20px", fontSize : "15px"}}>Members: {team && team.teamSize}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        }else if(this.state.judgesContentFlag){
            content = this.state.judgeDetails && this.state.judgeDetails.map(judge => {
                // return(
                //     <div>
                //         <a href="#" id={judge.judgeId}> 
                //             <Title level={4}>{judge && judge.judgeName}</Title>
                //         </a>
                //         <p>{judge && judge.judgeScreenName}, {judge && judge.judgeEmail}</p>
                //         <Divider></Divider>
                //     </div>
                // )
                return(
                    <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop:"10px", boxShadow:"5px 5px #FAFAFA", padding:"8px", marginLeft:"20px" }}>
                        <a href="#" id={judge.judgeId}> 
                            <Title level={4} style = {{marginLeft : "20px"}}> Name : {judge && judge.judgeName}</Title>
                        </a>
                        <p style = {{marginLeft : "20px", fontSize : "15px"}}>Screen name: {judge && judge.judgeScreenName}</p>
                        <p style = {{marginLeft : "20px", fontSize : "15px"}}>Email id: {judge && judge.judgeEmail}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        }else if(this.state.sponsorsContentFlag){
            content = this.state.sponsorDetails && this.state.sponsorDetails.map(sponsor => {
                // return(
                //     <div>
                //         <a href="#" id={sponsor.sponsorId}> 
                //             <Title level={4}>{sponsor && sponsor.sponsorName}</Title>
                //         </a>
                //         <p>{sponsor && sponsor.sponsorDescription}</p>
                //         <Divider></Divider>
                //     </div>
                // )
                return(
                    <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop:"10px", boxShadow:"5px 5px #FAFAFA", padding:"8px", marginLeft:"20px" }}>
                        <a href="#" id={sponsor.sponsorId}> 
                            <Title level={4} style = {{marginLeft : "20px"}}> Organization Name : {sponsor && sponsor.sponsorName}</Title>
                        </a>
                        <p style = {{marginLeft : "20px", fontSize : "15px"}}>Description : {sponsor && sponsor.sponsorDescription}</p>
                        
                        <Divider></Divider>
                    </div>
                )
            })
        }
        if(this.state.userTeam){
            console.log(this.state.userTeam)
            teamModalContent = this.state.userTeam.map(member => {
                return(
                    <Col span={6}>
                        <Avatar shape="square" icon="user"/>
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
                        <input type="text" disabled={submissionButtonFlag} value={this.state.submissionUrl} onChange={this.handleSubmission} placeholder={this.state.submissionUrl}/>
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
                    style={{height:"300px"}}
                >
                    <div style={{"height":"50px"}}>
                        <Row type="flex">
                            {teamModalContent}
                        </Row>
                    </div>
                </Modal>
                <p class="text-warning large">Team Payment Due</p>
            </div>
        }else if(this.state.message === "judge"){
            buttons = <div>
                <Link to="/hackathon/grade">
                    <Button type="primary" size="large" style={{ marginTop: "25%" }} disabled={gradeButtonFlag}>Grade Submission</Button>
                </Link>
            </div>
        } 
        else if (this.state.message === "admin") {

            buttons = <div>
                <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonOpen}>Open</Button><Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonClose}>Close</Button>
                <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonFinalize}>Finalize</Button><br />
            </div>
        } 
        else {
            buttons = <div>
                <Link to={`/hackathon/register/${this.props.match.params.id}`}><Button type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.showTeamModal} disabled={registerButtonFlag}>Register</Button><br /></Link>
            </div>
        }

        console.log("Printing this.state.message : ",this.state.message)

        // if (this.state.message === "admin") {

        //     buttons = <div>
        //         <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonOpen}>Open</Button><Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonClose}>Close</Button>
        //         <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonFinalize}>Finalize</Button><br />
        //     </div>
        // }

        // Show winner after finalizing - TODO Test this
        let winner = null;
        if (this.state.winner !== "" && this.state.winner !== undefined) {
            winner = <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="team" /><b> Winner : {this.state.winner}</b></p>
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
                            <Col span={18}>
                                <p style={{ color: "#46535e", fontSize: "15px", paddingTop: "4%" }}> <Icon type="user" />  Allowed team size : {this.state.teamSizeMin} - {this.state.teamSizeMax}</p>
                                <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="calendar" /> Starts on : {new Date(this.state.startDate).toDateString()}</p>
                                <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="calendar" /> Ends on : {new Date(this.state.endDate).toDateString()}</p>
                            </Col>
                            <Col span={6}>
                                {buttons}
                            </Col>
                        </Row>

                    </div>
                </div>
                <div>
                    <Layout style = {{minHeight : "330px"}}>
                        <Sider style={{ overflow: 'auto', height: '100%', left: 0, marginBottom:"20px" }}>
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
                        <Layout style={{ marginLeft: 10}} >
                            <Content style={{ margin: '0px 16px 0', overflow: 'initial'}}>
                                <div style={{ padding: 14, background: '#fff', minHeight : "300px"}}>
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
