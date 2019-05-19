import React, { Component } from 'react'
import '../../App.css'
import { Link } from 'react-router-dom'
import NavBar from '../Navbar/Navbar';
import { Layout, Tooltip, Icon, Row, Col, Button, Input, Form, Skeleton } from 'antd';
import axios from 'axios'
import Title from 'antd/lib/typography/Title';
import swal from 'sweetalert';
import { Redirect } from 'react-router'
const { Content, Sider } = Layout;

// 4 states:
// nothing : no action taken yet
// member :  the hacker is the member of the organization,
// owner : the hacker is the owner of the organization, so he/she can approve/decline request to join organization
// requested :  the hacker has sent a request to join an organization, waiting for approval.

class ViewSubmission extends Component {
    constructor(props) {
        super(props);
        this.state = {
            hackathonId: this.props.match.params.hackathonId,
            submissionsData: [],
        }
    }

    componentDidMount() {

        console.log("\nInside component did mount")

        // (WRITE THE AXIOS CALL HERE TO FETCH ALL THE SUBMISSIONS FOR THE JUDGE)

        axios.defaults.withCredentials = true;
        axios.get(`http://localhost:8080/submission/${this.state.hackathonId}`)
            .then(response => {
                if (response.status === 200) {
                    this.setState({
                        submissionsData: response.data.submissionDetails
                    })
                }
            })
            .catch(err => {
                console.log("err is:", err)
            })
    }

    gradeSubmit = (e, sub_id) => {
        console.log("Grade submission button pressed", e, sub_id)
        e.preventDefault();
        this.props.form.validateFields((err, values) => {


            if (!err) {
                var submission_id = sub_id
                var grade = values[submission_id]
                //console.log(typeof grade)
                let body = {
                    "grade": grade
                }
                axios.defaults.withCredentials = true
                axios.post(`http://localhost:8080/gradeSubmission/${submission_id}`, body)
                    .then(response => {
                        if (response.status === 200) {
                            swal("Successfully graded!", "success")
                                .then(() => {
                                    window.location.reload()
                                })

                        }
                    })

                // (WRITE THE AXIOS FUNCTION HERE TO STORE THE GRADE ACROSS THE SUBMISSION ID)

            }
        });
    }

    render() {
        const { getFieldDecorator } = this.props.form;
        let submissionDisplay = null
        let teamMembersDisplay = null

        let redirect = null
        if (!localStorage.getItem("userId")) {
            redirect = <Redirect to="/login" />
        }
        if (this.state.submissionsData == null || this.state.submissionsData.length == 0) {
            submissionDisplay = (
                <div>
                    <div class="center">
                        <br /><br />
                        <center>
                            <img src="https://dumielauxepices.net/sites/default/files/mechanical-clipart-engineering-class-679271-5014506.gif" width="300" height="250"></img>
                            <p style={{ fontSize: "20px" }}>No submissions yet!!</p>
                        </center>
                    </div>
                </div>
            )
        }
        else {
            submissionDisplay = this.state.submissionsData.map(sub => {
                if (sub.memberDetails != null && sub.memberDetails.length > 0) {
                    teamMembersDisplay = sub.memberDetails.map(mem => {
                        return (
                            <div style={{ margin: "10px" }}>
                                <p style={{ marginRight: "10px" }}>
                                    <Icon type="user"></Icon>
                                    {mem.memberName}
                                </p>
                            </div>
                        )
                    })
                }
                return (
                    <div style={{ backgroundColor: "#EAECEE", marginTop: "20px", boxShadow: "5px 5px #FAFAFA", padding: "8px", marginLeft: "20px", marginRight: "200px", marginBottom: "20px" }}>
                        <br></br>
                        <Row type="flex">
                            <Col span={17} style={{ borderRight: "1px solid gray", marginRight: "10px" }}>
                                <div style={{ marginLeft: "20px" }}>
                                    <h4>Submission {sub.submissionId}</h4>
                                    <h6>Submission link : <u style={{ color: "blue" }}>{sub.submissionUrl}</u></h6>
                                    <h6>Hackathon name : {sub.hackathonName}</h6>
                                    <h6>Team name : {sub.teamName}</h6>
                                    <h6>Team members : </h6>
                                    {teamMembersDisplay}

                                </div>
                                <br></br>
                            </Col>
                            <Col span={5} style={{ marginLeft: "20px" }}>
                                <Form
                                    layout="vertical"
                                    onSubmit={(e) => this.gradeSubmit(e, sub.submissionId)}
                                >
                                    <Form.Item label="Grades">
                                        {getFieldDecorator(`${sub.submissionId}`)(
                                            <Input
                                                placeholder={sub.grade || "Enter grades here"}
                                                prefix={<Icon type="edit" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                                suffix={
                                                    <Tooltip title="The grades should be a float number between 0-10, inclusive.">
                                                        <Icon type="info-circle" style={{ color: 'rgba(0,0,0,.45)' }} />
                                                    </Tooltip>
                                                }
                                                allowClear="true"
                                                size="large"
                                                id={sub.submissionId}
                                            />
                                        )}
                                    </Form.Item>
                                    <Form.Item>
                                        <Button type="primary" htmlType="submit" >Submit</Button>
                                    </Form.Item>
                                </Form>
                            </Col>
                        </Row>
                    </div>
                )
            })
        }
        return (
            <div>
                <NavBar></NavBar>
                <div class="px-4 py-4">
                    <h2>Submissions</h2>
                    <hr></hr>
                    {submissionDisplay}

                </div>
            </div>
        )
    }

}

export default Form.create()(ViewSubmission);