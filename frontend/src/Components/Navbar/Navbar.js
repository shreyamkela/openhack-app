import React, { Component } from 'react'
import '../../App.css'
import { Menu, Icon} from 'antd';
import { Row, Col } from 'antd';
import {Link} from 'react-router-dom'





class NavBar extends Component {

    state = {
        current: 'Challenges',
    }

    handleClick = (e) => {
        console.log('click ', e);
        this.setState({
            current: e.key,
        });
    }

    render() {

        return (
            <div class="pb-4">
                <Row>
                    <Col span={16}>
                        <Menu
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
                            <Menu.Item key="Organisations">
                                <Link to="/organisation">
                                    <Icon type="home" />Organisations
                                </Link>
                            </Menu.Item>
                        </Menu>
                    </Col>
                    <Col span={4} offset={4}>
                        <Menu
                        onClick={this.handleClick}
                        selectedKeys={[this.state.current]}
                        mode="horizontal"
                        >
                            <Menu.Item key="Login">
                                <Link to="/login">
                                    <Icon type="user"/>Login
                                </Link>
                                
                            </Menu.Item>
                        </Menu>
                    </Col>
                </Row>
            </div>
        )
    }
}

export default NavBar;