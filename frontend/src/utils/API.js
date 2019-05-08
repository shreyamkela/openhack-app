import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:8080",
    withCredentials: true // NOTE - If at some route withCredentials should be false then set withCredentials = false just in that route code
});
