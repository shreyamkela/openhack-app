import {
    Form, Input, Tooltip, Icon, Select, Checkbox, Button, AutoComplete,
} from 'antd';
import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import '../../css/LoginMain.css';
import NavBar from '../Navbar/Navbar';
import axios from 'axios';
var swal = require('sweetalert');





const { Option } = Select;
const AutoCompleteOption = AutoComplete.Option;

class Signup extends Component {
    state = {
        confirmDirty: false,
        autoCompleteResult: [],
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);

                axios.defaults.withCredentials = true;
                axios.post('http://localhost:8080/adduser', values)
                    .then(response => {
                        console.log("Status Code : ", response.status);
                        console.log("Here", JSON.stringify(response));
                        if (response.status === 200) {
                            swal("Signup Successful", "", "success");
                        }
                        else{
                            swal("Kindly Register again with correct data", "", "error");
                        }
                    });
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
        const form = this.props.form;
        if (value && this.state.confirmDirty) {
            form.validateFields(['confirm'], { force: true });
        }
        callback();
    }

    handleWebsiteChange = (value) => {
        let autoCompleteResult;
        if (!value) {
            autoCompleteResult = [];
        } else {
            autoCompleteResult = ['.com', '.org', '.net'].map(domain => `${value}${domain}`);
        }
        this.setState({ autoCompleteResult });
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
        const prefixSelector = getFieldDecorator('prefix', {
            initialValue: '1',
        })(
            <Select style={{ width: 70 }}>
                <Option value="1">+</Option>
                <Option value="87">+87</Option>
                <Option value="86">+86</Option>
                <Option value="91">+91</Option>
            </Select>
        );

        const websiteOptions = autoCompleteResult.map(website => (
            <AutoCompleteOption key={website}>{website}</AutoCompleteOption>
        ));

        return (
            <Form {...formItemLayout} onSubmit={this.handleSubmit} className='signup-form'>
                <Form.Item
                    label="Title">
                    {getFieldDecorator('title', {
                        rules: [{
                            required: true, message: 'Please input your Title!',
                        }],
                    })(
                        <Input />
                    )}
                </Form.Item>
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
                        rules: [{ required: true, message: 'Please input your Screen name!', whitespace: true }],
                    })(
                        <Input />
                    )}
                </Form.Item>
                <Form.Item
                    label="Phone Number">
                    {getFieldDecorator('phone', {
                        rules: [{ required: true, message: 'Please input your phone number!' }],
                    })(
                        <Input addonBefore={prefixSelector} style={{ width: '100%' }} />
                    )}
                </Form.Item>
                <Form.Item
                    label="AboutMe">
                    {getFieldDecorator('aboutMe')(
                        <AutoComplete
                            onChange={this.handleAboutMeChange}
                            placeholder="About me">
                            <Input />
                        </AutoComplete>
                    )}
                </Form.Item>
                <Form.Item
                    label="Street">
                    {getFieldDecorator('street')(
                        <AutoComplete
                            onChange={this.handleStreetChange}
                            placeholder="Street">
                            <Input />
                        </AutoComplete>
                    )}
                </Form.Item>
                <Form.Item
                    label="Zip">
                    {getFieldDecorator('zip')(
                        <AutoComplete
                            onChange={this.handleZipChange}
                            placeholder="Zip">
                            <Input />
                        </AutoComplete>
                    )}
                </Form.Item>
                <Form.Item
                    label="City">
                    {getFieldDecorator('city')(
                        <AutoComplete
                            onChange={this.handleCityChange}
                            placeholder="City">
                            <Input />
                        </AutoComplete>
                    )}
                </Form.Item>
                <Form.Item
                    label="State">
                    {getFieldDecorator('state')(
                        <AutoComplete
                            onChange={this.handleStateChange}
                            placeholder="State">
                            <Input />
                        </AutoComplete>
                    )}
                </Form.Item>
                <Form.Item
                    label="Country">
                    {getFieldDecorator('country')(
                        <AutoComplete
                            onChange={this.handleCountryChange}
                            placeholder="Country">
                            <Input />
                        </AutoComplete>
                    )}
                </Form.Item>
                <Form.Item {...tailFormItemLayout}>
                    <Button type="primary" htmlType="submit">Register</Button>
                </Form.Item>
            </Form>
        );
    }
}

const WrappedRegistrationForm = Form.create({ name: 'register' })(Signup);

ReactDOM.render(<WrappedRegistrationForm />, document.getElementById('root'));
export default WrappedRegistrationForm;