import React, { Component } from 'react';
import Title from 'antd/lib/typography/Title';
// import { Layout, Menu, Icon, Row, Col, Button, Modal, Divider, Avatar, Form, Input, message, Skeleton } from 'antd';
import { Divider, Button, Modal, Avatar, Col } from 'antd';
import "../../css/LoginMain.css";
import axios from 'axios'

class RegistrationPaymentReport extends Component {
    constructor(props) {
        super(props);
        this.state = {
            teamDetails: '',
            visibleTeamModal: false,
            userTeam: [],

        }
    }
    componentDidMount() {
        console.log("Data from hackathon ", this.props.location.state.details)
        this.setState({
            teamDetails: this.props.location.state.details,
        })

    }
    showTeamModal = async (teamId) => {
        axios.defaults.withCredentials = true
        var response = await axios.get(`http://localhost:8080/payment/team/${teamId}`)
        if (response.status === 200) {
            console.log("Payment/Team/Data", response.data)
            // console.log("Payment/Team/Data Member ID",response.data.memberId)
            // var userId= response.data.memberId
            // var member_response = await axios.get(`http://localhost:8080/user/${userId}`)
            //if (member_response === 200) {
                this.setState({
                    visibleTeamModal: true,
                    userTeam: response.data,
                    // userName:member_response.data.name,
                    // userTitle:member_response.data.title
                })
            //}
        }
    }
    handleCancel = () => {
        this.setState({
            visibleTeamModal: false,
        })
    }
    render() {
        var teamModalContent = null
        if (this.state.userTeam) {
            console.log(this.state.userTeam)
            teamModalContent = this.state.userTeam.map(member => {
                return (
                    <Col span={6}>
                        <Avatar shape="square" icon="user" />
                        <p><b>{member.memberId}</b></p>
                        <p>{member.status}</p>

                    </Col>
                )
            })
        }

        var content = null
        content = this.state.teamDetails && this.state.teamDetails.map(team => {
            return (
                <div style={{ backgroundColor: "#EAECEE", width: "75%", marginTop: "10px", boxShadow: "5px 5px #FAFAFA", padding: "8px", marginLeft: "20px" }}>
                    <a href="#" id={team.teamId}>
                        <Title level={4} style={{ marginLeft: "20px" }}>Team name : {team && team.teamName}</Title>
                    </a>
                    <p style={{ marginLeft: "20px", fontSize: "15px" }}>Members: {team && team.teamSize}</p>
                    <Button type="primary" size="large" style={{ marginLeft:"80%" }} onClick={this.showTeamModal(team.teamId)}>View Team</Button><br />
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
                    <Divider></Divider>
                </div>
            )
        })
        return (
            <div className="payment-report">
                <h2>Registration Payment Report</h2>
                {content}
            </div>
        );
    }
}

export default RegistrationPaymentReport;