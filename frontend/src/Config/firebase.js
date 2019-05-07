import firebase from 'firebase';

const config = { /* COPY THE ACTUAL CONFIG FROM FIREBASE CONSOLE */
    apiKey: 'AIzaSyC7hSxpbLOvl140m_g-bzRfhJcj6WZIFFs',
    authDomain: 'my-project-sayalee.firebaseapp.com',
    databaseURL: 'https://visual-studio-sync.firebaseio.com',
    projectId: 'visual-studio-sync',
    storageBucket: 'visual-studio-sync.appspot.com',
    messagingSenderId: '691628632398'
};
const firebase_con = firebase.initializeApp(config);
export default firebase_con;