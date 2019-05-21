import React, { Component } from 'react'

import { Button, message, Skeleton, Card, Collapse } from 'antd';

import API from '../../utils/API'

const Panel = Collapse.Panel;

class HackathonResults extends Component {
    state = {
        hackathonResults: null
    }
    componentDidMount = async () => {
        console.log("Fetching hackathon results! Hackathon Id: ", this.props.match.params.id)
        let body = {
            "hackathonId": this.props.match.params.id,
            "userId": localStorage.getItem("userId")
        }
        try {
            let response = await API.post(`hackathon/results`, body);
            console.log("Response: ", response.data);
            this.setState({ hackathonResults: response.data.hackathonResults })
        } catch (error) {
            console.log(error.response);
            message.error("Unable to fetch hackathon results at the moment. Please refresh the page and try again.")
        }
    }

    handleGoToHome = () => {
        this.props.history.push("/")
    }

    render() {

        let resultsContent = null
        if (this.state.hackathonResults === null) {
            resultsContent = <Skeleton active />

        } else if (this.state.hackathonResults[0] !== undefined) {
            let hackathonResults = this.state.hackathonResults
            hackathonResults.sort((a, b) => {
                return b["grade"] - a["grade"];
            });

            let teamsCount = 0;
            let topPerformersCollapse = hackathonResults.map(key => {
                teamsCount++;
                if (teamsCount < 4) {
                    let rank = `Rank ${teamsCount} - Team "${key.teamName}"`
                    let teamMembers = "";
                    for (let teamMember in key.teamMembers) {
                        teamMembers += key.teamMembers[teamMember] + " "
                    }
                    let grade = "No grade assigned yet."
                    if (key.grade !== null) {
                        grade = key.grade
                    }
                    let participants = `Participants: ${teamMembers}, \n Grade: ${grade}`
                    return (<Panel header={rank} key={teamsCount}>
                        <p>{participants}</p>
                    </Panel>);
                }
            })
            let topPerformers = <Card title="Top Performers" style={{ width: 750 }}><Collapse accordion>{topPerformersCollapse}</Collapse></Card>


            let otherTeams = null;
            if (hackathonResults.length > 3) {
                let otherTeamsCount = 0;
                let otherTeamsCollapse = hackathonResults.map(key => {
                    otherTeamsCount++;
                    console.log("XXXXXXXX", otherTeamsCount)
                    if (otherTeamsCount > 3) {
                        let rank = `Rank ${otherTeamsCount} - Team "${key.teamName}"`
                        let teamMembers = "";
                        for (let teamMember in key.teamMembers) {
                            teamMembers += key.teamMembers[teamMember] + " "
                        }
                        let grade = "No grade assigned yet."
                        if (key.grade !== null) {
                            grade = key.grade
                        }
                        let participants = `Participants: ${teamMembers}, \n Grade: ${grade}`
                        return (<Panel header={rank} key={otherTeamsCount}>
                            <p>{participants}</p>
                        </Panel>);
                    }
                })
                otherTeams = <Card title="Other teams" style={{ width: 750 }}><Collapse accordion>{otherTeamsCollapse}</Collapse></Card>
            }

            resultsContent = (<div><div>{topPerformers}</div>
                <div>{otherTeams}</div></div>)


        }
        else {
            resultsContent = <b>No grades assigned yet.</b>
        }



        return (
            <div style={{ marginLeft: 280, marginTop: 10 }}>
                <div style={{ textAlign: "center" }}>
                    <Card title="Hackathon Results" style={{ background: '#ECECEC', width: 800 }}>
                        <div style={{ marginLeft: 650 }}>
                            <Button className="mx-2" type="primary" size="medium" onClick={this.handleGoToHome}>Home</Button>
                        </div>
                        <br />
                        <div >{resultsContent}</div>

                    </Card>

                </div>
            </div>
        )
    }
}

export default HackathonResults