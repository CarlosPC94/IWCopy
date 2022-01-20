<template> 
<div>
    <div class="row mt-5" id="contenedor-images">
        <div class="slider-container">
        <div class="menu">
            <label for="slide-dot-1"></label>
            <label for="slide-dot-2"></label>
            <label for="slide-dot-3"></label>
        </div>
        <div id="titulo">
            <h1 id="comentario">Visita nuestro catálogo</h1>
            <button class="btn" h style="width: 10%" @click="pagina_products">Productos</button>
        </div>
        
        <input id="slide-dot-1" type="radio" name="slides" checked>
            <div class="slide slide-1"></div>
        
        <input id="slide-dot-2" type="radio" name="slides">
            <div class="slide slide-2"></div>
        
        <input id="slide-dot-3" type="radio" name="slides">
            <div class="slide slide-3"></div>
        </div>
    </div>
   
    <h1 style="margin: 20px; text-align: center;">Ofertas</h1>
    <div class="row mt-5" id="contenedor">

        <div class="col-md-4 col-xl-2" id="tarjeta" v-for="(list, index) in listproductos" :key="list.id">
            
            <div class="card" >
                <ToggleFavorite v-if="user && !isAdmin" :products="list" id="corazon"/> 
            
                <img class="card-img-top" @click="infoProduct(list.id)" :src="imageUrl(list.link)" alt="Card image cap">
                <div class="card-body" id="bottom-card"> 

                    <h5 class="card-title" @click="infoProduct(list.id)"> {{list.name}}</h5>
                    <p class="card-text">  <b style="color: red"> {{(list.price - list.discount).toFixed(2)}}€ </b></p>
                    
                    <button v-if="user && !isAdmin"  class="btn" @click="añadirCarrito(index, list.id)" ><img id="imagen-carrito" src="../assets/carrito.png"></button>
                
                </div>
            </div>
        </div> 
    </div>          
</div>


</template>

<script>
import ProductService from '../services/ProductService'
import CategoryService from '../services/CategoryService'
import ToggleFavorite from './ToggleFavorite.vue'
import CartService from '../services/CartService'
import ParametrageService from '../services/ParametrageService'
import { mapGetters } from 'vuex'

export default {
    name:'Home',
    components: {
       ToggleFavorite
    },
    data(){
        return{
            listproductos: null,
            listproductosfiltered: [],
            categorias: null,
            wishlistProducts: [],
            busqueda: null
        }
    },
    async created(){
        if(this.user){
            await ProductService.getAllProductsByWishList().then(resp=> {  
            this.wishlistProducts=resp.data
        })
        }
        await ProductService.getAllProducts().then(resp=> {
            this.listproductos=resp.data.filter((product) => product.discount != 0)
        })
        await CategoryService.getallCategories().then(resp=> {
            this.categorias = resp.data
        })
        this.busqueda = localStorage.getItem('busqueda')
        localStorage.removeItem('busqueda')
        this.buscar()
    },
    methods:{
       async  añadirCarrito(index,id){
             await CartService.addProduct(id).then(resp=>{
            this.$store.commit('carrito',resp.data)
            })
            this.$parent.$emit('carritoActualizado',this.listproductos[index])
          
        },
        infoProduct(index){
            this.$router.push({
                    name: "InfoProducts",
                    params:{
                        id: index,
                    }
                });
        },
        imageUrl(imageName){
            return ParametrageService.BASE_URL + '/user-images/' + imageName
        } ,
        pagina_products()
        {
          window.location.href = '/products'
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
  },  
}
</script>



<style scoped>

#comentario {
    color: white;
    -webkit-text-stroke: 1px black;
}

#titulo {
  position: absolute;
  text-align: left;
  z-index: 900;
  width: 100%;
}

#contenedor-images {
    width: 100%;
    height: 70vh;
    margin: 0;
    padding: 0;
    font-family: "Helvetica", sans-serif;
}

img {
  max-width: 100%;
}

.slider-container{
  height: 100%;
  width: 100%;
  position: relative;
  overflow: hidden; 
  text-align: center;
}

.menu {
  position: absolute;
  left: 0;
  z-index: 900;
  width: 100%;
  bottom: 0;
}

.menu label {
  cursor: pointer;
  display: inline-block;
  width: 16px;
  height: 16px;
  background: #fff;
  border-radius: 50px;
  margin: 0 .2em 1em;
  &:hover {
    background: red;
  }
}

.slide {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 100%;
  z-index: 10;
  padding: 8em 1em 0;
  background-size: cover;
  background-position: 50% 50%;
  transition: left 0s .75s;
}

[id^="slide"]:checked + .slide {
  left: 0;
  z-index: 100;
  transition: left .65s ease-out;
}

.slide-1 {
  background-image: url("https://www.scotsman.com/jpim-static/image/2021/02/25/08/shutterstock_1080117269.jpg?width=2048&enable=upscale");
}
.slide-2 {
  background-image: url("https://www.scotsman.com/jpim-static/image/2021/04/19/18/Best%20mens%20gym%20clothes%20for%20a%20comfortable%2C%20high%20performance%20workout.jpg?width=2048&enable=upscale");
}
.slide-3 {
  background-image: url("https://images.ctfassets.net/8cd2csgvqd3m/65yF1LK9OXVvFLWaUUSJvB/fa15f796fc9614d6f08568bc9962deec/E8Sport_olympics.png");
}

#contenedor {
    justify-content: center;
    align-items: left;
}

#tarjeta {
    margin: 20px;
}

#imagen-carrito {
    width: 50%;
}

#bottom-card {
    border-color: rgba(0, 0, 0, 0.295);
    background-color: #6423a141;
    border-radius: 20px;
    border-bottom-left-radius: 50px;
    border-top-right-radius: 50px;
    box-shadow: #929292;
    box-shadow: 2px 2px 10px #666;
    background-image: url("../assets/geo.jpg");
    color: white;
}

.a {
    cursor:pointer;
}

.card{
    border-radius: 20px;
    border-bottom-left-radius: 50px;
    border-top-right-radius: 50px;
    transition: box-shadow .5s;

}

.card:hover {
  box-shadow: 10px 2px 10px #666;
}

.card-img-top {
    border-radius: 20px;
    border-top-right-radius: 50px;
    height: 30vh;
}

#corazon {
    width: 10%;
    position: absolute;
    background-color: #ffffff00;
    border: #ffffff00;
}

.nav-categorias{
    background-image: url("../assets/geo.jpg");
}
.categorias{
    font-size: 20px;
    margin:50px;
    cursor: pointer;
   color:white
}
.categorias:hover{
    color:rgb(95, 162, 240)
}

.btn {
  text-decoration: none;
  border: 1px solid rgba(57, 29, 65, 0.473);
  position: relative;
  overflow: hidden;
  width: 30%;
  border-radius: 15px;
  border-bottom-left-radius: 50px;
  background-color: rgb(255, 255, 255);

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

/* ----- */
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
  right: 0;
}

.dropdown:hover .dropdown-content {
  display: block;
}
</style>