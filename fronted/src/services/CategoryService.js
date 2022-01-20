import axios  from 'axios'
// import authHeader from '../helpers/authHeader'
import ParametrageService from '../services/ParametrageService'
import authHeader from '../helpers/authHeader'

class CategoryService{
    url=ParametrageService.BASE_API_URL + "/categories"
    urlSubcategorias=ParametrageService.BASE_API_URL + "/subcategories"

    getallCategories () {
        return axios.get(this.url);
    }

    async addCategory (categoryName) {
        const response = await axios.post(this.url, {
            name: categoryName
        },
        { headers: authHeader() })
        return response.data
    }

    async addSubcategory (categoryId, subcategoryName) {
        const response = await axios.post(this.url + '/' + categoryId + '/subcategories', {
            name: subcategoryName
        },
        { headers: authHeader() })
        return response.data
    }

    async deleteCategory (categoryId) {
        const response = await axios.delete(this.url + '/' + categoryId, { headers: authHeader() })
        return response
    }

    async deleteSubcategory (subcategoryId) {
        const response = await axios.delete(this.urlSubcategorias + '/' + subcategoryId, { headers: authHeader() })
        return response
    }
}
export default new CategoryService