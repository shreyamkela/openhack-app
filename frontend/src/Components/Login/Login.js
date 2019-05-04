import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import {
  Form, Icon, Input, Button, Checkbox,
} from 'antd';
import '../../css/LoginMain.css';
import axios from 'axios';
import GoogleLogin from 'react-google-login';
import FacebookLogin from 'react-facebook-login';
import firebase from "firebase"
import StyledFirebaseAuth from "react-firebaseui/StyledFirebaseAuth"

firebase.initializeApp({
  apiKey: "AIzaSyC7hSxpbLOvl140m_g-bzRfhJcj6WZIFFs",
  authDomain: "my-project-sayalee.firebaseapp.com"
})


class Login extends Component {
  state = { isSignedIn: false }
  uiConfig = {
    signInFlow: "popup",
    signInOptions: [
      firebase.auth.GoogleAuthProvider.PROVIDER_ID,
      firebase.auth.FacebookAuthProvider.PROVIDER_ID,
      firebase.auth.EmailAuthProvider.PROVIDER_ID
    ],
    callbacks: {
      signInSuccess: () => false
    }
  }
  componentDidMount() {
    firebase.auth().onAuthStateChanged(user => {
      this.setState({ isSignedIn: !!user })
      console.log("user", user)
    })

    // axios.defaults.withCredentials = true;
    // axios.get('http://localhost:8080/getuser')
    //   .then(response => {
    //     console.log("Status Code : ", response.status);
    //     console.log("Here", JSON.stringify(response));
    //     if (response.status === 200) {

    //     }
    //   });
  }
  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
        // axios.defaults.withCredentials = true;
        // axios.post('http://localhost:8080/login', values)
        //     .then(response => {
        //         console.log("Status Code : ", response.status);
        //         console.log("Here", JSON.stringify(response));
        //         if (response.status === 200) {
        //         }
        //     });
      }
    });


  }

  render() {
    const { getFieldDecorator } = this.props.form;
    // const responseGoogle = (response) => {
    //   console.log(response);
    // }
    // const responseFacebook = (response) => {
    //   console.log(response);
    // }
    return (<div>
      {/* <GoogleLogin
        clientId="628741954443-9v0avqjgj0ufssangk6ki15kc10hg96q.apps.googleusercontent.com"
        buttonText="Login"
        onSuccess={responseGoogle}
        onFailure={responseGoogle}
        cookiePolicy={'single_host_origin'}
      />
      <FacebookLogin
        appId="2286654528238751"
        autoLoad={true}
        fields="name,email,picture"
        // onClick={componentClicked.bind(this)}
        callback={responseFacebook} /> */}
      {this.state.isSignedIn ? (
        <span>
          <div>Signed In!</div>
          <button onClick={() => firebase.auth().signOut()}>Sign out!</button>
          <h1>Welcome {firebase.auth().currentUser.displayName}</h1>
          <img
            alt="profile picture"
            src={firebase.auth().currentUser.photoURL}
          />
        </span>
      ) : (
          <StyledFirebaseAuth
            uiConfig={this.uiConfig}
            firebaseAuth={firebase.auth()}
          />
        )}
      <Form onSubmit={this.handleSubmit} className="login-form">
        <Form.Item>
          {getFieldDecorator('userName', {
            rules: [{ required: true, message: 'Please input your username!' }],
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Username" />
          )}
        </Form.Item>
        <Form.Item>
          {getFieldDecorator('password', {
            rules: [{ required: true, message: 'Please input your Password!' }],
          })(
            <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" placeholder="Password" />
          )}
        </Form.Item>
        <Form.Item>
          {getFieldDecorator('remember', {
            valuePropName: 'checked',
            initialValue: true,
          })(
            <Checkbox>Remember me</Checkbox>
          )}
          <a className="login-form-forgot" href="">Forgot password</a>
          <Button type="primary" htmlType="submit" className="login-form-button">
            Log in
            </Button>
          Or <a href="/signup">register now!</a>
        </Form.Item>
      </Form>
    </div>
    );
  }
}

const WrappedNormalLoginForm = Form.create({ name: 'normal_login' })(Login);

ReactDOM.render(<WrappedNormalLoginForm />, document.getElementById('root'));

export default WrappedNormalLoginForm;