<template>

<div class="div">
    <nav id="navbar" class="navbar navbar-expand-lg navbar-dark">
    
        <button
        class="navbar-toggler"
        type="button"
        data-mdb-toggle="collapse"
        data-mdb-target="#navbarButtonsExample"
        aria-controls="navbarButtonsExample"
        aria-expanded="false"
        aria-label="Toggle navigation"
        >
        <i class="fas fa-bars"></i>
        </button>
        <div class="collapse navbar-collapse" id="navbarButtonsExample" style="margin-left: 25px">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <a class="navbar-brand" href="/">
            <img src="../assets/favicon.png" width="35" height="35" class="d-inline-block align-top" alt="">
              SportUA
          </a>
        </ul>
        <input id="search-bar" type="search" placeholder="Buscar ..." aria-label="Search" v-model="busqueda">
        <button class="btn" style="margin-right: 70vh;"  @click="buscar(busqueda)"><img src="../assets/lupa.png" width="23" height="23"></button> 
        <div class="d-flex align-items-center">
          <a v-if="user && !isAdmin" class="btn btn-secondary"  @click="mostrar_carrito()  " role="button" style="padding: 0; background: transparent; border: transparent; margin-right: 20px; margin-top: 2px"><img src="../assets/carro.png" width="40" height="40"></a>
          <div class="cart" id="cart" v-if="carrito_estado" >
            <table class="table" style="color:white; background:black;">
              <thead >
                <tr>
                  <th>Cantidad</th>
                  <th>Imagen</th>
                  <th>Producto</th>
                  <th>Precio</th>
                </tr>
              </thead>
              <tbody>
                <tr  class="fila_carrito" v-for="(list,index) in carrito" :key="index" >
                  <td>{{list.quantity}}</td>  
                  <td><img class="img-carrito" :src="imageUrl(list.product.link)" alt=""></td>
                  <td>{{list.product.name}}</td>
                  <td>{{list.product.price}}€</td>
                  <td @click="quitarProductoCarrito(list.product.price*list.quantity,list.product.discount*list.quantity,index,list.product.id)" class="x_quitarProducto"> <div style="margin-top:30px;"> X</div></td>
                </tr>
                
              </tbody>
            </table>
            <h6 v-if="carrito.length>0 && descuento!=0">Descuento Aplicado {{descuento}}€</h6>
            <h6 v-if="carrito.length>0">Sub Total {{total_carrito}}€</h6>
            <p v-if="carrito.length>0"  class="total_carrito"> Total {{total_carrito-descuento}}€

              <button id="procesar" @click="procesar_pedido" class="btn btn-dark ">Procesar Pedido</button>
            </p>


          </div>
          </div>
          <div class="btn-group">
            <button type="button" id="menu-button" class="btn btn-secondary dropdown-toggle" style="margin-right: 25px; padding: 0" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="false">
              <img src="../assets/menu.png" width="25" height="25">
            </button>
            <ul class="dropdown-menu dropdown-menu-lg-end">
              <a v-if="!isAdmin" class="dropdown-item" role="button" href="/products">Productos</a>
              <a v-if="isAdmin" class="dropdown-item" role="button" href="/admin/products">Productos</a>
              <a v-if="!user" class="dropdown-item" role="button" href="/login">Login</a>
              <a v-if="!user" class="dropdown-item" role="button" href="/register" >Sign up</a>
              <a v-if="user && !isAdmin" class="dropdown-item" href="/wishList" role="button">Favoritos</a>
              <a v-if="user && !isAdmin" class="dropdown-item" href="/myOrders" role="button">Pedidos</a>
              <a v-if="isAdmin" class="dropdown-item" role="button" href="/admin/orders">Pedidos</a>
              <a v-if="isAdmin" class="dropdown-item" role="button" href="/admin/categories">Categorias</a>
              <a v-if="isAdmin" class="dropdown-item" role="button" href="/users">Usuarios</a>
              <a v-if="user" class="dropdown-item" href="/profile" role="button">Perfil</a>
              <a v-if="user" @click="handleLogout()" class="dropdown-item" role="button" style="color: red">Log Out</a>
            </ul>
      </div>
    </div>
  </nav>
    <!-- <NavbarSecundario
      v-if="isAdmin"
      :menuItems="adminMenu"
      :clickAction="goTo"/> -->
    <router-view> </router-view>
</div>
</template>

<script>


import CartService from "../services/CartService"

import axios from 'axios'
import authHeader from '../helpers/authHeader.js'
import ParametrageService from '../services/ParametrageService'
import { mapGetters } from 'vuex'
/* import NavbarSecundario from './NavbarSecundario.vue' */

export default {

  name: "NavBar",
  data() {
      return {
        info: "mensaje",
        carrito_estado:false,
        carrito:[],
        total_carrito:0,
        descuento:0,
        busqueda:'',
        /* adminMenu: [
          {
            itemName: 'Categorias',
            itemURL: '/admin/categories'
          },
          {
            itemName: 'Productos',
            itemURL: '/admin/products'
          },
          {
            itemName: 'Pedidos',
            itemURL: '/admin/orders'
          }
        ] */
      }
    },
     async created(){
      try {
        const response = await axios.get(ParametrageService.BASE_API_URL + '/users/perfil/' + localStorage.getItem('user'), { headers: authHeader() })
        this.$store.dispatch('user', response.data)
   
        this.carrito=this.$store.state.user_carrito;

        this.carrito.forEach(element=>{
            this.total_carrito= Math.round((this.total_carrito+(element.product.price*element.quantity)) * 100) / 100;
            this.descuento=Math.round((this.descuento+element.product.discount) * 100) / 100;
          })
         localStorage.setItem("descuento",this.descuento)


      } catch (e) {
      // No estamos logeados
      }
      //Controla el evento del carrito
        this.$on('carritoActualizado',async  (data) => {      
         await axios.get(ParametrageService.BASE_API_URL + "/users/cart/"+localStorage.getItem('user')).then((resp)=>{
           console.log("REsp"+JSON.stringify(resp.data))
                        this.carrito=resp.data
              })

            this.total_carrito= Math.round((this.total_carrito+data.price) * 100) / 100;
            this.descuento=Math.round((this.descuento+data.discount) * 100) / 100;
            localStorage.setItem("descuento",this.descuento)
            

          })

          /*if(this.carrito.includes(data))
            {
              var aux = this.carrito.splice(this.carrito.findIndex(element=>element==data),1)
  
              this.total_carrito= Math.round((this.total_carrito+data.prize) * 100) / 100;

            }
            else{
              data.carrito_cantidad=1;
               this.total_carrito= Math.round((this.total_carrito+data.prize) * 100) / 100;
               this.carrito.push(data);
            }*/
        
    },
    methods:{
      mostrar_carrito(){
        if(this.carrito_estado!=true)
          this.carrito_estado=true;
        else
          this.carrito_estado=false;
      },
      quitarProductoCarrito(price,des,index,id){
        console.log("Hola"+price)
          this.total_carrito=Math.round((this.total_carrito-price) * 100) / 100;
          this.descuento=Math.round((this.descuento-des) * 100) / 100;
          this.carrito.splice(index,1);
          CartService.deleteProducts(id).then(resp=>{
          this.$store.commit('carrito',resp.data)

        })
       
      },

      procesar_pedido()
      {
      const bodyFormData = new FormData();
      this.carrito_estado=false;
       this.carrito.forEach((item) => {

           bodyFormData.append('producto[]', item);
        });
        
         axios.post(ParametrageService.BASE_API_URL + "/users/cart/new/"+2,bodyFormData)
          this.$store.commit('carrito',this.carrito)
         
            this.$router.push({
                    name: "ProcesarPedido"
                });
      },

      handleLogout(){
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        localStorage.removeItem('wishlist')
        this.$store.dispatch('user', null)
        this.$store.commit('carrito',[])
        window.location.href = '/login'
          
     
      },
      goTo(url){
        this.$router.push(url)
      },
      buscar(busqueda) {
        localStorage.setItem('busqueda', busqueda)
        console.log(busqueda)
        window.location.href = '/products'
      },
       imageUrl(imageName){
        return ParametrageService.BASE_URL + '/user-images/' + imageName
        }  
    },
    computed: {
    // maps store getters to local computed properties
    // Nos permite usar user como si fuera uno de nuestros datos
    ...mapGetters(['user']),
    isAdmin(){
        console.log('check if admin')
        if(this.$store.state.user){
          const adminRole = this.$store.state.user.groups.filter( group => group.name === 'ADMIN' )
          return adminRole.length > 0
        }
        return false
    }
  },/* 
  components: {
    NavbarSecundario
  } */
}
</script>

<style scoped>

#search-bar {
  width: 60%;
  border-radius: 5px;
  border: transparent;
}

#navbar {
  background-color: black;
  height: 5%;
}

#menu-button {
  background-color: transparent;
  border: transparent;
}

.dropdown-toggle::after {
    display:none;
}

.img-carrito{
  height: 100%;
  width: 100%;
}
.total_carrito{
  font-size: 25px;
  text-align: center;

}
.fila_carrito:hover{
  background:  rgb(39, 36, 36);
  color: white;

}
.x_quitarProducto{
  cursor: pointer;
  text-align: center;
  width: 20%;
}

.cart{
  position: absolute;
  right: 100px;
  width: 450px;
  height: 200px;
  top:100%;
  background: white;
  border-radius: 2%;
  overflow-y: scroll;
  z-index: 3;
}
.cart::-webkit-scrollbar{

}
.div{
      width: 100%;
    height: 100%;
}

.bg-image {

  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;

}
/* dropdown a ver si va*/
.dropbtn {
  background-color: #333;
  color: white;
  padding: 14px 16px;
  font-size: 17px;
  border: none;
  cursor: pointer;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  left: 0;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content a:hover {background-color: #f1f1f1}

.dropdown:hover .dropdown-content {
  display: block;
}

.dropdown:hover .dropbtn {
  background-color: #ddd;
  color: black;
}
h6{
  width: 80%;
  margin-left:10%;
 background: rgba(155, 155, 167, 0.247);
 padding: 10px;
 border-radius: 3%;
}
#procesar:hover{
  background: rgba(55, 55, 61, 0.932);
}
</style>