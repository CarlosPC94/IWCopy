<template>
   
<div class="container d-lg-flex">
    <div class="box-1 bg-light user">
        <div class="d-flex align-items-center mb-3"> <img src="https://images.pexels.com/photos/4925916/pexels-photo-4925916.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" class="pic rounded-circle" alt="">
            <p class="ps-2 name">{{ nombre_user}}</p>
        </div>

        <div class="box-inner-1 pb-3 mb-3 ">
            <div class="d-flex justify-content-between mb-3 userdetails">
                <p class="fw-bold">Tus Productos</p>
                <p class="fw-lighter"><span class="fas fa-dollar-sign"></span>{{total_carrito}}€</p>
            </div>
            <div id="my" class="carousel slide carousel-fade img-details " data-bs-ride="carousel" data-bs-interval="2000">
                <div class="carousel-indicators"> <button type="button" data-bs-target="#my" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button> <button type="button" data-bs-target="#my" data-bs-slide-to="1" aria-label="Slide 2"></button> <button type="button" data-bs-target="#my" data-bs-slide-to="2" aria-label="Slide 3"></button> </div>
                <div class="carousel-inner">
                    <div class="carousel-item active"> <img :src="imageUrl(carrito[0].product.link)"  class="d-block w-100"> </div>
                    <div class="carousel-item" v-for="(list,index) in carrito" :key="index"  > <img  :src="imageUrl(list.product.link)" class="d-block w-100 h-100"> </div>
                </div> <button class="carousel-control-prev" type="button" data-bs-target="#my" data-bs-slide="prev">
                    <div class="icon"> <span class="fas fa-arrow-left"></span> </div> <span class="visually-hidden">Previous</span>
                </button> <button class="carousel-control-next" type="button" data-bs-target="#my" data-bs-slide="next">
                    <div class="icon"> <span class="fas fa-arrow-right"></span> </div> <span class="visually-hidden">Next</span>
                </button>
            </div>
            <p class="dis info my-3">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Cupiditate quos ipsa sed officiis odio </p>
            <div class="radiobtn">  <label for="one" class="box py-2 first"  @click="infoProduct(list.product.id)"   v-for="list in carrito" :key="list.product.id" >
                    <div class="d-flex align-items-start mb-2" > 
                        <div class="course">
                            <div class="d-flex align-items-center justify-content-between mb-2"> <span class="fw-bold">{{list.product.name}} </span> <span class="fas fa-dollar-sign">{{list.product.price}}€</span> </div> <span> Cantidad x {{list.quantity}}  </span>
                        </div>
                     
                    </div>
                </label> 
            </div>
        </div>
    </div>
    <div class="box-2">
        <div class="box-inner-2">
            <div>
                <p class="fw-bold">Detalles de Pago</p>
                <p class="dis mb-3">Completa los datos de su compra para poder procesar el pedido</p>
            </div>
            <form action="">
                <div>
                    <p class="dis fw-bold" style="color:red;" v-if="mensajesError!=''">{{mensajesError}}</p>
                    <p class="dis fw-bold mb-2">Datos Tarjeta</p>
                    <div class="d-flex align-items-center justify-content-between card-atm border rounded">
                        <div class="fab fa-cc-visa ps-3"></div> <input type="number" class="form-control" placeholder="Nùmero Tarjeta" v-model="tarjeta.numero">
                        <div class="d-flex w-50"> <input type="text" class="form-control px-0" placeholder="MM/YYYY "  v-model="tarjeta.fecha_exp"> <input type="password" maxlength=3 class="form-control px-0" placeholder=" CVV" v-model="tarjeta.cvv"> </div>
                    </div>
                    <div class="my-3 cardname">
                        <p class="dis fw-bold mb-2">Titular de Tarjeta</p> <input class="form-control" type="text" placeholder="Nombre y Apellidos" v-model="titutal_tarjeta">
                    </div>
                    <div class="address">
                        <p class="dis fw-bold mb-3">Dirreción de envio</p> 
                           <input class="form-control" type="text"  placeholder="Calle .."  v-model="direccion">

                        <div class="d-flex mb-2"> <input class="form-control zip" type="number" placeholder="Código Postal" v-model="codigo_postal" > <input class="form-control state" type="text" placeholder="Provincia" v-model="provincia"> </div>
                      
                        <div class="d-flex flex-column dis">
                             <div class="d-flex align-items-center justify-content-between mb-2">
                                <p>Decuento Aplicado</p>
                                <p><span class="fas fa-dollar-sign"></span>{{descuento}}€</p>
                            </div>
                            <div class="d-flex align-items-center justify-content-between mb-2">
                                <p>Subtotal</p>
                                <p><span class="fas fa-dollar-sign"></span>{{aux}}€</p>
                            </div>
                            <div class="d-flex align-items-center justify-content-between mb-2">
                                <p class="fw-bold">Total</p>
                                <p class="fw-bold"><span class="fas fa-dollar-sign"></span>{{total_carrito}}€</p>
                            </div>
                            <div class="btn btn-primary mt-2" v-on:click="procesar_pedido()" >Procesar Pago<span class="fas fa-dollar-sign px-1"></span>{{total_carrito}}€ </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    
</div>

    </div>
</template>

<script>

import CartService from '../services/CartService'
import OrderService from '../services/OrderService'
import axios from 'axios'
import TvpService from '../services/TvpService'
import Tarjeta from '../modules/tarjeta'
import ParametrageService from '../services/ParametrageService'

export default {
    name:'ProcesarPedido',
    data(){
        return{
            direccion:'',
            codigo_postal:'',
            provincia:'',
            carrito:null,
            estado_pedido:'',
            total_carrito:0,
            tarjeta: new Tarjeta('','',''),
            titutal_tarjeta:"",
            nombre_user:"",
            mensajesError:'',
            descuento:0,
            aux:0
        }
    },
     async created(){
         //Hago que espere un segundo porque si no no carga el carrito
         let delay = (n) => new Promise( r => setTimeout(r, n*500));
            await delay(1);
         this.carrito=this.$store.state.user_carrito;
         this.nombre_user=  localStorage.getItem('user_name');
        this.total_carrito=CartService.totalCarrito(JSON.stringify(this.carrito))
        this.aux=this.total_carrito
        this.descuento= localStorage.getItem("descuento",this.descuento);
        this.total_carrito=this.total_carrito-this.descuento;
        
    
    },
    computed:{
        datos_cart(){
            return this.$store.state.user_carrito;
        }
    },

    methods:{
         validar(){

             if(this.tarjeta.numero=="" ||  this.tarjeta.cvv=="" ||this.tarjeta.fecha_exp=="")
             {
                  this.mensajesError="Los datos de la tarjeta no pueden estar en blanco"
                  return false
             }
             else{
                   var numero =this.tarjeta.numero.toString()
                    var cvv=this.tarjeta.cvv.toString();
                    var fecha =this.tarjeta.fecha_exp.toString()
             }

             if(numero.length<4){
        
                   this.mensajesError="El número de tarjeta no es válido"
                   return false
             }
               if(cvv.length!=3){
                   this.mensajesError="El CVV debe tener  tres cifras"
                   return false
             }
                  
            if(!fecha.includes("/") ||  !this.tarjeta.fecha_exp.includes("20"))
            {
                this.mensajesError="El formato de la fecha de expiración no es correcto"
                return false
            }
             this.mensajesError='';
             return true;
         },
     infoProduct(index){
            this.$router.push({
                    name: "InfoProducts",
                    params:{
                        id: index,
                    }
                })},
        async procesar_pedido() {
        if(this.validar())
        {
          let data = new FormData();

          data.append('dirrecion',this.direccion)
          data.append('codigo_postal',this.codigo_postal)
          data.append('provincia',this.provincia)
          data.append('listproduct',this.carrito)
          data.append('total_prize',this.total_carrito)
    
          let config = 
          {
             header : {
            'Content-Type': 'multipart/form-data',
            'Authorization': 'Bearer ' + localStorage.getItem('token')
             }               
          }

           let datosTarjeta ={
                amount: 40.4,
                concept: "Compra SportsUa",
                reference: "UserID"+localStorage.getItem('user'),
                creditCard: {
                    "owner":  this.titutal_tarjeta,
                    "number": this.tarjeta.numero,
                    "ccv": this.tarjeta.cvv,
                    "expiry": this.tarjeta.fecha_exp
                }
                }

            console.log(datosTarjeta)

            await TvpService.pagarPedido(datosTarjeta).then(()=>{
             console.log("PEDIDO PAGADO CORRECTAMENTE");
             this.estado_pedido="PAGADO"
            }).catch(()=>{
                console.log("PEDIDO RECHAZADP");
             this.estado_pedido="RECHAZADO"
            })

            data.append('pedido_estado',this.estado_pedido)
   
            await axios.post(ParametrageService.BASE_API_URL + "/order/new/"+localStorage.getItem('user'), data, {headers: config.header}).then((resp) => {
              
              OrderService.addProductOrder(JSON.stringify(this.carrito),resp.data)
          
            });

           
            this.$store.commit('carrito',[])
       

             window.location.href = '/products'
        }
             

      },
        imageUrl(imageName){
        return ParametrageService.BASE_URL + '/user-images/' + imageName
        } ,
        
    }
    
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Montserrat:400,700&display=swap');
#alert-tarjeta-1{
    position: absolute;
    margin-left: 30%;
}
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    list-style: none;
    font-family: 'Montserrat', sans-serif
}

body {
    padding: 5px;
    background-color: #3c3049
}

p {
    margin: 0%
}

.container {
    max-width: 900px;
    margin: 20px auto;
    overflow: hidden;
    background-color: #f8f9fa
}

.box-1 {
    max-width: 450px;
    padding: 10px 40px;
    user-select: none
}

.box-1 div .fs-12 {
    font-size: 8px;
    color: white
}

.box-1 div .fs-14 {
    font-size: 15px;
    color: white
}

.box-1 img.pic {
    width: 20px;
    height: 20px;
    object-fit: cover
}

.box-1 img.mobile-pic {
    width: 100%;
    height: 200px;
    object-fit: cover
}

.box-1 .name {
    font-size: 11px;
    font-weight: 600
}

.dis {
    font-size: 12px;
    font-weight: 500
}

label.box {
    width: 100%;
    font-size: 12px;
    background: #ddd;
    margin-top: 12px;
    padding: 10px 12px;
    border-radius: 5px;
    cursor: pointer;
    border: 1px solid transparent
}

#one:checked~label.first,
#two:checked~label.second,
#three:checked~label.third {
    border-color: #7700ff
}

#one:checked~label.first .circle,
#two:checked~label.second .circle,
#three:checked~label.third .circle {
    border-color: #7a34ca;
    background-color: #fff
}

label.box .course {
    width: 100%
}

label.box .circle {
    height: 12px;
    width: 12px;
    background: #ccc;
    border-radius: 50%;
    margin-right: 15px;
    border: 4px solid transparent;
    display: inline-block
}

input[type="radio"] {
    display: none
}

.box-2 {
    max-width: 450px;
    padding: 10px 40px
}

.box-2 .box-inner-2 input.form-control {
    font-size: 12px;
    font-weight: 600
}

.box-2 .box-inner-2 .inputWithIcon {
    position: relative
}

.box-2 .box-inner-2 .inputWithIcon span {
    position: absolute;
    left: 15px;
    top: 8px
}

.box-2 .box-inner-2 .inputWithcheck {
    position: relative
}

.box-2 .box-inner-2 .inputWithcheck span {
    position: absolute;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: green;
    font-size: 12px;
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    right: 15px;
    top: 6px
}

.form-control:focus,
.form-select:focus {
    box-shadow: none;
    outline: none;
    border: 1px solid #7700ff
}

.border:focus-within {
    border: 1px solid #7700ff !important
}

.box-2 .card-atm .form-control {
    border: none;
    box-shadow: none
}

.form-select {
    border-radius: 0;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px
}

.address .form-control.zip {
    border-radius: 0;
    border-bottom-left-radius: 10px
}

.address .form-control.state {
    border-radius: 0;
    border-bottom-right-radius: 10px
}

.box-2 .box-inner-2 .btn.btn-outline-primary {
    width: 120px;
    padding: 10px;
    font-size: 11px;
    padding: 0% !important;
    display: flex;
    align-items: center;
    border: none;
    border-radius: 0;
    background-color: whitesmoke;
    color: black;
    font-weight: 600
}

.box-2 .box-inner-2 .btn.btn-primary {
    background-color: #18161a;
    color: whitesmoke;
    font-size: 14px;
    display: flex;
    align-items: center;
    font-weight: 600;
    justify-content: center;
    border: none;
    padding: 10px
}

.box-2 .box-inner-2 .btn.btn-primary:hover {
    background-color: #403849
}

.box-2 .box-inner-2 .btn.btn-primary .fas {
    font-size: 13px !important;
    color: whitesmoke
}

.carousel-indicators [data-bs-target] {
    width: 10px;
    height: 10px;
    border-radius: 50%
}

.carousel-inner {
    width: 100%;
    height: 250px
}

.carousel-item img {
    object-fit: cover;
    height: 100%
}

.carousel-control-prev {
    transform: translateX(-50%);
    opacity: 1
}

.carousel-control-prev:hover .fas.fa-arrow-left {
    transform: translateX(-5px)
}

.carousel-control-next {
    transform: translateX(50%);
    opacity: 1
}

.carousel-control-next:hover .fas.fa-arrow-right {
    transform: translateX(5px)
}

.fas.fa-arrow-left,
.fas.fa-arrow-right {
    font-size: 0.8rem;
    transition: all .2s ease
}

.icon {
    width: 30px;
    height: 30px;
    background-color: #f8f9fa;
    color: black;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transform-origin: center;
    opacity: 1
}

.fas,
.fab {
    color: #6d6c6d
}

::placeholder {
    font-size: 12px
}

@media (max-width:768px) {
    .container {
        max-width: 700px;
        margin: 10px auto
    }

    .box-1,
    .box-2 {
        max-width: 600px;
        padding: 20px 90px;
        margin: 20px auto
    }
}

@media (max-width:426px) {

    .box-1,
    .box-2 {
        max-width: 400px;
        padding: 20px 10px
    }

    ::placeholder {
        font-size: 9px
    }
}
</style>