import axios from 'axios'

const request = axios.create({
  baseURL: 'http://43.139.233.117:8080',
  timeout: 5000,
  headers: {
    platform: 'H5',
    'Content-Type': 'application/json',
  },
})

export default request
