import axios  from 'axios'
import ParametrageService from '../services/ParametrageService'

class CartService{
    url= ParametrageService.BASE_API_URL + "/users/cart/"
    
    addProduct(id_prod) {
        return axios.post(this.url+localStorage.getItem('user')+'/'+id_prod)
    }
    


    deleteProducts(id_prod) {
        console.log("Recibe"+id_prod)
        return axios.delete(this.url+'delete/'+localStorage.getItem('user')+'/'+id_prod);
    }

    totalCarrito(carrito){
        var total=0
        JSON.parse(carrito).forEach(element => {
            total=Math.round((total+(element.product.price*element.quantity)) * 100) / 100
        });
          
        return total
    }
}
export default new CartService