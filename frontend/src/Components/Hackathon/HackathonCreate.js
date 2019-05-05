/* eslint-disable eqeqeq */
import React, { Component } from 'react'
import '../../App.css'
import { Icon, Divider, Form, Input, DatePicker, InputNumber, Avatar, message } from 'antd';
import { Row, Col, AutoComplete, Button } from 'antd';
import { Link } from 'react-router-dom'
import Title from 'antd/lib/typography/Title';
import NavBar from '../Navbar/Navbar';
import axios from 'axios';
import swal from 'sweetalert'

class HackathonCreate extends Component {

  state = {
    name: "dar",
    startDate: null,
    endDate: null,
    description: null,
    fee: null,
    teamSizeMin: null,
    teamSizeMax: null,
    discount: null,
    nameErrFlag: true,
    dateErr: "",
    dateErrFlag: true,
    descErr: "",
    descErrFlag: true,
    minErr: "",
    minErrFlag: true,
    maxErr: "",
    maxErrFlag: true,
    feeErrFlag: true,
    current: 0,
    users:[],
    judges:[],
    judgesErr:"You need atleast one judge",
    judgesErrFlag:true,
    organisations:[],
    sponsors:[]
  }

  componentDidMount(){
    this.setState({
      users:[
        {
          "id":1,
          "firstname":"Darshil",
          "lastname":"Kapadia"
        },
        {
          "id":2,
          "firstname":"Kavina",
          "lastname":"Desai"
        },
        {
          "id":3,
          "firstname":"Disha",
          "lastname":"Kapadia"
        },
      ],
      organisations:[
        {
          "id":1,
          "firstname":"Darshil",
          "lastname":"Kapadia"
        },
        {
          "id":2,
          "firstname":"Kavina",
          "lastname":"Desai"
        },
        {
          "id":3,
          "firstname":"Disha",
          "lastname":"Kapadia"
        },
      ],
    })
  }
  onChangeName = (e) => {
    if (e.target.value) {
      this.setState({
        name: e.target.value,
        nameErrFlag: false
      })
    } else {
      this.setState({
        name: e.target.value,
        nameErrFlag: true
      })
    }
  }
  onChangeDesc = (e) => {
    if (e.target.value && e.target.value.length > 10) {
      this.setState({
        description: e.target.value,
        descErrFlag: false,
        descErr: ""
      })
    } else {
      this.setState({
        description: e.target.value,
        descErr: "Description should be greater than 10 characters!",
        descErrFlag: true
      })
    }
  }
  onChangeDates = (e) => {
    if (e && new Date(e[0]._d) < Date.now()) {
      console.log("Invalid Date")
      this.setState({
        startDate: null,
        endDate: null,
        dateErr: "Date is required and start date cannot be before today's date.",
        dateErrFlag: true
      })
    } else {
      this.setState({
        startDate: new Date(e[0]._d),
        endDate: new Date(e[1]._d),
        dateErr: "",
        dateErrFlag: false
      })
    }
  }

  onChangeMin = (e) => {
    console.log("minimum changed")
    if (this.state.teamSizeMax && this.state.teamSizeMax < e) {
      console.log("Minimum size should be less than maximum size")
      this.setState({
        minErr: "Minimum size should be less than maximum size",
        minErrFlag: true
      })
    } else {
      this.setState({
        teamSizeMin: e,
        minErr: "",
        minErrFlag: false
      })
    }
  }

  onChangeMax = (e) => {
    if (this.state.teamSizeMin && this.state.teamSizeMin > e) {
      this.setState({
        maxErr: "Maximum size should be greater than minimum size",
        maxErrFlag: true
      })
    } else {
      this.setState({
        teamSizeMax: e,
        maxErr: "",
        maxErrFlag: false
      })
    }
  }

  onChangeFee = (e) => {
    if (!e) {
      this.setState({
        feeErrFlag: true
      })
    } else {
      this.setState({
        fee: e,
        feeErrFlag: false
      })
    }
  }

  onChangeDis = (e) => {
    this.setState({
      discount: e
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

  onJudgeSelect = (value,option) => {
    var judge = this.state.users.filter(user => user.id == option.key)
    // console.log(judge[0])
    var judges = this.state.judges
    // console.log("Judges: ",judges)
    judges.push(judge[0].id)
    // console.log("Judges: ",judges)
    var users = this.state.users.filter(user => {
      return user.id != option.key
    })
    this.setState({
      judges: judges,
      users:users,
      judgesErrFlag:false,
      judgesErr:""
    },() => {
      console.log(this.state.judges)
    })
  }

  onSponsorSelect = (value,option) => {
    var sponsor = this.state.organisations.filter(organisation => organisation.id == option.key)
    // console.log(sponsor[0])
    var sponsors = this.state.sponsors
    // console.log("sponsors: ",sponsors)
    sponsors.push(sponsor[0].id)
    // console.log("sponsors: ",sponsors)
    var organisations = this.state.organisations.filter(organisation => {
      return organisation.id != option.key
    })
    this.setState({
      sponsors: sponsors,
      organisations:organisations,
    },() => {
      console.log(this.state.sponsors)
    })
  }
  
  createHackathon = (e) => {
    let body = {
      "name":this.state.name,
      "startDate":this.state.startDate,
      "endDate":this.state.endDate,
      "description":this.state.description,
      "teamSizeMin":this.state.teamSizeMin,
      "teamSizeMax":this.state.teamSizeMax,
      "fee":this.state.fee,
      "discount":this.state.discount,
      "judgesId":this.state.judges,
      "sponsorsId":this.state.sponsors
    }
    axios.defaults.withCredentials = true;
    axios.post("http://localhost:8080/hackathon/",body)
      .then( response => {
        console.log(response)
        if(response.status===200){
          console.log("Inside response status 200")
          swal("Hackathon Created!","", "success");
          setTimeout(()=>{

          },5000);
          this.props.history.push("/home")
        }
      })
      .catch(err => {
        swal(err,"", "error");
      })
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    var judgeAvatars=null
    var sponsorAvatars=null
    if(this.state.judges){
      judgeAvatars = this.state.judges.map(judge => {
        return(
          <Col span={4}>
            <Avatar shape="square" size="large" icon="user" />
            <p>{judge.firstname}</p>
          </Col>
        )
      })
    }
    if(this.state.sponsors){
      sponsorAvatars = this.state.sponsors.map(sponsor => {
        return(
          <Col span={4}>
            <Avatar shape="square" size="large" icon="home" />
            <p>{sponsor.firstname}</p>
          </Col>
        )
      })
    }
    return (
      <div>
        <div class="blur-bg"></div>
        <NavBar></NavBar>
        <div class="hackathon-create p-5">
          <Title level={3}>Create a Hackathon Event</Title>
          <Divider></Divider>
          <Form>
            <Form.Item
              label="Event Name"
            >
              {getFieldDecorator('name', {
                rules: [{ required: true, message: "Event Name is required" }],
              })(
                <Input prefix={<Icon type="snippets" style={{ color: 'rgba(0,0,0,.25)' }} />} id="name" placeholder="Event Name" onChange={this.onChangeName} />
              )}
            </Form.Item>
            <Form.Item
              label="Start Date ~ End Date"
            >
              {getFieldDecorator('dates', {
                rules: [{ type: 'array', required: true }],
              })(
                <DatePicker.RangePicker showTime format="MM-DD-YYYY HH:mm:ss"
                  onChange={this.onChangeDates}
                />
              )}
            </Form.Item>
            <p class="text-danger">{this.state.dateErr}</p>
            <Form.Item
              label="Description"
            >
              {getFieldDecorator('description', {
                rules: [{ required: true, message: "Description is required" }],
              })(
                <Input.TextArea rows={4}
                  id="description" onChange={this.onChangeDesc}
                />
              )}
            </Form.Item>
            <p class="text-danger">{this.state.descErr}</p>
            <Row type="flex">
              <Col span={12}>
                <Form.Item
                  label="Min Team Size"
                >
                  {getFieldDecorator('min', {
                    rules: [{ required: true, message: 'Min Size Required!' }],
                  })(
                    <InputNumber onChange={this.onChangeMin} />
                  )}
                </Form.Item>
                <p class="text-danger">{this.state.minErr}</p>
              </Col>
              <Col span={12}>
                <Form.Item
                  label="Max Team Size"
                >
                  {getFieldDecorator('max', {
                    rules: [{ required: true, message: 'Max Size Required!' }],
                  })(
                    <InputNumber onChange={this.onChangeMax} />
                  )}
                </Form.Item>
                <p class="text-danger">{this.state.maxErr}</p>
              </Col>
            </Row>
            <Row type="flex">
              <Col span={12}>
                <Form.Item
                  label="Fees"
                >
                  {getFieldDecorator('fee', {
                    rules: [{ required: true, message: 'Fees required!' }],
                  })(
                    <InputNumber onChange={this.onChangeFee} />
                  )}
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  label="Sponsor's Discount"
                >
                  {getFieldDecorator('dis', {
                    rules: [{ message: 'Max Size Required!' }],
                  })(
                    <InputNumber onChange={this.onChangeDis} />
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Divider></Divider>
            <Row type="flex">
              {judgeAvatars}
            </Row>
            <Form.Item
              label="Judges"
            >
              {getFieldDecorator('judges', {})(
                <AutoComplete
                style={{ width: 300 }}
                dataSource = {this.state.users && this.state.users.map(this.renderOption)}
                //dataSource = {this.state.dataSource && this.state.dataSource}
                onSelect = {this.onJudgeSelect}
                placeholder="Select Judges"
                value={""}
                allowClear={true}
                >
                </AutoComplete>  
              )}
            </Form.Item>
            <p class="text-danger">{this.state.judgesErr}</p>
            <Divider></Divider>
            <Row type="flex">
              {sponsorAvatars}
            </Row>
            <Form.Item
              label="Sponsors"
            >
              {getFieldDecorator('sponsors', {})(
                <AutoComplete
                style={{ width: 300 }}
                dataSource = {this.state.organisations && this.state.organisations.map(this.renderOption)}
                //dataSource = {this.state.dataSource && this.state.dataSource}
                onSelect = {this.onSponsorSelect}
                placeholder="Select Sponsors"
                allowClear={true}
                >
                </AutoComplete>  
              )}
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                disabled={this.state.nameErrFlag || this.state.dateErrFlag || this.state.descErrFlag || this.state.minErrFlag || this.state.maxErrFlag || this.state.feeErrFlag || this.state.judgesErrFlag}
                block
                size="large"
                onClick={this.createHackathon}
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

export default Form.create()(HackathonCreate);