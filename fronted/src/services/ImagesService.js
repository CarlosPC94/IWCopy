import axios from 'axios'
import ParametrageService from '../services/ParametrageService'
//import authHeader from '../helpers/authHeader'

class ImagesService {

    url = ParametrageService.BASE_API_URL + "/users/image"

    getAll() {
        return axios.get(this.url+"all");
    }

    getUser() {
        return axios.get(ParametrageService.BASE_API_URL + "/users/perfil/"+ localStorage.getItem('user'))
    }

    async saveImage(imagen, url) {
        let data = new FormData();
        data.append('file', imagen);
        let config = 
        {
            header : {
                'Content-Type': 'multipart/form-data',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }               
        }
   
        const response = await axios.post(url, data, {headers: config.header})
        console.log(response)
    }
}

export default new ImagesService();