import {
    Form, Input, Tooltip, Icon, Button, AutoComplete, Select, Row, Col
} from 'antd';
import React, { Component } from 'react';
import Navbar from '../Navbar/Navbar';
import axios from 'axios';
var swal = require('sweetalert');

const { Option } = Select;
const AutoCompleteOption = AutoComplete.Option;
class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            autoCompleteResult: [],
            dataSource: [],
            // modalVisible: false,
            // confirmLoading: false,
            // owner_id: null,
            organizations: [],
            user: '',
            address: '',
            userOrganization:''
        }

        this.fetchOrganizations = this.fetchOrganizations.bind(this);
    }
    componentDidMount() {
        axios.defaults.withCredentials = true;
        axios.get(`http://localhost:8080/getuserid/${localStorage.getItem("userId")}`)
            .then(response => {
                if (response.status === 200) {
                    this.setState({
                        user: response.data,
                        address: response.data.address,
                        userOrganization: response.data.organization
                    })
                    console.log("User " + JSON.stringify(this.state.user));
                    console.log("Address " + JSON.stringify(this.state.address));
                }
            });
        axios.defaults.withCredentials = true;
        axios.get('http://localhost:8080/hacker/getOrganizations')
            .then((response) => {
                console.log("Status Code : ", response.status);
                if (response.status === 200) {
                    console.log("Response received from backend");
                    console.log(JSON.stringify(response.data.organizations));
                    this.setState({
                        organizations: response.data.organizations,
                        // dataSource:response.data.organizations
                    });
                    // console.log("The number of the organizations is" + response.data.organizations.length);
                    // var org_names = []
                    // for (let i = 0; i < response.data.organizations.length; i++) {
                    //     org_names.push(response.data.organizations[i].name)
                    // }
                    // this.setState({
                    //     dataSource: org_names
                    // });
                    // console.log("\nOrganization names : " + org_names)
                }
            })
            .catch(err => {
                console.log(err);
            });
    }

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                console.log("this.handleOrganizationChange " + this.handleOrganizationChange);

                axios.defaults.withCredentials = true;
                axios.put(`http://localhost:8080/updateuser/${localStorage.getItem("userId")}`, values)
                    .then(response => {
                        console.log("Status Code : ", response.status);
                        console.log("Here", JSON.stringify(response));
                        if (response.status === 200) {
                            swal("Update Successful", "", "success");
                        }
                        else {
                            swal("Kindly Register again with correct data", "", "error");
                        }
                    });
            }
        });
    }

    fetchOrganizations = (value) => {

        // if(value===""){
        //     this.setState({
        //         filteredUsers:this.state.users
        //     })
        // }else{
        var organizations = this.state.organizations.filter(org => {
            console.log(org.name, " and ", value)
            return org.name.startsWith(value)
        })
        console.log(organizations)
        this.setState({
            organizations: organizations
        })
        // }
    }
    onOrganizationSelect = (value, option) => {
        var org = this.state.organizations.filter(user => user.id == option.key)
        // var members = this.state.members
        // members.push(member[0].id)
        console.log("org " + JSON.stringify(org))
        var users = this.state.organizations.filter(user => {
            return user.id != option.key
        })
        console.log(users)
        if (this.state.user.organization.id === org[0].id) {
            swal("You are already a part of this Organization", "", "error");
        }
        else {
            axios.defaults.withCredentials = true;
            axios.put(`http://localhost:8080/hacker/requestJoinOrganization/${localStorage.getItem("userId")}/${org[0].id}`)
                .then(response => {
                    if (response.status === 200) {
                        swal("Approval request sent to join the organization", "", "success");
                    }
                })
        }


    }
    renderOption = (item) => {
        console.log(`renderOption.item`, item);
        return (
            <AutoComplete.Option key={item.id} text={item.name}>
                <span>{item.name}</span>
            </AutoComplete.Option>
        );
    }


    render() {
        const { getFieldDecorator } = this.props.form;
        const { autoCompleteResult } = this.state;

        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 8 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
        };
        const tailFormItemLayout = {
            wrapperCol: {
                xs: {
                    span: 24,
                    offset: 0,
                },
                sm: {
                    span: 16,
                    offset: 8,
                },
            },
        };

        // const prefixSelector = getFieldDecorator('prefix', {
        //     initialValue: '1',
        // })(
        //     <Select style={{ width: 70 }}>
        //         <Option value="1" key="1">+1</Option>
        //         <Option value="87" key="2">+87</Option>
        //         <Option value="86" key="3">+86</Option>
        //         <Option value="91" key="4">+91</Option>
        //     </Select>
        // );

        return (<div>
            <Navbar></Navbar>
            <h3 align="center"> Update Profile</h3>
            <div className='signup-center'>
                <Form {...formItemLayout} onSubmit={this.handleSubmit} className='signup-form'>
                    <Form.Item
                        label="Title">
                        {getFieldDecorator('title')(
                            <AutoComplete
                                onChange={this.handleTitleChange}
                                placeholder={this.state.user.title}
                            >
                                <Input />
                            </AutoComplete>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="AboutMe"
                    >
                        {getFieldDecorator('aboutMe')(
                            <AutoComplete
                                onChange={this.handleAboutMeChange}
                                placeholder={this.state.user.aboutMe}
                            >
                                <Input />
                            </AutoComplete>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="Street">
                        {getFieldDecorator('street')(
                            <AutoComplete
                                onChange={this.handleStreetChange}
                                placeholder={this.state.address.street}
                            >
                                <Input />
                            </AutoComplete>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="Zip">
                        {getFieldDecorator('zip')(
                            <AutoComplete
                                onChange={this.handleZipChange}
                                placeholder={this.state.address.zip}
                            >
                                <Input />
                            </AutoComplete>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="City">
                        {getFieldDecorator('city')(
                            <AutoComplete
                                onChange={this.handleCityChange}
                                placeholder={this.state.address.city}
                            >
                                <Input />
                            </AutoComplete>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="State">
                        {getFieldDecorator('state')(
                            <AutoComplete
                                onChange={this.handleStateChange}
                                placeholder={this.state.address.state}
                            >
                                <Input />
                            </AutoComplete>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="Country">
                        {getFieldDecorator('country')(
                            <AutoComplete
                                onChange={this.handleCountryChange}
                                placeholder={this.state.address.country}
                            >
                                <Input />
                            </AutoComplete>
                        )}
                    </Form.Item>
                    <Form.Item label="Join Organization">

                        {/* {getFieldDecorator('organization')(
                            <AutoComplete
                                //style={{ width: 200 }}
                                //dataSource = {this.state.dataSource && this.state.dataSource.map(this.renderOption)}
                                dataSource={this.state.dataSource && this.state.dataSource}
                                // rowKey={this.state.dataSource.id}
                                onChange={this.handleOrganizationChange}
                                // placeholder={this.state.user.organization} //"Find Organizations"
                                onFocus={this.fetchOrganizations}
                                allowClear={true}
                            ></AutoComplete>
                        )} */}
                        {getFieldDecorator('organization', {})(
                            <AutoComplete
                                // style={{ width: 200 }}
                                dataSource={this.state.organizations && this.state.organizations.map(this.renderOption)}
                                onSelect={this.onOrganizationSelect}
                                onSearch={this.fetchOrganizations}
                                placeholder="Select Organization"
                                allowClear={true}
                            >
                            </AutoComplete>
                        )}

                    </Form.Item>
                    <Form.Item {...tailFormItemLayout}>
                        <Button type="primary" htmlType="submit">Update Profile</Button>
                    </Form.Item>
                </Form>
            </div>
        </div>);
    }
}

export default Form.create()(Profile);