<template>
<div class="container emp-profile">
                <div class="row">
                    <div class="col-md-4">
                    </div>
                    <div class="col-md-6">
                        <div class="profile-head">
                                    <h2>
                                        Identificador de pedido: {{pedido.id}}
                                    </h2>
                                    <h3>
                                        Fecha de pedido: {{pedido.date.split("T")[0]}}
                                    </h3>
                                    <h6>
                                        Estado: {{pedido.state}}
                                    </h6>
                                    <br>                       
                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                <li class="nav-item">
                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Detalles del pedido</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4">
                        <div class="profile-work">
                            <p>Need Help?</p>
                            <a href="">Contact us</a><br/>
                            <a href="" style="color: red;">Procesar Devolución</a><br/>                          
                        </div>
                    </div>
                    <div class="col-md-8">
                        <div class="tab-content profile-tab" id="myTabContent">
                            <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                <table class="table">
                                    <thead>
                                        <tr>
                                        <th scope="col">ID</th>
                                        <th scope="col">Producto</th>
                                        <th scope="col">Imagen</th>
                                        <th scope="col">Cantidad</th>
                                        <th scope="col">Precio</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="(producto) in productos" :key="producto.product.id">
                                        <th scope="row">{{producto.product.id}}</th>
                                        <td>{{producto.product.name}}</td>
                                        <td>{{producto.product.image}}</td>
                                        <td>{{producto.quantity}}</td>
                                        <td>{{producto.product.price}} €</td>
                                        </tr>
                                        <tr v-if="descuento!=0" style="color:red;"> 
                                            Descuento Aplicado {{descuento}} €
                                        </tr>                                   
                                    </tbody>
                                </table>
                                <h3>Precio total: {{pedido.totalPrice}} €</h3>       
                            </div>
                        </div>
                    </div>
                </div>      
        </div>
</template>

<script>
import OrderService from "../services/OrderService"

export default {
    name: "OrderInfo",
    data () {
        return {
            pedido: null,
            productos: [],
            descuento:0
        }
    },
    async created(){  
        this.pedido = await OrderService.getPedido(this.$route.params.id)
        this.productos = this.pedido.productsInOrders
        console.log(this.productos);
         this.productos.forEach(element=>{
            this.descuento =this.descuento+element.product.discount*element.quantity
        })
    }
}
</script>

<style scoped>

body{
    background: -webkit-linear-gradient(left, #3931af, #00c6ff);
}
.emp-profile{
    padding: 3%;
    margin-top: 3%;
    margin-bottom: 3%;
    border-radius: 0.5rem;
    background: #fff;
}
.profile-img{
    text-align: center;
}
.profile-img img{
    width: 70%;
    height: 100%;
}
.profile-img .file {
    position: relative;
    overflow: hidden;
    margin-top: -20%;
    width: 70%;
    border: none;
    border-radius: 0;
    font-size: 15px;
    background: #212529b8;
}
.profile-img .file input {
    position: absolute;
    opacity: 0;
    right: 0;
    top: 0;
}
.profile-head h5{
    color: #333;
}
.profile-head h6{
    color: #0062cc;
}
.profile-edit-btn{
    border: none;
    border-radius: 1.5rem;
    width: 70%;
    padding: 2%;
    font-weight: 600;
    color: #6c757d;
    cursor: pointer;
}
.proile-rating{
    font-size: 12px;
    color: #818182;
    margin-top: 5%;
}
.proile-rating span{
    color: #495057;
    font-size: 15px;
    font-weight: 600;
}
.profile-head .nav-tabs{
    margin-bottom:5%;
}
.profile-head .nav-tabs .nav-link{
    font-weight:600;
    border: none;
}
.profile-head .nav-tabs .nav-link.active{
    border: none;
    border-bottom:2px solid #0062cc;
}
.profile-work{
    padding: 14%;
    margin-top: -15%;
}
.profile-work p{
    font-size: 12px;
    color: #818182;
    font-weight: 600;
    margin-top: 10%;
}
.profile-work a{
    text-decoration: none;
    color: #495057;
    font-weight: 600;
    font-size: 14px;
}
.profile-work ul{
    list-style: none;
}
.profile-tab label{
    font-weight: 600;
}
.profile-tab p{
    font-weight: 600;
    color: #0062cc;
}

a {
  font-size: 1rem;
  padding: 0.5rem 1.5rem;
  color: #929292;
  text-transform: uppercase;
}

.btn {
  text-decoration: none;
  border: 1px solid rgb(146, 148, 248);
  position: relative;
  overflow: hidden;
}

.btn:hover {
  box-shadow: 1px 1px 25px 10px rgba(146, 148, 248, 0.4);
}

.btn:before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    120deg,
    transparent,
    rgba(146, 148, 248, 0.4),
    transparent
  );
  transition: all 650ms;
}

.btn:hover:before {
  left: 100%;
}


</style>