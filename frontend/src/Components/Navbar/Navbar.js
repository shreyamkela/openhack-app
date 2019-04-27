import React, { Component } from 'react'
import '../../App.css'
import { Menu, Icon } from 'antd';
import { Row, Col, AutoComplete, Badge } from 'antd';
import { Link } from 'react-router-dom'





class NavBar extends Component {

    state = {
        current: 'Challenges',
        dataSource: []
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
            dataSource: ["Organisation 1","Organisation 2","Organisation 3"]
        })
    }
    handleClick = (e) => {
        console.log('click ', e);
        this.setState({
            current: e.key,
        });
    }

    render() {

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
                <Menu.Item key="Organisations">
                    <Link to="/organisation">
                        <Icon type="home" />Organisations
                </Link>
                </Menu.Item>
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
                    </Col>
                    <Col span={8} offset={4}>
                        {rightMenuItems}
                    </Col>
                </Row>
            </div>
        )
    }
}

export default NavBar;