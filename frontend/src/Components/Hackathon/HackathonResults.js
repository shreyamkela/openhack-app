import React, { Component } from 'react'

import { Button, message, Skeleton, Card } from 'antd';

import API from '../../utils/API'

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
            console.log("FFFFFFFFFFFFFFFff", this.state.hackathonResults)
            hackathonResults.sort((a, b) => {
                return b["grade"] - a["grade"];
            });
            console.log("FFFFFFFFFFFFFFFff", hackathonResults)
            resultsContent = <p>Grades assigned.</p>
        }
        else {
            resultsContent = <b>No grades assigned yet.</b>
        }



        return (
            <div style={{ marginLeft: 280, marginTop: 100 }}>
                <div style={{ textAlign: "center" }}>
                    <Card title="Hackathon Results" style={{ background: '#ECECEC', width: 800 }}>
                        <div style={{ marginLeft: 650 }}>
                            <Button className="mx-2" type="primary" size="medium" onClick={this.handleGoToHome}>Home</Button>
                        </div>
                        {resultsContent}
                    </Card>

                </div>
            </div>
        )
    }
}

export default HackathonResults