import axios from 'axios'

class TpvService{
    url ="https://tpvviw.tk/api/v1/auth/"
    //Obtiene el token de nuestro comercio
    tokenComercio(){
       return axios.post(this.url+'authtoken',{apiKey: "cdbdd719-4e68-4214-b9cd-f2ca6d2461de"})
    }

    pagarPedido(datoPago){


        let config = {
            headers: {
              'Authorization': 'Bearer ' + localStorage.getItem('token_comercio')
            }
          }
        
       return axios.post("https://tpvviw.tk/api/v1/payments/",datoPago,config)
        
    }

}

export default new TpvService();