import axios from 'axios'
import ParametrageService from '../services/ParametrageService'

export default class RegisterService {

    url = ParametrageService.BASE_API_URL + "/auth/"

    register(user) {
        return axios.post(this.url+"register", user).then(response => {
            return response.data;
        })
    }
}