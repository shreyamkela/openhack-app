import React, { Component } from 'react'
import '../../App.css'
import { Menu, Icon, Divider, Form, Input, DatePicker, InputNumber, Avatar } from 'antd';
import { Row, Col, AutoComplete, Badge, Steps, Button, message } from 'antd';
import { Link } from 'react-router-dom'
import Title from 'antd/lib/typography/Title';
import NavBar from '../Navbar/Navbar';
class HackathonRegister extends Component {

    state = {
        teamName: null,
        teamNameErrFlag: true,
        idea: null,
        users: [],
        filteredUsers: [],
        members: [],
        membersErrFlag: true,
        membersErr: "Select atleast 1 member",
        min: 2,
        max: 2
    }

    componentDidMount() {
        this.setState({
            users: [
                {
                    "id": 1,
                    "firstname": "Sayalee",
                    "lastname": "Bhusari"
                },
                {
                    "id": 2,
                    "firstname": "Kavina",
                    "lastname": "Desai"
                },
                {
                    "id": 3,
                    "firstname": "Disha",
                    "lastname": "Kapadia"
                },
            ]
        }, () => {
            this.setState({
                filteredUsers: this.state.users
            })
        })
    }
    onChangeName = (e) => {
        if (e.target.value) {
            this.setState({
                teamName: e.target.value,
                teamNameErrFlag: false
            })
        } else {
            this.setState({
                name: e.target.value,
                teamNameErrFlag: true
            })
        }
    }
    onChangeIdea = (e) => {
        this.setState({
            idea: e.target.value
        })
    }

    renderOption = (item) => {
        console.log(`renderOption.item`, item);
        return (
            <AutoComplete.Option key={item.id} text={item.firstname}>
                <span>{item.firstname}</span>
            </AutoComplete.Option>
        );
    }

    onMemberSelect = (value, option) => {
        var member = this.state.users.filter(user => user.id == option.key)
        var members = this.state.members
        members.push(member[0])
        console.log(members)
        var users = this.state.users.filter(user => {
            return user.id != option.key
        })
        if(members.length < this.state.min){
            this.setState({
                users: users,
                members: members,
                membersErr: "Select more members",
                membersErrFlag: true
            })
        }else if(members.length === this.state.max){
            this.setState({
                users: users,
                members: members,
                membersErr: "Maximum members done! Please dont add now!",
                membersErrFlag: false
            })
        }
    }


    filterDataSource = (value) => {
        if(value===""){
            this.setState({
                filteredUsers:this.state.users
            })
        }else{
            var filteredUsers = this.state.filteredUsers.filter(user => {
                console.log(user.firstname," and ",value)
                return user.firstname.startsWith(value)
            })
            console.log(filteredUsers)
            this.setState({
                filteredUsers:filteredUsers
            })
        }
    }
    render() {

        const { getFieldDecorator } = this.props.form
        var teamAvatars = null
        if(this.state.members){
            teamAvatars = this.state.members.map(member => {
                return(
                    <Col span={4}>
                        <a id={member.id} onClick={this.removeMember}>
                            <Avatar shape="square" size="large" icon="user" />
                        </a>
                        <p>{member.firstname}</p>
                    </Col>
                )
            })
        }
        return (

            <div style={{ "width": "100%", "height": "610px" }} class="blur-bg">
                <div class="hackathon-create p-5" style={{ "backgroundColor": "white" }}>
                    <Title style={3}>Register to Hackathon</Title>
                    <Divider></Divider>
                    <Form onSubmit={this.createHackathon}>
                        <Form.Item
                            label="Team Name"
                        >
                            {getFieldDecorator('name', {
                                rules: [{ required: true, message: "Team Name is required" }],
                            })(
                                <Input prefix={<Icon type="snippets" style={{ color: 'rgba(0,0,0,.25)' }} />} id="name" placeholder="Team Name" onChange={this.onChangeName} />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="Idea"
                        >
                            {getFieldDecorator('idea', {})(
                                <Input prefix={<Icon type="snippets" style={{ color: 'rgba(0,0,0,.25)' }} />} id="idea" placeholder="Idea" onChange={this.onChangeIdea} />
                            )}
                        </Form.Item>
                        <Divider></Divider>
                        <Row type="flex">
                            
                                {teamAvatars}
                            
                        </Row>
                        <Form.Item
                            label="Team Members"
                        >
                            {getFieldDecorator('team', {})(
                                <AutoComplete
                                    style={{ width: 300 }}
                                    dataSource={this.state.filteredUsers && this.state.filteredUsers.map(this.renderOption)}
                                    onSelect={this.onMemberSelect}
                                    onSearch={this.filterDataSource}
                                    placeholder="Select Team Members"
                                    allowClear={true}
                                >
                                </AutoComplete>
                            )}
                        </Form.Item>
                        <p class="text-danger">{this.state.membersErr}</p>
                        <Form.Item>
                            <Button
                                type="primary"
                                htmlType="submit"
                                disabled={this.state.teamNameErrFlag || this.state.membersErrFlag}
                            >
                                Log in
                            </Button>
                        </Form.Item>
                    </Form>
                </div>
            </div>
        )
    }
}

export default Form.create()(HackathonRegister);
