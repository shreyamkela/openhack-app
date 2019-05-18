import React, { Component } from 'react'
import '../../App.css'
import { Menu, Icon, Row, Col, Badge, Card, Button, Carousel } from 'antd';
import { Link } from 'react-router-dom'
import NavBar from '../Navbar/Navbar';





class ChallengeCard extends Component {

    render() {
        console.log(this.props)
        // var cards = this.props.cards && this.props.cards.map(details => {
        //     return (
        // <Col span={7}>
        //     <a href="#">
        //         <Card
        //             cover={<img alt="example" src="https://cdn-images-1.medium.com/max/1600/1*supQ92uykNElEfyYf7UgHw.png" />}
        //             title={details.title}
        //             bordered={true}
        //             style={{ width: 300 }}>
        //             <center>{details.description}</center>
        //             <hr></hr>
        //             <b>Start:</b> {details.start} <t /> <b>End:</b> {details.end}<br />
        //             <b>Fees:</b> {details.fees}<br />
        //             <b>Min-Max Team:</b> {details.minMaxTeam}<br />
        //             <b>SponsorDiscount:</b> {details.SponsorDiscount}<br />
        //             <center class="py-2">
        //                 <Button type="primary">Open</Button>
        //             </center>
        //         </Card>
        //     </a>
        // </Col>
        //     )
        // })
        var loginBasedCards = null;
        if (localStorage.getItem("userId") && localStorage.getItem("userType") === "user") {
            loginBasedCards = <Link to={`/hackathon_details/${this.props.card.id}`}>
                <Card
                    cover={<img alt="example" src="https://cdn-images-1.medium.com/max/1600/1*supQ92uykNElEfyYf7UgHw.png" />}
                    title={this.props.card && this.props.card.name}
                    bordered={true}
                    style={{ width: 270, height: 470 }}>
                    <center>{this.props.card && this.props.card.description}</center>
                    <hr></hr>
                    <b>Start:</b> {this.props.card && new Date(this.props.card.startDate).toDateString()}<br />
                    <b>End:</b> {this.props.card && new Date(this.props.card.endDate).toDateString()}<br />
                    <b>Fees:</b> ${this.props.card && this.props.card.fee}<br />
                    <b>Min-Max Team:</b> {this.props.card && this.props.card.teamSizeMin} - {this.props.card && this.props.card.teamSizeMax}<br />
                    <b>SponsorDiscount:</b> {this.props.card && this.props.card.discount}%<br />
                    <center class="py-2">
                        <Button type="primary">View</Button>
                    </center>
                </Card>
            </Link>
        }
        else {
            loginBasedCards = <Link to={`/admin/hackathon_details/${this.props.card.id}`}>
                <Card
                    cover={<img alt="example" src="https://cdn-images-1.medium.com/max/1600/1*supQ92uykNElEfyYf7UgHw.png" />}
                    title={this.props.card && this.props.card.title}
                    bordered={true}
                    style={{ width: 300, height: 450 }}>
                    <center>{this.props.card && this.props.card.description}</center>
                    <hr></hr>
                    <b>Start:</b> {this.props.card && new Date(this.props.card.startDate).toDateString()}<br />
                    <b>End:</b> {this.props.card && new Date(this.props.card.endDate).toDateString()}<br />
                    <b>Fees:</b> ${this.props.card && this.props.card.fee}<br />
                    <b>Min-Max Team:</b> {this.props.card && this.props.card.teamSizeMin} - {this.props.card && this.props.card.teamSizeMax}<br />
                    <b>SponsorDiscount:</b> {this.props.card && this.props.card.discount}%<br />
                    <center class="py-2">
                        <Button type="primary">View</Button>
                    </center>
                </Card>
            </Link>
        }
        return (
            <div class="px-3 py-3">
                <Col span={6}>
                    {loginBasedCards}
                </Col>
            </div>
        )
    }
}

export default ChallengeCard;