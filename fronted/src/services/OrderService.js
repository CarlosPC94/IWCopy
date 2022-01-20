import axios from "axios"
import ParametrageService from '../services/ParametrageService'
import authHeader from '../helpers/authHeader'

class OrderService{
    async addProductOrderSolo(id_order,data)
    {
        const newPromise  = await axios.post(ParametrageService.BASE_API_URL + "/order/add/"+id_order, data, { headers: authHeader() })
        return newPromise
    }

     addProductOrder(carrito,id_order){
        
        console.log("csdfsf"+JSON.stringify(carrito))
        JSON.parse(carrito).forEach( element => {
            var data = new FormData();
      
            data.append('id_product',element.product.id)
            data.append('cantidad',element.quantity)
            this.addProductOrderSolo(id_order,data)
           console.log("TODO OK"+element.product.id)
        });
    }

    async getPedidos(id_user){
        const pedidos = await axios.get(ParametrageService.BASE_API_URL + "/order/all/"+id_user, { headers: authHeader() })
        return pedidos.data
    }

    async getPedido(id){
        const pedido = await axios.get(ParametrageService.BASE_API_URL + "/order/"+id, { headers: authHeader() })
        return pedido.data;
    }

    async getAllPedidos(){
        const pedido = await axios.get(ParametrageService.BASE_API_URL + "/order/all", { headers: authHeader() })
        return pedido.data;
    }

    async getPedidosByState(state){
        const pedido = await axios.get(ParametrageService.BASE_API_URL + "/order/state/" + state, { headers: authHeader() })
        return pedido.data;
    }

    async changeStatePedido(id, state){
        let data = new FormData();

        data.append('state', state)
    
        let config = 
        {
            header : {
                'Content-Type': 'multipart/form-data',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }               
        }
   
        const pedido = await axios.put(ParametrageService.BASE_API_URL+"/order/" + id + "/state", data, {headers: config.header})
        console.log(pedido)
        return pedido;
    }
   
}
export default new OrderService();