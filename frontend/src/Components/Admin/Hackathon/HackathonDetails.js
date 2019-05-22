import React, { Component } from 'react'
import '../../../App.css'
import { Link } from 'react-router-dom'
import NavBar from '../../Navbar/Navbar';
import { Layout, Menu, Icon, Row, Col, Button, Modal, Divider, Avatar, Form, Input, message, Skeleton } from 'antd';
import axios from 'axios'
import Title from 'antd/lib/typography/Title';
import swal from 'sweetalert';
import Swal from 'sweetalert2'
import { Redirect } from 'react-router'
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
            userTeam: [],
            userTeamId: null,
            aboutContentFlag: true,
            teamsContentFlag: false,
            judgesContentFlag: false,
            sponsorsContentFlag: false,
            paymentReportContentFlag: false,
            revenueReportContentFlag: false,
            expenseReportContentFlag: false,
            visibleTeamModal: false,
            visibleSubmissionModal: false,
            submissionUrl: null, // IDK right now
            submissionButtonFlag: false,
            gradeButtonFlag: false,
            registerButtonFlag: false,
            hackathonId: this.props.match.params.id,
            winnerTeam: null,
            isLoaded: false,
            isFinalized: false,
            visibleInviteModal: false,
            paymentReport: [],
            paidRevenue: null,
            unpaidRevenue: null,
            sponsorRevenue: null,
            expenseDetails: [],
            totalExpense: null,
            expenseModalVisible: false
        }
    }

    componentDidMount() {

        console.log("Printing the userID that was stored in the local storage", localStorage.getItem("userId"))
        let body = {
            "userId": localStorage.getItem("userId")
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
                        userTeam: response.data.userTeam,
                        userTeamId: response.data.userTeamId,
                        discount: response.data.discount,
                        message: response.data.message,
                        teamDetails: response.data.teamDetails,
                        judgeDetails: response.data.judgeDetails,
                        sponsorDetails: response.data.sponsorDetails,
                        submissionUrl: response.data.submissionUrl,
                        winnerTeam: response.data.winnerTeam,
                        isLoaded: true,
                        isFinalized: response.data.isFinalize
                    })
                }
            })
            .catch(err => {
                console.log(err)
            })

        let body2 = {
            "hackathonId": this.state.hackathonId
        }
        axios.post("http://localhost:8080/payment/report", body2)
            .then(response => {
                console.log("report is: ", response.data)
                if (response.status === 200) {
                    this.setState({
                        "paymentReport": response.data.paymentReport,
                        "paidRevenue": response.data.paidRevenue,
                        "unpaidRevenue": response.data.unpaidRevenue,
                        "sponsorRevenue": response.data.sponsorRevenue
                    })
                }
            })
            .catch(err => {
                console.log(err);
            })

        axios.get(`http://localhost:8080/hackathon/expenseDetails/${this.state.hackathonId}`)
            .then(response => {
                if(response.status === 200){
                    console.log("expense Details",response.data)
                    this.setState({
                        totalExpense:response.data.totalExpense,
                        expenseDetails:response.data.expenseDetails
                    })
                }
            })
    }

    loadAboutContent = (e) => {
        this.setState({
            aboutContentFlag: true,
            teamsContentFlag: false,
            judgesContentFlag: false,
            paymentReportContentFlag: false,
            revenueReportContentFlag: false,
            expenseReportContentFlag: false,
            sponsorsContentFlag: false
        })
    }
    loadJudgesContent = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag: false,
            paymentReportContentFlag: false,
            revenueReportContentFlag: false,
            expenseReportContentFlag: false,
            judgesContentFlag: true
        })
    }
    loadTeamsContent = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: true,
            sponsorsContentFlag: false,
            paymentReportContentFlag: false,
            revenueReportContentFlag: false,
            expenseReportContentFlag: false,
            judgesContentFlag: false
        })
    }

    loadSponsorsContent = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag: true,
            paymentReportContentFlag: false,
            revenueReportContentFlag: false,
            expenseReportContentFlag: false,
            judgesContentFlag: false
        })
    }

    loadPaymentReport = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag: false,
            judgesContentFlag: false,
            revenueReportContentFlag: false,
            expenseReportContentFlag: false,
            paymentReportContentFlag: true
        })
    }

    loadRevenueReport = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag: false,
            judgesContentFlag: false,
            revenueReportContentFlag: true,
            expenseReportContentFlag: false,
            paymentReportContentFlag: false
        })
    }

    loadExpenseReport = (e) => {
        this.setState({
            aboutContentFlag: false,
            teamsContentFlag: false,
            sponsorsContentFlag: false,
            judgesContentFlag: false,
            revenueReportContentFlag: false,
            expenseReportContentFlag: true,
            paymentReportContentFlag: false
        })
    }


    handleHackathonOpen = async () => {

        console.log("Open hackathon: ", this.state.hackathonId)
        console.log("Start date, end date: ", this.state.startDate, this.state.endDate)
        if (this.state.startDate === undefined || this.state.endDate === undefined) {
            message.error("Unable to open hackathon at the moment. Please refresh the page and try again.")
        }
        else {
            var startdateconv = new Date(this.state.startDate);
            var start_sec = startdateconv.getTime()
            var enddateconv = new Date(this.state.endDate)
            var end_sec = enddateconv.getTime()
            let currentDate = Date.now()
            console.log("\nstart_sec : " + start_sec + "end_sec : " + end_sec + "curr_sec : " + currentDate)
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

                    Swal.fire({
                        title: 'Opening Hackathon',
                        text: 'In progress...',
                        showConfirmButton: false
                    })
                    let response = await API.post(`hackathon/open`, body);
                    console.log("Response: ", response.data);
                    Swal.close(); // Close the Swal when reponse has been fetched
                    message.success("Hackathon open for submission!")
                    setTimeout(2000)
                    window.location.reload();
                } catch (error) {
                    Swal.close(); // Close the Swal when reponse has been fetched
                    console.log(error.response);
                    message.error("Unable to open hackathon at the moment. Please refresh the page and try again.")
                }
            }
        }
    }

    handleHackathonClose = async () => {
        console.log("Close hackathon: ", this.state.hackathonId)
        console.log("Start date, end date: ", this.state.startDate, this.state.endDate)

        if (this.state.startDate === undefined || this.state.endDate === undefined) {
            message.error("Unable to close hackathon at the moment. Please refresh the page and try again.")
        } else {
            var startdateconv = new Date(this.state.startDate);
            var start_sec = startdateconv.getTime()
            var enddateconv = new Date(this.state.endDate)
            var end_sec = enddateconv.getTime()
            let currentDate = Date.now()

            console.log("\nstart_sec : " + start_sec + "end_sec : " + end_sec + "curr_sec : " + currentDate)
            if (currentDate > end_sec) { // Past end date, hackathon is already closed
                message.warning("Hackathon already closed!");
            } else if (currentDate < start_sec) { // Cannot close hackathon before the starting date
                message.warning("Cannot close hackathon before the starting date!");
            } else if (currentDate > start_sec && currentDate < end_sec) { // If hackathon is closed between the starting and ending date then change the end date at the backend
                console.log("Close hackathon for submission - change end date to current date: ", this.state.hackathonId)
                // NOTE if no teams have participated yet, then cannot close hackathon before the end date
                if (this.state.teamDetails[0] == undefined) {
                    message.warning("No teams have participated yet! Please wait until the original end date.")
                    return
                }


                // NOTE Grade can only be assigned after the end date/close for submission date
                // NOTE Check whether submissions for all teams have been received. If all submissions have not been received then cannot close hackathon before the original end date
                // Change end date at the backend
                let body = {
                    "hackathonId": this.state.hackathonId,
                    "currentDate": currentDate,
                    "userId": localStorage.getItem("userId")
                }
                try {
                    Swal.fire({
                        title: 'Closing Hackathon',
                        text: 'In progress...',
                        showConfirmButton: false
                    })
                    let response = await API.post(`hackathon/close`, body);
                    console.log("Response: ", response.data);
                    Swal.close(); // Close the Swal when reponse has been fetched
                    message.success("Hackathon closed for submission!")
                    setTimeout(2000)
                    window.location.reload();
                } catch (error) {
                    console.log(error.response);
                    console.log("Error status code: ", error.response.status);
                    Swal.close(); // Close the Swal when reponse has been fetched
                    if (error.response.status === 400) {
                        message.warning("All team submissions not received yet! Please wait until the original end date.")
                    } else {
                        message.error("Unable to close hackathon at the moment. Please refresh the page and try again.")
                    }
                }
            }

        }

    }

    handleHackathonFinalize = async () => {
        console.log("Finalize hackathon: ", this.state.hackathonId)
        console.log("Start date, end date: ", this.state.startDate, this.state.endDate)

        if (this.state.isFinalized === true) {
            message.warning("Hackathon already finalized!");
            return
        }

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
                    Swal.fire({
                        title: 'Finalizing Hackathon',
                        text: 'In progress...Wait for few minutes',
                        showConfirmButton: false
                    })
                    let response = await API.post(`hackathon/finalize`, params)
                    console.log("Response: ", response.data);
                    Swal.close();
                    message.success("Hackathon finalized! The winner is Team: " + response.data.winner + "!")
                    this.setState({ winnerTeam: response.data.winner })
                } catch (error) {
                    console.log(error.response);
                    console.log("Error status code: ", error.response.status);
                    Swal.close(); // Close the Swal when reponse has been fetched
                    if (error.response.status === 400) {
                        message.warning("All submission grades not received yet! Cannot finalize hackathon.")
                    } else {
                        message.error("Unable to finalize hackathon at the moment. Please refresh the page and try again.")
                    }
                }
            }
        }
    }

    routeToResults = () => {
        this.props.history.push(`/hackathon_details/${this.state.hackathonId}/results`);
    }
    addExpenseModal = () => {
        console.log("\nAdd Expense Button Clicked ");
        this.setState({
            expenseModalVisible: true,
        });



    }
    handleCancel = () => {
        console.log('Clicked cancel button');
        this.setState({
            expenseModalVisible: false,
        });
        window.location.reload();
    }
    createExpense = (e) => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                values.hackathonId =this.state.hackathonId;
                values.time=Date.now();
                console.log("\nSending the Expense object data to the backend\n")
                console.log(JSON.stringify(values))
                axios.defaults.withCredentials = true;
                axios.post('http://localhost:8080/hackathon/addExpense', values)
                    .then(response => {
                        console.log("Status Code : ", response.status);

                        if (response.status === 200) {
                            Swal.close();
                            swal("Expense created successfully", "", "success");
                            console.log(JSON.stringify(response.data));
                        }
                        else {
                            Swal.close();
                            swal("There was some error creating the Expense", "", "error");
                        }
                    })
                    .catch(err => {
                        console.log(err)
                        Swal.close();
                        swal("bad request for creating the Expense", "", "error");
                    });
            }
        });


    }

    showInviteModal = () => {
        console.log("Show invite modal!")
        this.setState({
            visibleInviteModal: true
        });
    }

    handleInviteModalCancel = e => {
        //console.log(e);
        this.setState({
            visibleInviteModal: false,
        });
    };

    handleInviteModalOk = e => {
        //console.log(e);
        e.preventDefault();
        this.props.form.validateFields(async (err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);

                // Cannot invite a new user if hackathon has been declared open
                var startdateconv = new Date(this.state.startDate)
                var start_sec = startdateconv.getTime()
                let currentDate = Date.now()
                if (currentDate > start_sec) {
                    message.warning("Cannot send invitation as the Hackathon is open for submission!")
                } else {
                    if (values.email.includes("@sjsu.edu")) {
                        message.warning("Cannot send invitation to an SJSU email Id. Please invite users with a non-SJSU email id.")
                        setTimeout(2000)
                    } else if (values.email.includes(".") === false || values.email.includes("@") === false) {
                        message.error("Please enter a valid email id!")
                    } else {
                        let body = {
                            "hackathonId": this.props.match.params.id,
                            "userId": localStorage.getItem("userId"),
                            "inviteEmail": values.email
                        }
                        try {
                            Swal.fire({
                                title: 'Sending Invite',
                                text: 'In progress...',
                                showConfirmButton: false
                            })
                            let response = await API.post(`hackathon/invite`, body);
                            console.log("Response: ", response.data);
                            Swal.close(); // Close the Swal when reponse has been fetched
                            message.success("Invititation sent!")
                            setTimeout(2000)
                            this.setState({ visibleInviteModal: false })
                        } catch (error) {
                            Swal.close(); // Close the Swal when reponse has been fetched
                            console.log(error.response);
                            if (error.response.status === 400) {
                                message.warning("Input email id already registered!")
                            } else {
                                message.error("Unable to send invitation at the moment. Please refresh the page and try again.")
                            }
                        }

                    }

                }
            }
        });
    };



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
        var paymentReportTable = null
        var revenueReportTable = null
        var expenseReportTable = null
        const { expenseModalVisible } = this.state;
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
        if (this.state.paymentReport && this.state.paymentReport.length > 0) {
            paymentReportTable = this.state.paymentReport.map(payment => {
                return (
                    <tr>
                        <th scope="row">{payment.userName} - {payment.userEmail}</th>
                        <td>{payment.userTeam}</td>
                        <td>{payment.fee}</td>
                        <td>{payment.status}</td>
                        <td>{new Date(payment.paymentDate).toDateString()} {new Date(payment.paymentDate).toLocaleTimeString()}</td>
                    </tr>
                )
            })
        } else {
            paymentReportTable = <tr>
                <th>
                    No Data
                </th>
            </tr>
        }

        if(this.state.expenseDetails && this.state.expenseDetails.length>0){
            expenseReportTable = this.state.expenseDetails.map(expense => {
                return(
                    <tr>
                        <th scope="row">{expense.title}</th>
                        <td>{expense.description}</td>
                        <td>{expense.amount}</td>
                        <td>{new Date(expense.time).toDateString()} {new Date(expense.time).toLocaleTimeString()}</td>
                    </tr>
                )
            })
        }else{
            expenseReportTable = <tr>
            <th>
                No Expense Added
            </th>
        </tr> 
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
                // return(
                //     <div>
                //         <a href="#" id={judge.judgeId}> 
                //             <Title level={4}>{judge && judge.judgeName}</Title>
                //         </a>
                //         <p>{judge && judge.judgeScreenName}, {judge && judge.judgeEmail}</p>
                //         <Divider></Divider>
                //     </div>
                // )
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
                // return(
                //     <div>
                //         <a href="#" id={sponsor.sponsorId}> 
                //             <Title level={4}>{sponsor && sponsor.sponsorName}</Title>
                //         </a>
                //         <p>{sponsor && sponsor.sponsorDescription}</p>
                //         <Divider></Divider>
                //     </div>
                // )
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
        } else if (this.state.paymentReportContentFlag) {
            content = <div>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">Member</th>
                            <th scope="col">Team</th>
                            <th scope="col">Fee</th>
                            <th scope="col">Status</th>
                            <th scope="col">Payment Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        {paymentReportTable}
                    </tbody>
                </table>
            </div>
        } else if (this.state.revenueReportContentFlag) {
            content = <div>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">Metric</th>
                            <th scope="col">Revenue/Total Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td scope="row">Total Paid Revenue from Registration Fees</td>
                            <td>${this.state.paidRevenue}</td>
                        </tr>
                        <tr>
                            <td scope="row">Total Revenue from Sponsors</td>
                            <td>${this.state.sponsorRevenue}</td>
                        </tr>
                        <tr>
                            <td scope="row">Total Unpaid Revenue from Registration Fees</td>
                            <td>${this.state.unpaidRevenue}</td>
                        </tr>
                        <tr>
                            <td scope="row">Total Expense</td>
                            <td>${0 || this.state.totalExpense}</td>
                        </tr>
                        <tr>
                            <th scope="row">Total Profit</th>
                            <th>${this.state.paidRevenue+this.state.sponsorRevenue-this.state.totalExpense}</th>
                        </tr>
                    </tbody>
                </table>
            </div>
        } else if (this.state.expenseReportContentFlag) {
            content = <div>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">Expense Title</th>
                            <th scope="col">Expense Desc.</th>
                            <th scope="col">Expense Amount</th>
                            <th scope="col">Expense Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        {expenseReportTable}
                    </tbody>
                </table>
            </div>
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

        buttons = <div>
            <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonOpen}>Open</Button>
            <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonClose}>Close</Button>
            <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonFinalize}>Finalize</Button><br />
        </div>


        console.log("Printing this.state.message : ", this.state.message)

        // if (this.state.message === "admin") {

        //     buttons = <div>
        //         <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonOpen}>Open</Button><Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonClose}>Close</Button>
        //         <Button className="mx-2" type="primary" size="large" style={{ marginTop: "20%" }} onClick={this.handleHackathonFinalize}>Finalize</Button><br />
        //     </div>
        // }


        let resultsButton = null
        var enddateconv = new Date(this.state.endDate)
        var end_sec = enddateconv.getTime()
        let currentDate = Date.now()
        if (currentDate > end_sec) {
            resultsButton = <div>
            <Button className="mx-2" type="primary" shape="round" size="large" style={{ marginTop: "5%" }} onClick={this.routeToResults}>Results</Button>
            <Button className="mx-2" type="primary" shape="round" size="large" style={{ marginTop: "5%" }} onClick={this.addExpenseModal} disabled={this.state.isFinalized}>Add Expense</Button>

            </div>
        } else {
            resultsButton = <div>
            <Button className="mx-2" type="primary" shape="round" size="large" style={{ marginTop: "5%" }} disabled>Results</Button>
            <Button className="mx-2" type="primary" shape="round" size="large" style={{ marginTop: "5%" }} onClick={this.addExpenseModal} disabled={this.state.isFinalized}>Add Expense</Button>
            </div>
        }
        let addExpense = <Modal
            title="Add a new Expense"
            visible={expenseModalVisible}
            onOk={this.handleCancel}
            // confirmLoading={confirmLoading}
            onCancel={this.handleCancel}
        >

            <Form
                layout="vertical"
                onSubmit={this.createExpense}
            >
                <Form.Item label="Expense title">
                    {getFieldDecorator('title', {
                        rules: [{ required: true, message: 'Please input the title of the Expense' }],
                    })(<Input />)}
                </Form.Item>
                <Form.Item label="Description">
                    {getFieldDecorator('description', {
                        rules: [{ required: true, message: 'Please input the title of the Expense' }],
                    })(<Input type="textarea" rows="3" cols="10" />)}
                </Form.Item>
                <Form.Item label="Amount">
                    {getFieldDecorator('amount', {
                        rules: [{ required: true, message: 'Please input the title of the Expense' }],
                    })(<Input />)}
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">Add Expense</Button>
                </Form.Item>
            </Form>
        </Modal>

        let inviteButton = <Button className="mx-2" type="primary" shape="round" size="large" onClick={this.showInviteModal}>Invite</Button>


        return (
            <div>
                {redirect}
                <NavBar></NavBar>
                {addExpense}
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
                                {inviteButton}
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
                                <Menu.Item key="5"
                                    onClick={this.loadPaymentReport}
                                >
                                    <Icon type="dollar" />
                                    <span className="nav-text">Payment Registration Report</span>
                                </Menu.Item>
                                <Menu.Item key="6"
                                    onClick={this.loadExpenseReport}
                                >
                                    <Icon type="dollar" />
                                    <span className="nav-text">Expense Report</span>
                                </Menu.Item>
                                <Menu.Item key="7"
                                    onClick={this.loadRevenueReport}
                                >
                                    <Icon type="dollar" />
                                    <span className="nav-text">Revenue Report</span>
                                </Menu.Item>
                                <br></br>
                            </Menu>
                        </Sider>
                        <br></br>
                        <Layout style={{ marginLeft: 10 }} >
                            <Content style={{ margin: '0px 16px 0', overflow: 'initial' }}>
                                <div style={{ padding: 14, background: '#fff', minHeight: "300px" }}>
                                    {content}
                                </div>
                            </Content>
                        </Layout>
                    </Layout>
                </div>
                <div><Modal
                    title="Invite a new user"
                    visible={this.state.visibleInviteModal}
                    onOk={this.handleInviteModalOk}
                    onCancel={this.handleInviteModalCancel}
                    destroyOnClose="true"
                >
                    <b>Enter email:</b>
                    <br />
                    <Form onSubmit={this.handleInviteSubmit}>
                        <Form.Item>
                            {getFieldDecorator('email', {
                                rules: [{ required: true, message: 'Please input an email!' }],
                            })(
                                <Input
                                    prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                    placeholder="Email"
                                />,
                            )}
                        </Form.Item>

                    </Form>
                </Modal></div>
            </div>
        )
    }
}

export default Form.create()(HackathonDetails);
