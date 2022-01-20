import axios from  'axios'
import ParametrageService from '../services/ParametrageService'
import authHeader from '../helpers/authHeader'

class ProductService{
    url=ParametrageService.BASE_API_URL + "/products"

    getAllProducts(){
        return  axios.get(this.url+'/all');
    }
    getAllProductsByCategory(category_id){
        return  axios.get(this.url+'/all/'+category_id);
    }
    getAllProductsBySubcategory(subcategory_id){
        return  axios.get(this.url+'/all/subcategory/'+subcategory_id);
    }
    getAllProductsById(id){
        return  axios.get(this.url+'/'+id);
    }

    async createProduct(newProduct) {
        const response = await axios.post(this.url + '/micreate', newProduct, { headers: authHeader() })
        console.log(response)
        return response.data
    }

    async deleteProduct(productId) {
        const response = await axios.delete(this.url + '/' + productId, { headers: authHeader() })
        console.log(response)
        return response
    }

    //WishList
    getAllProductsByWishList(){
        return axios.get(ParametrageService.BASE_API_URL + "/wishList/products/"+ localStorage.getItem('wishlist'))
    }
  
    getOffProduct(product_id){
        var products = axios.post(ParametrageService.BASE_API_URL + "/wishList/"+localStorage.getItem('wishlist')+"/deleteProduct/" + product_id);
        return products;
    }
    addProductWishList(id_product)
    {
        var products= axios.post(ParametrageService.BASE_API_URL + "/wishList/"+localStorage.getItem('wishlist')+"/add/"+id_product)
        return products;
    }
}

export default new ProductService();