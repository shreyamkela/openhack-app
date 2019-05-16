import React, { Component } from 'react'
import '../../../App.css'
import { Link } from 'react-router-dom'
import NavBar from '../../Navbar/Navbar';
import { Layout, Menu, Icon, Row, Col, Button, Modal, Divider, message } from 'antd';
import API from '../../../utils/API'
import Title from 'antd/lib/typography/Title';
const { Content, Sider } = Layout;

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
            sponsorsContentFlag: false,
            hackathonId: this.props.match.params.id,
            winner: ""
        }
    }

    componentDidMount() {
        let body = {
            "userId": localStorage.getItem("userId")
        }
        console.log("from route", this.state.hackathonId)
        API.post(`hackathon/${this.state.hackathonId}`, body)
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
                        message: "admin", // TODO
                        teamDetails: response.data.teamDetails,
                        judgeDetails: response.data.judgeDetails,
                        sponsorDetails: response.data.sponsorDetails,
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


    handleHackathonOpen = async () => {// TODO
        console.log("Open hackathon: ", this.state.hackathonId)
        console.log("Start date, end date: ", this.state.startDate, this.state.endDate)

        if (this.state.startDate === undefined || this.state.endDate === undefined) {
            message.error("Unable to open hackathon at the moment. Please refresh the page and try again.")
        } else {
            let currentDate = Date.now();
            if (currentDate > this.state.startDate && currentDate < this.state.endDate) { // Cannot open as already open as past start date
                message.warning("Hackathon already open!");
            } else if (currentDate > this.state.endDate) { // If hackathon already closed, then cannot open it again - Dont need to go to backend, just check whether end date is less than today date. If end date is less then it has already been closed 
                // TODO DO WE HAVE TO HANDLE THIS - IF AFTER CLOSING A HACKATHON, NO GRADE HAS BEEN ASSIGNED YET, THEN IT CAN BE OPENED AGAIN?
                message.warning("Hackathon cannot be reopened past the end or closing date!");
            }
            else if (currentDate < this.state.startDate) {
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
        console.log("Start date, end date: ".this.state.startDate, this.state.endDate)

        if (this.state.startDate === undefined || this.state.endDate === undefined) {
            message.error("Unable to close hackathon at the moment. Please refresh the page and try again.")
        } else {
            let currentDate = Date.now();

            if (currentDate > this.state.endDate) { // Past end date, hackathon is already closed
                message.warning("Hackathon already closed!");
            } else if (currentDate < this.state.startDate) { // Cannot close hackathon before the starting date
                message.warning("Cannot close hackathon before the starting date!");
            } else if (currentDate > this.state.startDate && currentDate < this.state.endDate) { // If hackathon is closed between the starting and ending date then change the end date at the backend
                console.log("Close hackathon for submission - change end date to current date: ", this.state.hackathonId)
                // NOTE Grade can only be assigned after the end date/close for submission date
                // NOTE Check whether submissions for all teams have been received. If all submissions have not been received then cannot close hackathon before the original end date
                // TODO - In requirement "An admin can close the submission after it is open, even before the end date. Once a hackathon is closed and any grade has been given by any judge to any submission, the hackathon cannot be re-opened for submission again" - DOES THIS MEAN THAT IF AFTER CLOSING A HACKATHON, NO GRADE HAS BEEN ASSIGNED YET, THEN IT CAN BE MADE OPEN AGAIN?
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
        console.log("Start date, end date: ".this.state.startDate, this.state.endDate)

        if (this.state.startDate === undefined || this.state.endDate === undefined) {
            message.error("Unable to finalize hackathon at the moment. Please refresh the page and try again.")
        } else {
            let currentDate = Date.now();

            if (currentDate < this.state.endDate) {        // Cannot finalize before the end date
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
                    let response = await API.get(`hackathon/finalize`, { params: params }) // TODO check params
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
        if (this.state.aboutContentFlag) {
            content = <div>
                <p><b>Overview</b>: {this.state.description}</p>
                <p><b>Fee</b>: {this.state.fee}</p>
                <p><b>Sponsor Discount</b>: {this.state.discount}</p>
            </div>
        } else if (this.state.teamsContentFlag) {
            content = this.state.teamDetails && this.state.teamDetails.map(team => {
                return (
                    <div>
                        <a href="#" id={team.teamId}>
                            <Title level={4}>{team && team.teamName}</Title>
                        </a>
                        <p>Members: {team && team.teamSize}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        } else if (this.state.judgesContentFlag) {
            content = this.state.judgeDetails && this.state.judgeDetails.map(judge => {
                return (
                    <div>
                        <a href="#" id={judge.judgeId}>
                            <Title level={4}>{judge && judge.judgeName}</Title>
                        </a>
                        <p>{judge && judge.judgeScreenName}, {judge && judge.judgeEmail}</p>
                        <Divider></Divider>
                    </div>
                )
            })
        } else if (this.state.sponsorsContentFlag) {
            content = this.state.sponsorDetails && this.state.sponsorDetails.map(sponsor => {
                return (
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
        if (this.state.team) {
            teamModalContent = <p>Team Name</p>
        }
        if (this.state.message === "admin") {
            buttons = <div>
                <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonOpen}>Open</Button><Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonClose}>Close</Button>
                <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonFinalize}>Finalize</Button><br />
            </div>
        }


        // Show winner after finalizing - TODO Test this
        let winner = null;
        if (this.state.winner !== "" && this.state.winner.teamName !== undefined) {
            winner = <p style={{ color: "#46535e", fontSize: "15px" }}><Icon type="team" /><b> Winner : {this.state.winner.teamName}</b></p>
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
                                <div style={{ padding: 14, background: '#fff' }}>
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