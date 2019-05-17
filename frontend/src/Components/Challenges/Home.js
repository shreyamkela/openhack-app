import React, { Component } from 'react'
import '../../App.css'
import { Menu, Icon, Skeleton } from 'antd';
import { Row, Col, Pagination } from 'antd';
import { Link } from 'react-router-dom'
import { Redirect } from 'react-router'
import NavBar from '../Navbar/Navbar';
import { Badge } from 'antd';
import ChallengeCard from './ChallengeCard';
import axios from 'axios'


class Home extends Component {

    constructor(props) {
        super(props)
        this.state = {
            ongoingHackathons: [],
            currentOngoingHackathons: [],
            ongoingHackathonsSize: 0,
            upcomingHackathons: [],
            currentUpcomingHackathons: [],
            upcomingHackathonsSize: 0,
            previousHackathons: [],
            currentPreviousHackathons: [],
            previousHackathonsSize: 0,
            judgedHackathons: [],
            currentJudgedHackathons: [],
            judgedHackathonsSize: 0,
            currentOngoingHackathonsPage: 1,
            currentUpcomingHackathonsPage: 1,
            currentPreviousHackathonsPage: 1,
            currentJudgedHackathonsPage: 1
        }
    }

    componentDidMount() {
        axios.defaults.withCredentials = true
        let userId = localStorage.getItem("userId")
        axios.get(`http://localhost:8080/hackathon/getAllHackathons/${userId}`)
            .then(response => {
                if (response.status === 200) {
                    console.log(response.data)
                    let ongoingHackathons = []
                    let upcomingHackathons = []
                    let previousHackathons = []
                    let judgedHackathons = []
                    response.data.hackathonDetails.map(hackathon => {
                        if (new Date(hackathon.startDate) < new Date() && new Date(hackathon.endDate) > new Date()) {
                            ongoingHackathons.push(hackathon)
                        } else if (new Date(hackathon.startDate) > new Date()) {
                            upcomingHackathons.push(hackathon)
                        } else if (new Date(hackathon.endDate) < new Date()) {
                            previousHackathons.push(hackathon)
                        }

                        if (hackathon.message === "judge") {
                            console.log("judged Event")
                            judgedHackathons.push(hackathon)
                        }
                    })

                    this.setState({
                        ongoingHackathons: ongoingHackathons,
                        upcomingHackathons: upcomingHackathons,
                        previousHackathons: previousHackathons,
                        judgedHackathons: judgedHackathons
                    }, () => {
                        this.setState({
                            ongoingHackathonsSize: Math.ceil(this.state.ongoingHackathons.length),
                            upcomingHackathonsSize: Math.ceil(this.state.upcomingHackathons.length),
                            previousHackathonsSize: Math.ceil(this.state.previousHackathons.length),
                            judgedHackathonsSize: Math.ceil(this.state.judgedHackathons.length),
                            currentOngoingHackathons: this.state.ongoingHackathons.slice((this.state.currentOngoingHackathonsPage - 1) * 4, ((this.state.currentOngoingHackathonsPage - 1) * 4) + 4),
                            currentUpcomingHackathons: this.state.upcomingHackathons.slice((this.state.currentUpcomingHackathonsPage - 1) * 4, ((this.state.currentUpcomingHackathonsPage - 1) * 4) + 4),
                            currentPreviousHackathons: this.state.previousHackathons.slice((this.state.currentPreviousHackathonsPage - 1) * 4, ((this.state.currentPreviousHackathonsPage - 1) * 4) + 4),
                            currentJudgedHackathons: this.state.judgedHackathons.slice((this.state.currentJudgedHackathonsPage - 1) * 4, ((this.state.currentJudgedHackathonsPage - 1) * 4) + 4)
                        })
                    })
                }
            })
        console.log(this.state)
    }

    onChangeOnGoing = (page) => {
        console.log(page)
        this.setState({
            currentOngoingHackathonsPage: page,
            currentOngoingHackathons: this.state.ongoingHackathons.slice((page - 1) * 4, ((page - 1) * 4) + 4)
        })
    }

    onChangeUpComing = (page) => {
        console.log(page)
        this.setState({
            currentUpcomingHackathonsPage: page,
            currentUpcomingHackathons: this.state.upcomingHackathons.slice((page - 1) * 4, ((page - 1) * 4) + 4)
        })
    }
    onChangePrevious = (page) => {
        console.log(page)
        this.setState({
            currentPreviousHackathonsPage: page,
            currentPreviousHackathons: this.state.previousHackathons.slice((page - 1) * 4, ((page - 1) * 4) + 4)
        })
    }

    onChangeJudged = (page) => {
        console.log(page)
        this.setState({
            currentJudgedHackathonsPage: page,
            currentJudgedHackathons: this.state.judgedHackathons.slice((page - 1) * 4, ((page - 1) * 4) + 4)
        })
    }


    render() {

        var redirect = null
        var loader = null

        // if(!localStorage.getItem("userId")){
        //     redirect = <Redirect to="/login"></Redirect>
        // }
        if((!this.state.currentOngoingHackathons || this.state.currentOngoingHackathons.length<1) && (!this.state.currentJudgedHackathons || this.state.currentJudgedHackathons.length<1) && (!this.state.currentPreviousHackathons || this.state.currentPreviousHackathons.length<1) && (!this.state.currentUpcomingHackathons || this.state.currentUpcomingHackathons.length<1)){
            loader = <Skeleton active/>
        }else{
            loader=null
        }
        var onGoingCards = this.state.currentOngoingHackathons && this.state.currentOngoingHackathons.map(card => {
            return (
                <ChallengeCard card={card}></ChallengeCard>
            )
        })

        var upComingCards = this.state.currentUpcomingHackathons && this.state.currentUpcomingHackathons.map(card => {
            return (
                <ChallengeCard card={card}></ChallengeCard>
            )
        })

        var previousCards = this.state.currentPreviousHackathons && this.state.currentPreviousHackathons.map(card => {
            return (
                <ChallengeCard card={card}></ChallengeCard>
            )
        })

        var judgedCards = this.state.currentJudgedHackathons && this.state.currentJudgedHackathons.map(card => {
            return (

                <ChallengeCard card={card}></ChallengeCard>
            )
        })
        return (
            <div>
                {redirect}
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
                    <a href="#judged" class="px-4">
                        <Badge>
                            <h5>Judged Hackathons</h5>
                        </Badge>
                    </a>
                    <hr></hr>
                </div>
                <div class="px-4 py-3" id="live">
                    <h6 class="px-4">On-going  Hackathons</h6>
                    {loader}
                    <Row type="flex">
                        {onGoingCards}
                    </Row>
                    <center><Pagination current={this.state.currentOngoingHackathonsPage} onChange={this.onChangeOnGoing} defaultPageSize={4} total={this.state.ongoingHackathonsSize} /></center>
                </div>
                <div class="px-4 py-3" id="upcoming">
                    <h6 class="px-4">Upcoming Hackathons</h6>
                    {loader}
                    <Row type="flex">
                        {upComingCards}
                    </Row>
                    <center><Pagination current={this.state.currentUpcomingHackathonsPage} onChange={this.onChangeUpComing} defaultPageSize={4} total={this.state.upcomingHackathonsSize} /></center>
                </div>
                <div class="px-4 py-3" id="previous">
                    <h6 class="px-4">Previous Hackathons</h6>
                    {loader}
                    <Row type="flex">
                        {previousCards}
                    </Row>
                    <center><Pagination current={this.state.currentPreviousHackathonsPage} onChange={this.onChangePrevious} defaultPageSize={4} total={this.state.previousHackathonsSize} /></center>
                </div>
                <div class="px-4 py-3" id="judged">
                    <h6 class="px-4">Judged Hackathons</h6>
                    {loader}
                    <Row type="flex">
                        {judgedCards}
                    </Row>
                    <center><Pagination current={this.state.currentJudgedHackathonsPage} onChange={this.onChangeJudged} defaultPageSize={4} total={this.state.judgedHackathonsSize} /></center>
                </div>
            </div>
        )
    }
}

export default Home;