import React, { Component } from 'react'
import '../../App.css'
import { Link } from 'react-router-dom'
import NavBar from '../Navbar/Navbar';
import { Layout, Skeleton, Icon, Row, Col, Button, Tag } from 'antd';
import axios from 'axios'
import Title from 'antd/lib/typography/Title';
import {Redirect} from 'react-router'
const { Content, Sider } = Layout;

// 4 states:
// nothing : no action taken yet
// member :  the hacker is the member of the organization,
// owner : the hacker is the owner of the organization, so he/she can approve/decline request to join organization
// requested :  the hacker has sent a request to join an organization, waiting for approval.

class OrganizationDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            organizationData : {},
            cover_image : "https://media-fastly.hackerearth.com/media/companies/763e5c8-DeepSense_background_image_rotated_1516x460_bw.jpg",
            user_id : this.props.match.params.user_id,
            org_id : this.props.match.params.org_id,
            user_status : "",
            member_of_org : false,
            owner_of_org : false,
            info_received : false
        }
    }

    componentDidMount() {

        console.log("\nInside component did mount")

        axios.defaults.withCredentials = true;
        axios.get(`http://localhost:8080/hacker/getOneOrganization/${this.state.user_id}/${this.state.org_id}`)
                    .then((response) => {
                        console.log("Status Code : ", response.status);
                        
                        if (response.status === 200) {
                            console.log("Response received from backend");
                            console.log("\n"+JSON.stringify(response.data));
                            this.setState({
                                organizationData : response.data, 
                                info_received : true
                            })
                        }
                        else{
                            console.log("There was some error fetching list of organization from the backend")
                        }
                    })
                    .catch(err => {
                        console.log(err)
                    });
    }

    approveRequest = (userId, orgId, requestId) => {
        console.log("\nApprove button pressed !")
        console.log("Printing userId : ", userId);
        console.log("Printing organizationId : ", orgId);
        console.log("Printing requestId : ", requestId);

        axios.defaults.withCredentials = true;
        axios.put(`http://localhost:8080/hacker/joinOrganization/${userId}/${orgId}/${requestId}`)
                    .then((response) => {
                        console.log("Status Code : ", response.status);
                        
                        if (response.status === 200) {
                            console.log("\nResponse received from backend after decliing the join request");
                            console.log("\n"+JSON.stringify(response.data));
                            window.location.reload()
                        }
                        else{
                            console.log("\nThere was some error fetching organization info from the backend after request approval")
                        }
                    })
                    .catch(err => {
                        console.log(err)
                    });

    }

    declineRequest = (userId, orgId, requestId) => {
        console.log("\nDecline button pressed !")
        console.log("Printing userId : ", userId);
        console.log("Printing organizationId : ", orgId);
        console.log("Printing requestId : ", requestId);

        axios.defaults.withCredentials = true;
        axios.put(`http://localhost:8080/hacker/declineJoinOrganization/${userId}/${orgId}/${requestId}`)
                    .then((response) => {
                        console.log("Status Code : ", response.status);
                        
                        if (response.status === 200) {
                            console.log("\nResponse received from backend after decliing the join request");
                            console.log("\n"+JSON.stringify(response.data));
                            window.location.reload()
                        }
                        else{
                            console.log("\nThere was some error fetching organization info from the backend after request decline")
                        }
                    })
                    .catch(err => {
                        console.log(err)
                    });
    }

    render(){
        let skl = null
        if(!this.state.info_received)
        {
            skl = (
            <Skeleton active />
            )
        }
        console.log("\nThe set user id is : "+this.state.user_id)
        let membersDisplay = null;
        let sponsoredHackthonsDisplay = null;
        let noDataDisplay = null;
        let orgDataDisplay = null;
        let pendingRequests = null;
        let ownerName = null;
        let buttons = null;
        let address = null;
        let titleDisplay = null;
        let requestTitleDisplay = null;
        let pendingrequestTitle = null;
        let redirect = null
        if(!localStorage.getItem("userId")){
            redirect = <Redirect to="/login"/>
        }
        if(this.state.organizationData==null || this.state.organizationData.length==0)
        {
            orgDataDisplay = (
                <div class="center">
                <br/><br/>
                <center>
                    <p style = {{fontSize:"20px"}}>No data of this organization obtained</p>
                </center>
                </div>
            )
        }
        else
        {
            if(this.state.organizationData.owner)
            {
                ownerName = (
                    <div>
                        <p style={{ color: "#46535e", fontSize: "20px" }}> Owner of the organization : {this.state.organizationData.owner.name} </p>
                    </div>
                )
            }
            if(this.state.organizationData.Address)
            {
                address = (
                    <div>
                        <p style={{ color: "#46535e", fontSize: "20px" }}> Address : {this.state.organizationData.Address.street} {this.state.organizationData.Address.city} {this.state.organizationData.Address.state} {this.state.organizationData.Address.zip} </p>
                    </div>
                )
            }
            if(this.state.organizationData.members && this.state.organizationData.members.length!=0)
            {
                membersDisplay = this.state.organizationData.members.map(member => {
                    if(member.title)
                    {
                        titleDisplay = (
                            <p style = {{marginLeft:"10px"}}>Title : {member.title}</p>
                        )
                    }
                    return(
                        <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop:"10px", boxShadow:"5px 5px #FAFAFA", padding:"8px" }}>
                            <Row type="flex">
                            <Col span={2}>
                                <Icon type="user" style = {{fontSize : "30px"}} />
                            </Col>
                            <Col span={15}>
                            <div>
                                <p style = {{marginLeft:"10px"}}>Name : {member.name}</p>
                                <p style = {{marginLeft:"10px"}}>Email id : {member.email}</p>
                                {titleDisplay}
                                {/* <p style = {{marginLeft:"10px"}}>Title : {member.title}</p> */}
                            </div>
                            </Col>
                           </Row> 
                        </div>
                    )
                })  
            }
            else if(!this.state.info_received)
            {
                membersDisplay = (
                    <Skeleton active />
                )
            }
            else
            {
                membersDisplay = (
                    <div class="center">
                    <center>
                        <p style = {{fontSize:"15px", color: "#46535e"}}>There are no members of this organization!!</p>
                    </center>
                    </div>
                )
            }
            
            if(this.state.organizationData.sponsoredHackathons && this.state.organizationData.sponsoredHackathons.length!=0)
            {
                sponsoredHackthonsDisplay = this.state.organizationData.sponsoredHackathons.map(hackathon => {
                    return(
                        <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop:"10px", boxShadow:"5px 5px #FAFAFA", padding:"8px" }}>
                            <Row type="flex">
                            <Col span={2}>
                                <Icon type="home" style = {{fontSize : "30px"}} />
                            </Col>
                            <Col span={20}>
                            <div>
                                <p style = {{marginLeft:"10px"}}>Name : {hackathon.name}</p>
                                <p style = {{marginLeft:"10px"}}>Description : {hackathon.description}</p>
                                <p style = {{marginLeft:"10px"}}>Start Date : {hackathon.startDate.slice(0, 10)}</p>
                                <p style = {{marginLeft:"10px"}}>End Date : {hackathon.endDate.slice(0, 10)}</p>
                            </div>
                            </Col>
                           </Row> 
                        </div>
                    )
                })
            }
            else if(!this.state.info_received)
            {
                sponsoredHackthonsDisplay = (
                    <Skeleton active />
                )
            }
            else
            {
                sponsoredHackthonsDisplay = (
                    <div class="center">
                    <center>
                        <p style = {{fontSize:"15px", color: "#46535e"}}>There are no sponsored hackathons yet!!</p>
                    </center>
                    </div>
                )
            }

            if(this.state.organizationData.user_org_status=="requested")
            {
                buttons = (
                    <div style = {{marginTop: "20px"}}>
                        <Tag>
                            <p style = {{fontSize:"20px", marginTop:"10px"}}> Request sent </p>  
                        </Tag>
                    </div>
                )
            }
            else if(this.state.organizationData.user_org_status=="member")
            {
                buttons = (
                    <div style = {{marginTop: "20px"}}>
                        <Tag color="green" >
                            <p style = {{fontSize:"20px", marginTop:"10px"}}> Member <Icon type="check-circle" theme="twoTone" twoToneColor="#52c41a" style={{fontSize:"20px"}} /></p>  
                        </Tag>
                    </div>
                )
            }
            else if(this.state.organizationData.user_org_status=="owner")
            {
                pendingrequestTitle = (
                    <p style={{ color: "#46535e", fontSize: "20px" }}> Pending requests for joining the organization :   </p>
                )
                console.log("This user is the owner of the organization")
                if(this.state.organizationData.org_join_requests && this.state.organizationData.org_join_requests.length>0)
                {
                    pendingRequests = this.state.organizationData.org_join_requests.map(request => {
                    if(request.user.title)
                    {
                        requestTitleDisplay = (
                            <p style = {{marginLeft:"10px"}}>Title : {request.user.title}</p>
                        )
                    }
                    return(
                        <div>
                            
                            <br></br>
                            <div style={{ backgroundColor: "#EAECEE", width: "50%", marginTop:"10px", boxShadow:"5px 5px #FAFAFA", padding:"8px" }}>
                                <Row type="flex">
                                    <Col span={2}>
                                        <Icon type="user" style = {{fontSize : "30px"}} />
                                    </Col>
                                    <Col span={15}>
                                    <div>
                                        <p style = {{marginLeft:"10px"}}>Name : {request.user.name}</p>
                                        <p style = {{marginLeft:"10px"}}>Email id : {request.user.email}</p>
                                        {requestTitleDisplay}
                                    </div>
                                    </Col>
                            </Row> 
                            <br></br>
                            <Row type="flex">
                                <Col>
                                    <Button type="primary" size="large" style={{ marginLeft: "20px" }} onClick = {() => {this.approveRequest(request.user.id, this.state.org_id, request.request_id)}} > Approve </Button><br />
                                </Col>
                                <Col>
                                    <Button type="primary" size="large" style={{ marginLeft: "20px" }} onClick = {() => {this.declineRequest(request.user.id, this.state.org_id, request.request_id)}} > Decline </Button><br />
                                </Col> 
                                </Row>
                                <br></br>
                            </div>
                        </div>
                    )
                    })
                }
                else 
                {
                    pendingRequests = (
                        <div class="center">
                            
                            <br></br>
                            <center>
                                <img src = "https://dumielauxepices.net/sites/default/files/mechanical-clipart-engineering-class-679271-5014506.gif" width="300" height="250"></img>
                                <p style = {{fontSize:"15px", color: "#46535e"}}>There are no pending requests !!</p>
                            </center>
                        </div>
                    )
                }   
            }
            
            orgDataDisplay = (
                <div style={{ marginLeft: "10%" }}>
                        <Row type="flex">
                            <Col span={16}>
                                <h2 style={{ color: "#46535e", paddingTop: "4%" }}>  {this.state.organizationData.name} </h2>
                                <p style={{ color: "#46535e", fontSize: "20px" }}> {this.state.organizationData.description} </p>
                                {address}
                                {ownerName}
                                <hr/>
                                <p style={{ color: "#46535e", fontSize: "20px" }}> Members of the organization :  </p>
                                {membersDisplay}
                                <hr/>
                                <p style={{ color: "#46535e", fontSize: "20px" }}> Sponsored hackathons :   </p>
                                {sponsoredHackthonsDisplay}
                                <hr/>
                                {pendingrequestTitle}
                                {pendingRequests}
                                <br></br>
                            </Col>
                            <Col span={6}>
                                {buttons}
                            </Col>
                        </Row>
                    </div>
            )
            
        }
        return (
            <div>
                {redirect}
                <NavBar></NavBar>
                <div style={{ backgroundImage: "url(" + this.state.cover_image + ")", height: "300px", position: "relative" }}>
                </div>
                {skl}
                {orgDataDisplay}
            </div>
        )
    }
}

export default OrganizationDetails