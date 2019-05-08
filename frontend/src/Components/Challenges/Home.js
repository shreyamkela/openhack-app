import React, { Component } from 'react'
import '../../App.css'
import { Menu, Icon } from 'antd';
import { Row, Col, Pagination } from 'antd';
import {Link} from 'react-router-dom'   
import NavBar from '../Navbar/Navbar';
import { Badge } from 'antd';
import ChallengeCard from './ChallengeCard';



class Home extends Component {

    state = {
        ongoingHackathons:[],
        currentOngoingHackathons:[],
        ongoingHackathonsSize:0,
        upcomingHackathons:[],
        currentUpcomingHackathons:[],
        upcomingHackathonsSize:0,
        previousHackathons:[],
        currentPreviousHackathons:[],
        previousHackathonsSize:0,
        currentOngoingHackathonsPage:1,
        currentUpcomingHackathonsPage:1,
        currentPreviousHackathonsPage:1
    }

    componentDidMount(){
        this.setState({
            ongoingHackathons:[
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                }
            ],
            upcomingHackathons:[
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                }
            ],
            previousHackathons:[
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                },
                {
                    "title":"International Women's Hackathon",
                    "description":"This is the description of the Hackathon",
                    "start":"04/22/2019",
                    "end":"04/23/2019",
                    "fees":"$50",
                    "minMaxTeam":"2-4 Persons",
                    "SponsorDiscount":"$10"
                }
            ],
        },() => {
            this.setState({
                ongoingHackathonsSize:Math.ceil(this.state.ongoingHackathons.length),
                upcomingHackathonsSize:Math.ceil(this.state.upcomingHackathons.length),
                previousHackathonsSize:Math.ceil(this.state.previousHackathons.length),
                currentOngoingHackathons: this.state.ongoingHackathons.slice((this.state.currentOngoingHackathonsPage-1)*4,((this.state.currentOngoingHackathonsPage-1)*4)+4),
                currentUpcomingHackathons: this.state.upcomingHackathons.slice((this.state.currentUpcomingHackathonsPage-1)*4,((this.state.currentUpcomingHackathonsPage-1)*4)+4),
                currentPreviousHackathons: this.state.previousHackathons.slice((this.state.currentPreviousHackathonsPage-1)*4,((this.state.currentPreviousHackathonsPage-1)*4)+4)
            })
        })
    }
    
    onChangeOnGoing = (page) =>{
        console.log(page)
        this.setState({
            currentOngoingHackathonsPage:page,
            currentOngoingHackathons:this.state.ongoingHackathons.slice((page-1)*4,((page-1)*4)+4)
        })
    }

    onChangeUpComing = (page) =>{
        console.log(page)
        this.setState({
            currentUpcomingHackathonsPage:page,
            currentUpcomingHackathons:this.state.upcomingHackathons.slice((page-1)*4,((page-1)*4)+4)
        })
    }
    onChangePrevious = (page) =>{
        console.log(page)
        this.setState({
            currentPreviousHackathonsPage:page,
            currentPreviousHackathons:this.state.previousHackathons.slice((page-1)*4,((page-1)*4)+4)
        })
    }
        

    render() {

        var onGoingCards = this.state.currentOngoingHackathons && this.state.currentOngoingHackathons.map(card => {
            return(
                <ChallengeCard card={card}></ChallengeCard>
            )
        })

        var upComingCards = this.state.currentUpcomingHackathons && this.state.currentUpcomingHackathons.map(card => {
            return(
                <ChallengeCard card={card}></ChallengeCard>
            )
        })

        var previousCards = this.state.currentPreviousHackathons && this.state.currentPreviousHackathons.map(card => {
            return(
                <ChallengeCard card={card}></ChallengeCard>
            )
        })
        return (
            <div>
                <NavBar></NavBar>
                <div class="px-4 py-4">
                    <h3>Hackathons, Programming Challenges, and Coding Competitions</h3>
                </div>
                <div class="px-4 py-3" id="main">
                    <hr></hr>
                    <a href="#live" class="px-4">
                    <Badge count={this.state.ongoingHackathonsSize}>
                        <h5>Live Hackathons</h5>
                    </Badge>
                    </a>
                    <a href="#upcoming" class="px-4">
                    <Badge count={this.state.upcomingHackathonsSize}>
                        <h5>Upcoming Hackathons</h5>
                    </Badge>
                    </a>
                    <a href="#previous" class="px-4">
                    <Badge>
                        <h5>Previous Hackathons</h5>
                    </Badge>
                    </a>
                    <hr></hr>
                </div>
                <div class="px-4 py-3" id="live">
                    <h6 class="px-4">On-going  Hackathons</h6>
                    <Row type="flex">
                        {onGoingCards}
                    </Row>
                    <center><Pagination current={this.state.currentOngoingHackathonsPage} onChange={this.onChangeOnGoing} defaultPageSize={4} total={this.state.ongoingHackathonsSize} /></center>
                </div>
                <div class="px-4 py-3" id="upcoming">
                    <h6 class="px-4">Upcoming Hackathons</h6>
                    <Row type="flex">
                        {upComingCards}
                    </Row>
                    <center><Pagination current={this.state.currentUpcomingHackathonsPage} onChange={this.onChangeUpComing} defaultPageSize={4} total={this.state.upcomingHackathonsSize} /></center>
                </div>
                <div class="px-4 py-3" id="previous">
                    <h6 class="px-4">Previous Hackathons</h6>
                    <Row type="flex">
                        {previousCards}
                    </Row>
                    <center><Pagination current={this.state.currentPreviousHackathonsPage} onChange={this.onChangePrevious} defaultPageSize={4} total={this.state.previousHackathonsSize} /></center>
                </div>
            </div>
        )
    }
}

export default Home;