import React, { Component } from 'react'
import '../../App.css'
import { Menu, Icon, Divider, Form, Input, DatePicker, InputNumber, Avatar } from 'antd';
import { Row, Col, AutoComplete, Badge, Steps, Button, message } from 'antd';
import { Link } from 'react-router-dom'
import Title from 'antd/lib/typography/Title';
import NavBar from '../Navbar/Navbar';
import axios from 'axios'
import swal from 'sweetalert';
class HackathonPayment extends Component {

    constructor(props) {
        super(props)
        this.state = {
            paymentId: this.props.match.params.paymentId,
            fee: null,
            memberId: null,
            teamId: null
        }
    }

    componentDidMount() {

        axios.get(`http://localhost:8080/payment/${this.state.paymentId}`)
            .then(response => {
                if (response.status === 200) {
                    console.log(response.data)
                    this.setState({
                        fee: response.data.fee,
                        memberId: response.data.memberId,
                        teamId: response.data.teamId
                    })
                }
            })
            .catch(err => {
                console.log(err);
            })
    }

    doPayment = (e) => {

        let body = {
            "teamId":this.state.teamId,
            "memberId":this.state.memberId
        }
        axios.post(`http://localhost:8080/payment/${this.state.paymentId}`,body)
            .then(response => {
                if(response.status===200){
                    swal("Payment Done","success")
                    this.props.history.push("/home")
                }
            })
            .catch(err => {
                console.log(err);
            })

    }
    render() {

        const { getFieldDecorator } = this.props.form
        return (

            <div style={{ "width": "100%", "height": "610px" }} class="blur-bg">
                <div class="hackathon-create p-5" style={{ "backgroundColor": "white" }}>
                    <Title style={3}>Payment for Hackathon</Title>
                    <Divider></Divider>
                    <Form>
                        <Form.Item
                            label="Card Number"
                        >
                            {getFieldDecorator('cardnumber', {
                                rules: [{ required: true, message: "Card Number required" }],
                            })(
                                <Input prefix={<Icon type="snippets" style={{ color: 'rgba(0,0,0,.25)' }} />} id="name" placeholder="XXXX-XXXX-XXXX-XXXX" />
                            )}
                        </Form.Item>
                        <Row type="flex">
                            <Col span={12}>
                                <Form.Item
                                    label="Expiration Date"
                                >
                                    {getFieldDecorator('exp', {
                                        rules: [{ required: true, message: 'Expiration required!' }],
                                    })(
                                        <InputNumber placeholder="MM/YY" />
                                    )}
                                </Form.Item>
                            </Col>
                            <Col span={12}>
                                <Form.Item
                                    label="CVV"
                                >
                                    {getFieldDecorator('dis', {
                                        rules: [{ required: true, message: 'CVV required' }],
                                    })(
                                        <InputNumber placeholder="CVV" />
                                    )}
                                </Form.Item>
                            </Col>
                        </Row>
                        <Form.Item>
                                    <Button
                                        type="primary"
                                        htmlType="submit"
                                        block
                                        size="large"
                                        onClick={this.doPayment}
                                    >
                                        Pay
                                     </Button>
                        </Form.Item>
                    </Form>
                </div>
            </div>
        )
    }
}
export default Form.create()(HackathonPayment);


