import React, { Component } from 'react'
import '../../App.css'
import { Menu, Icon } from 'antd';
import { Row, Col, AutoComplete, Badge, Button, Modal, Form, Input } from 'antd';
import { Link } from 'react-router-dom'





class NavBar extends Component {

    state = {
        current: 'Challenges',
        dataSource: [],
        modalVisible : false,
        confirmLoading : false,
        owner_name : null
    }

    // renderOption = (item) => {
    //     console.log(`renderOption.item`, item);
    //     return (
    //       <AutoComplete.Option key={item.name} text={item.name}>
    //         <span>{item.name}</span>
    //       </AutoComplete.Option>
    //     );
    //   }


    // onOrganisationSelect = (value,option) => {
    //     console.log("Value:",value)
    //     console.log("Option:",option)
    // }

    componentDidMount() {
        this.setState({
            dataSource: ["Organisation 1","Organisation 2","Organisation 3"],
            owner_name : "User1"
        })
    }

    handleClick = (e) => {
        console.log('click ', e);
        this.setState({
            current: e.key,
        });
    }

    createOrgModal = () => {
        this.setState({
            modalVisible: true,
          });
        console.log("\nCreate organization button clicked!!");
    }

    handleOk = () => {
        this.setState({
          ModalText: 'The modal will be closed in one seconds',
          confirmLoading: true,
        });
        setTimeout(() => {
          this.setState({
            modalVisible: false,
            confirmLoading: false,
          });
        }, 1000);
        console.log("\nOkay button of the modal pressed")
    }
    
    handleCancel = () => {
    console.log('Clicked cancel button');
    this.setState({
        modalVisible: false,
    });
    }

    createOrganization = (e) => {
        e.preventDefault();
        // console.log("\nPrinting e : ")
        // console.log(e)
        this.props.form.validateFields((err, values) => {
            if (!err) {
              console.log('Received values of form: ', values);
              console.log("Name of the organization : "+values.name)
              console.log("Description of the organization : "+values.description)
            }
        });
    }

    render() {
        const { modalVisible, confirmLoading } = this.state;
        const { getFieldDecorator } = this.props.form;
        var leftMenuItems = null;
        var rightMenuItems = null;
        if (localStorage.getItem("userId")) {
            leftMenuItems = <Menu
                onClick={this.handleClick}
                selectedKeys={[this.state.current]}
                mode="horizontal"
            >
                <Menu.Item>
                    OpenHack
                </Menu.Item>
                <Menu.Item key="Challenges">
                    <Link to="/challenges">
                        <Icon type="snippets" />Challenges
                </Link>
                </Menu.Item>
                {/* <Menu.Item key="Organisations"> */}
                    {/* <Link to="/organisation"> */}
                        <Button onClick = {this.createOrgModal}> <Icon type="home" /> Create Organisations</Button>
                        
                {/* </Link> */}
                {/* </Menu.Item> */}
            </Menu>
            
            rightMenuItems = <div>
                <br></br>
                <Row type="flex" justify="end">
                    <Col span={12}>
                        <AutoComplete
                        style={{ width: 200 }}
                        //dataSource = {this.state.dataSource && this.state.dataSource.map(this.renderOption)}
                        dataSource = {this.state.dataSource && this.state.dataSource}
                        //onSelect = {this.onOrganisationSelect}
                        placeholder="Find Organisations"
                        allowClear={true}
                        ></AutoComplete>
                    </Col>
                    <Col span={6}>
                        <Badge count={4} style={{ backgroundColor: '#52c41a' }}>
                            <Link to="/messages">
                                <Icon type="message" /> Messages
                            </Link>
                        </Badge>
                    </Col>
                    <Col span={6}>
                        <Badge style={{ backgroundColor: '#52c41a' }}>
                            <Link to="/profile">
                                <Icon type="user" /> Hey xyz
                            </Link>
                        </Badge>
                    </Col>
                </Row>
            </div>
        } else {
            leftMenuItems = <Menu
                onClick={this.handleClick}
                selectedKeys={[this.state.current]}
                mode="horizontal"
            >
                <Menu.Item>
                    OpenHack
            </Menu.Item>
            </Menu>
            rightMenuItems = <div>
                <br></br>
                <Row type="flex" justify="end">
                    <Col span={24}>

                        <Link to="/login">
                            <Icon type="user" /> Login
                        </Link>

                    </Col>
                </Row>
            </div>
        }

        return (
            <div class="pb-4">
                <Row>
                    <Col span={12}>
                        {leftMenuItems}
                        <Modal
                            title="Create a new organization"
                            visible={modalVisible}
                            onOk={this.handleOk}
                            confirmLoading={confirmLoading}
                            onCancel={this.handleCancel}
                            >
                                      <Form 
                                      layout="vertical"
                                      onSubmit={this.createOrganization}
                                      >
                                        <Form.Item label="Organization name">
                                            {getFieldDecorator('name', {
                                                rules: [{ required: true, message: 'Please input the name of the organization' }],
                                            })(<Input />)}
                                        </Form.Item>
                                        <Form.Item label="Description">
                                            {getFieldDecorator('description')(<Input type="textarea" rows="3" cols="10"/>)}
                                        </Form.Item>
                                        <Form.Item label="Street Address">
                                            {getFieldDecorator('street')(<Input />)}
                                        </Form.Item>
                                        <Form.Item label="City">
                                            {getFieldDecorator('city')(<Input />)}
                                        </Form.Item>
                                        <Form.Item label="State">
                                            {getFieldDecorator('state')(<Input />)}
                                        </Form.Item>
                                        <Form.Item label="Zip code">
                                            {getFieldDecorator('zip')(<Input />)} 
                                        </Form.Item>
                                        <Form.Item label="Country">
                                            {getFieldDecorator('country')(<Input />)} 
                                        </Form.Item>
                                        <Form.Item>
                                            <Button type="primary" htmlType="submit">Create</Button>
                                        </Form.Item>
                                    </Form>
                        </Modal>
                    </Col>
                    <Col span={8} offset={4}>
                        {rightMenuItems}
                    </Col>
                </Row>
            </div>
        )
    }
}

export default Form.create()(NavBar);