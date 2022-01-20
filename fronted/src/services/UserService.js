import axios from 'axios'
import ParametrageService from '../services/ParametrageService'

export default class UserService {

    url = ParametrageService.BASE_API_URL + "/users/"

    getAll() {
        return axios.get(this.url+"all");
    }

    getUser() {
        return axios.get(ParametrageService.BASE_API_URL + "/users/perfil/"+ localStorage.getItem('user'))
    }

    updateUser() {
        
    }

    deleteUser(id) {
        axios.delete("http://localhost:8080/api/users/"+id)
    }

}