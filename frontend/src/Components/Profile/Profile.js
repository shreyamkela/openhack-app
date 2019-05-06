import {
    Form, Input, Tooltip, Icon, Button, AutoComplete,Select
} from 'antd';
import React, { Component } from 'react';
import Navbar from '../Navbar/Navbar';
import axios from 'axios';
var swal = require('sweetalert');

const { Option } = Select;
const AutoCompleteOption = AutoComplete.Option;
class Profile extends Component {
    state = {
        autoCompleteResult: [],
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);

                axios.defaults.withCredentials = true;
                axios.post('http://localhost:8080/updateuser', values)
                    .then(response => {
                        console.log("Status Code : ", response.status);
                        console.log("Here", JSON.stringify(response));
                        if (response.status === 200) {
                            swal("Signup Successful", "", "success");
                        }
                        else {
                            swal("Kindly Register again with correct data", "", "error");
                        }
                    });
            }
        });
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
                <Option value="1">+1</Option>
                <Option value="87">+87</Option>
                <Option value="86">+86</Option>
                <Option value="91">+91</Option>
            </Select>
        );

        return (<div>
            <Navbar></Navbar>
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
        </div>);
    }
}

export default Form.create()(Profile);