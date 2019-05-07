import {
    Form, Input, Tooltip, Icon, Button, AutoComplete,
} from 'antd';
import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import '../../css/LoginMain.css';
import axios from 'axios';
import Navbar from '../Navbar/Navbar';
import firebase_con from '../../Config/firebase';
import Home from '../Challenges/Home';
import Login from '../Login/Login';
var swal = require('sweetalert');
var userIsVerified = false;
var screenNameUnique = true;


class Signup extends Component {
    constructor(props) {
        super(props);
        this.state = {
            confirmDirty: false,
            user: {},
            values: {},
            screennames: {},
        }
        this.verifyScreenNames = this.verifyScreenNames.bind(this);
    }
    componentDidMount() {
        this.authListener();
        this.verifyScreenNames();
    }

    authListener() {
        firebase_con.auth().onAuthStateChanged((user) => {
            console.log("User " + user);
            if (user) {
                this.setState({ user });
                // localStorage.setItem('userId', user.uid);
                user.sendEmailVerification().then(function () {
                    console.log("Sent Email Verification  ");
                }).catch(function (error) {
                    console.log("Error : " + error.message);
                });

                if (user && !userIsVerified) {
                    user = firebase_con.auth().currentUser;
                    userIsVerified = user.emailVerified;
                    console.log("USer Verified" + userIsVerified);

                }
            }
            else {
                this.setState({ user: null });
                localStorage.removeItem('userId');
            }

        });
    }

    async verifyScreenNames() {
        axios.default.withCredentials = true;
        const response = await axios.get('http://localhost:8080/getallscreennames')
        // .then(response => {
        if (response.status === 200) {
            console.log("Lol " + JSON.stringify(response));
            this.setState({ screennames: response.data });
        }
        // });
        return await response.json;
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                var noerror = true;

                console.log('Received values of form: ', values);

                // async getData(){
                //     const res = await axios('/data');
                //     return await res.json();
                //  }

                console.log(" Screenname " + JSON.stringify(this.state.screennames));
                if (this.state.screennames.indexOf(values.screenName) > -1) {
                    screenNameUnique = false;
                    swal('ScreenName Already exists, Use different Screen Name', "", "error");
                }
                else{
                    screenNameUnique=true;
                }


                // this.setState({ values: values });
                if (screenNameUnique) {
                    firebase_con.auth().createUserWithEmailAndPassword(values.email, values.password).catch(function (error) {
                        if (error.message == "auth/email-already-in-useThe email address is already in use by another account.") {
                            swal("Email already registered! Kindly use new email to register", "", "error");
                        }
                        if (error.message != null) {
                            swal(error.message, "", "error");
                            noerror = false;
                        }
                        console.log("Error " + error.code + error.message);
                    });
                    if (noerror) {
                        swal("Verification Email sent please verify to create your account", "", "success");
                        if (values.email.includes("@sjsu.edu")) {
                            values.usertype = "admin";
                        }
                        else {
                            values.usertype = "user";
                        }
                        values.verified = "N";
                        console.log("Send Axios ");
                        axios.defaults.withCredentials = true;
                        axios.post('http://localhost:8080/adduser', values)
                            .then(response => {
                                console.log("Status Code : ", response.status);
                                console.log("Here", JSON.stringify(response));
                                if (response.status === 200) {
                                    this.props.history.push("/login");
                                }
                                else {
                                    swal("Kindly Register again with correct data", "", "error");
                                }
                            });
                    }
                }
            }
        });

    }


    handleConfirmBlur = (e) => {
        const value = e.target.value;
        this.setState({ confirmDirty: this.state.confirmDirty || !!value });
    }

    compareToFirstPassword = (rule, value, callback) => {
        const form = this.props.form;
        if (value && value !== form.getFieldValue('password')) {
            callback('Two passwords that you enter is inconsistent!');
        } else {
            callback();
        }
    }

    validateToNextPassword = (rule, value, callback) => {
        if (value.length < 6) {
            callback('Length should be greater than 6');
        }
        const form = this.props.form;
        if (value && this.state.confirmDirty) {
            form.validateFields(['confirm'], { force: true });
        }
        callback();
    }
    validateLength = (rule, value, callback) => {

        if (value.length < 3) {
            callback('Length should be greater than 3');
        }

        callback();
    }


    render() {
        const { getFieldDecorator } = this.props.form;

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


        return (
            <div>
                <Navbar></Navbar>
                {/* {this.state.user ? <Home /> : <Login />} */}
                <h3 align="center"><b> Sign Up</b></h3>
                <div className='signup-center'>
                    <Form {...formItemLayout} onSubmit={this.handleSubmit} className='signup-form'>
                        <Form.Item
                            label="First Name">
                            {getFieldDecorator('name', {
                                rules: [{
                                    required: true, message: 'Please input your Name!',
                                }],
                            })(
                                <Input />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="Last Name">
                            {getFieldDecorator('lastname', {
                            })(
                                <Input />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="E-mail">
                            {getFieldDecorator('email', {
                                rules: [{
                                    type: 'email', message: 'The input is not valid E-mail!',
                                }, {
                                    required: true, message: 'Please input your E-mail!',
                                }],
                            })(
                                <Input />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="Password">
                            {getFieldDecorator('password', {
                                rules: [{
                                    required: true, message: 'Please input your password!',
                                }, {
                                    validator: this.validateToNextPassword,
                                    // validator: this.validateLength,
                                }],
                            })(
                                <Input type="password" />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="Confirm Password">
                            {getFieldDecorator('confirm', {
                                rules: [{
                                    required: true, message: 'Please confirm your password!',
                                }, {
                                    validator: this.compareToFirstPassword,
                                    // validator: this.validateLength,

                                }],
                            })(
                                <Input type="password" onBlur={this.handleConfirmBlur} />
                            )}
                        </Form.Item>
                        <Form.Item
                            label={(
                                <span>
                                    Screenname&nbsp;
                <Tooltip title="What do you want others to call you?">
                                        <Icon type="question-circle-o" />
                                    </Tooltip>
                                </span>
                            )}>
                            {getFieldDecorator('screenName', {
                                rules: [{ required: true, message: 'Please input your Screen name! ', whitespace: false },
                                {
                                    validator: this.validateLength,
                                }],
                            })(
                                <Input />
                            )}
                        </Form.Item>
                        <Form.Item {...tailFormItemLayout}>
                            <Button type="primary" htmlType="submit">Register</Button>
                        </Form.Item>
                    </Form>
                </div>
            </div>
        );
    }
}


export default Form.create()(Signup);