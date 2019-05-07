import React, { Component } from 'react'
import '../../App.css'
import { Link } from 'react-router-dom'
import NavBar from '../Navbar/Navbar';
import { Layout, Tooltip , Icon, Row, Col, Button, Input, Form } from 'antd';
import axios from 'axios'
import Title from 'antd/lib/typography/Title';
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
            submissionsData : [],

        }
    }

    componentDidMount() {

        console.log("\nInside component did mount")

        // (WRITE THE AXIOS CALL HERE TO FETCH ALL THE SUBMISSIONS FOR THE JUDGE)

        this.setState({
            submissionsData : [
                {
                    "id" : 1,
                    "submissionLink" : "https://github.com/alice22/OpenHack-using-Spring-Framework",
                    "team" : {
                        "id" : 1,
                        "name" : "Team Stark",
                        "members" : [
                            {
                                "id" : "10",
                                "name" : "Bob"
                            },
                            {
                                "id" : "11",
                                "name" : "Alice"
                            },
                            {
                                "id" : "12",
                                "name" : "Mark"
                            }
                        ]
                    },
                    "hackathon" : {
                        "id" : 1,
                        "name" : "Hackathon XYZ"
                    },
                },
                {
                    "id" : 2,
                    "submissionLink" : "https://github.com/alice22/Linkedin-simulation",
                    "team" : {
                        "id" : 2,
                        "name" : "Team Baratheon",
                        "members" : [
                            {
                                "id" : "13",
                                "name" : "Jessica"
                            },
                            {
                                "id" : "14",
                                "name" : "Jared"
                            }
                        ]
                    },
                    "hackathon" : {
                        "id" : 2,
                        "name" : "Hackathon ABC"
                    }
                },
                {
                    "id" : 3,
                    "submissionLink" : "https://github.com/alice22/WeServe-web-application",
                    "team" : {
                        "id" : 3,
                        "name" : "Team Tyrell",
                        "members" : [
                            {
                                "id" : "10",
                                "name" : "Bob"
                            },
                            {
                                "id" : "11",
                                "name" : "Alice"
                            },
                            {
                                "id" : "15",
                                "name" : "Olivia"
                            }
                        ]
                    },
                    "hackathon" : {
                        "id" : 3,
                        "name" : "Hackathon ABC"
                    }
                },
                {
                    "id" : 4,
                    "submissionLink" : "https://github.com/alice22/School-maanagement-app",
                    "team" : {
                        "id" : 4,
                        "name" : "Team Tully",
                        "members" : [
                            {
                                "id" : "16",
                                "name" : "Harry"
                            },
                            {
                                "id" : "12",
                                "name" : "Mark"
                            }
                        ]
                    },
                    "hackathon" : {
                        "id" : 1,
                        "name" : "Hackathon XYZ"
                    }
                },,
                {
                    "id" : 5,
                    "submissionLink" : "https://github.com/alice22/Game-2048-using-python",
                    "team" : {
                        "id" : 5,
                        "name" : "Team Lannister",
                        "members" : [
                            {
                                "id" : "16",
                                "name" : "Harry"
                            },
                            {
                                "id" : "15",
                                "name" : "Olivia"
                            }
                        ]
                    },
                    "hackathon" : {
                        "id" : 5,
                        "name" : "Hackathon XYZ"
                    }
                },
            ]
        })
    }

    gradeSubmit = (e,sub_id) => {
        console.log("Grade submission button pressed")
        

        e.preventDefault();
        this.props.form.validateFields((err, values) => {
          if (!err) {
            var submission_id = sub_id
            var grade = values.grade
            console.log("Submission id : "+submission_id)
            console.log("The grade for this submission id is : "+grade)

            // (WRITE THE AXIOS FUNCTION HERE TO STORE THE GRADE ACROSS THE SUBMISSION ID)

          }
        });
    }

    render(){
        const { getFieldDecorator } = this.props.form;
        let submissionDisplay = null
        let teamMembersDisplay = null
        if(this.state.submissionsData == null || this.state.submissionsData.length == 0)
        {
            submissionDisplay = (
                <div>
                    <div class="center">
                        <br/><br/>
                        <center>
                            <img src = "https://dumielauxepices.net/sites/default/files/mechanical-clipart-engineering-class-679271-5014506.gif" width="300" height="250"></img>
                            <p style = {{fontSize:"20px"}}>No submissions yet!!</p>
                        </center>
                    </div>
                </div>
            )
        }
        else
        {
            submissionDisplay = this.state.submissionsData.map(sub => {
                if(sub.team.members != null && sub.team.members.length > 0)
                {
                    teamMembersDisplay = sub.team.members.map(mem => {
                        return(
                            <div style = {{margin : "10px"}}>
                                <p style = {{marginRight : "10px"}}>
                                <Icon type="user"></Icon>
                                {mem.name}
                                </p>
                            </div>
                        )
                    })
                }
                return(
                    <div style={{ backgroundColor: "#EAECEE", marginTop:"20px", boxShadow:"5px 5px #FAFAFA", padding:"8px", marginLeft:"20px", marginRight:"200px", marginBottom:"20px" }}>
                        <br></br>
                        <Row type="flex">
                            <Col span={17} style={{borderRight : "1px solid gray", marginRight : "10px"}}>
                                <div style = {{marginLeft : "20px"}}>
                                    <h4>Submission {sub.id}</h4>
                                    <h6>Submission link : <u style = {{color : "blue"}}>{sub.submissionLink}</u></h6>
                                    <h6>Hackathon name : {sub.hackathon.name}</h6>
                                    <h6>Team name : {sub.team.name}</h6>
                                    <h6>Team members : </h6>
                                    {teamMembersDisplay}

                                </div>
                                <br></br>
                            </Col>
                            <Col span={5} style={{marginLeft : "20px"}}>
                            <Form
                                layout="vertical"
                                onSubmit={(e)=>this.gradeSubmit(e, sub.id)}
                            >
                                <Form.Item label="Grades">
                                    {getFieldDecorator('grade')(
                                        <Input
                                        placeholder="Enter grades here"
                                        prefix={<Icon type="edit" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                        suffix={
                                            <Tooltip title="The grades should be a float number between 0-10, inclusive.">
                                                <Icon type="info-circle" style={{ color: 'rgba(0,0,0,.45)' }} />
                                            </Tooltip>
                                        }
                                        allowClear = "true"
                                        size = "large"
                                        id = "grade"
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