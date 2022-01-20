<template>
<div class="container">
    <h1>Procesar Pedido</h1>
 
    <div class="row">

        <div class="col-4" style="background:white;">
            
            <h3>Tu Pedido</h3>
           
             <div class="elements-cart" v-for="list in carrito" :key="list.id">

                <div class="row">
                    <div class="col">  
                            {{list.product.name}}
                    </div>
                    <div class="col">
                        X{{list.quantity}}
                    </div>
                </div>
                <div class="row mt-2">
                    <div class="col">
                         {{list.product.price}}
                    </div>
                      
                </div>

   
            </div>  
            <h4 v-if="carrito.length!=0">Total {{total_carrito}} €</h4> 

        </div>

        

        <div class="col-4" style="background:white;">
            <h3>Dirección</h3>
            <input class="form-control" type="text" v-model="direccion">
            <h4>Código Postal</h4>
            <input class="form-control" type="number" value='0' v-model="codigo_postal">
            <h4>Provincia</h4>
            <input class="form-control" type="text" v-model="provincia">
        </div>

        <div class="col-4" style="background:white;">
             <h3>Numero de Tarjeta</h3>
            <input class="form-control" type="number" v-model="tarjeta.numero">
            <h4>Cvv</h4>
            <input class="form-control" type="number" value='0' v-model="tarjeta.cvv">
            <h4>Fecha de expiracion</h4>
            <input class="form-control" type="text" v-model="tarjeta.fecha_exp">
          
            <button v-on:click="procesar_pedido()" class="custom-input-file">Crear</button>
        </div>
         


    </div>
</div>
    
</template>

<script>

import CartService from '../services/CartService'
import OrderService from '../services/OrderService'
import axios from 'axios'
import TvpService from '../services/TvpService'
import ParametrageService from '../services/ParametrageService'
import Tarjeta from '../modules/tarjeta'

export default {
    name:'OrderCreate',
    data(){
        return{
            direccion:'',
            codigo_postal:'',
            provincia:'',
            carrito:null,
            total_carrito:0,
            tarjeta: new Tarjeta()
        }
    },
     async created(){
         //Hago que espere un segundo porque si no no carga el carrito
         let delay = (n) => new Promise( r => setTimeout(r, n*500));
            await delay(1);
         this.carrito=this.$store.state.user_carrito;
           this.total_carrito=CartService.totalCarrito(JSON.stringify(this.carrito))
    
    },
    computed:{
        datos_cart(){
            return this.$store.state.user_carrito;
        }
    },

    methods:{
          
    
        async procesar_pedido() {
          let data = new FormData();

          data.append('dirrecion',this.direccion)
          data.append('codigo_postal',this.codigo_postal)
          data.append('provincia',this.provincia)
          data.append('listproduct',this.carrito)
          data.append('total_prize',this.total_carrito)
          data.append('state', 'EN PROCESO')
    
          let config = 
          {
             header : {
            'Content-Type': 'multipart/form-data'
             }               
          }
   
          await axios.post(ParametrageService.BASE_API_URL + "/order/new/"+localStorage.getItem('user'), data, {headers: config.header}).then((resp) => {
              
              OrderService.addProductOrder(JSON.stringify(this.carrito),resp.data)
          
            });

            let datosTarjeta ={
                amount: 40.4,
                concept: "Compra SportsUa",
                reference: "UserID"+localStorage.getItem('user'),
                creditCard: {
                    "owner":   localStorage.getItem('user_name'),
                    "number": this.tarjeta.numero,
                    "ccv": this.tarjeta.cvv,
                    "expiry": this.tarjeta.fecha_exp
                }
            }

            console.log(datosTarjeta)
            TvpService.pagarPedido(datosTarjeta).then(console.log("Pagado"))
           this.$store.commit('carrito',[])
       

             // window.location.href = '/products'
             

      }
    }
    
}
</script>

<style scoped>

h4{
   
    margin-top: 5%;

}
.elements-cart{
    padding: 2%;
    margin-top: 10px;
}
.custom-input-file {

  box-shadow: inset 0px 0px 0px 0px rgb(245, 245, 245, 1);;
  margin-left: 47%;
  margin-top:2%;
  padding: 5px;
  text-align: center;
  height: 38px;
  width: 330px;
  border-radius: 3px;
  background: rgb(31, 70, 114);
  color: rgba(245,245,245,1);
  font-size: 18px;
  font-family: Roboto;
  font-weight: Regular;
    margin-top:10% ;
  left: 30%;
  cursor: pointer;
  transform: translate(-50%, -50%);
  transition: ease-out 0.4s;
  overflow: hidden;
}
.custom-input-file .input-file {
width: 330px;
 cursor: pointer;
 margin: 0;
 opacity: 0;
 outline: 0 none;
 padding: 0;
 position: absolute;
}


</style>