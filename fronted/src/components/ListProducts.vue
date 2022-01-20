<template> 
<div>
    <div class="nav-categorias">
        <div class="dropdown" v-for="categoria in categorias" :key="categoria.id">
            <span
            @click="filtrarCategoria(categoria.id)"
            class="categorias ">
                {{ categoria.name }}
            </span>
            <div class="dropdown-content dropdown-menu dropdown-menu-lg-end">
                <a v-for="subcategoria in categoria.subcategories" :key="subcategoria.id" 
                class="dropdown-item" style="cursor:pointer;"
                @click="filtrarSubcategoria(subcategoria.id)">
                    {{ subcategoria.name }}
                </a>
            </div>
        </div>
    </div>
   
    <h1 style="margin: 20px;">Productos</h1>
    <div class="row mt-5" id="contenedor" v-if="busqueda == null || busqueda == ''">

        <div class="col-md-4 col-xl-2" id="tarjeta" v-for="(list, index) in listproductos" :key="list.id">
            
            <div class="card" >
                <ToggleFavorite v-if="user && !isAdmin" :products="list" id="corazon"/> 
            
                <img class="card-img-top" @click="infoProduct(list.id)" :src="imageUrl(list.link)" alt="Card image cap">
                <div class="card-body" id="bottom-card"> 

                    <h5 class="card-title" @click="infoProduct(list.id)"> {{list.name}}</h5>
                    <p class="card-text">  <b> {{list.price}}€ </b></p>
                    
                    <button v-if="user && !isAdmin"  class="btn" @click="añadirCarrito(index, list.id)" ><img id="imagen-carrito" src="../assets/carrito.png"></button>
                
                </div>
            </div>
        </div>           
    </div>
    <div class="row mt-5" v-if="busqueda != null && busqueda != ''">

        <div class="col-md-4 col-xl-2" style="margin-left: 20px" v-for="(list,index) in listproductosfiltered" :key="list.id">
            
            <div class="card">
                <ToggleFavorite v-if="user && !isAdmin" :products="list" id="corazon"/> 
            
                <img class="card-img-top" @click="infoProduct(list.id)" :src="imageUrl(list.imageLink)" alt="Card image cap">
                <div class="card-body" id="bottom-card"> 

                    <h5 class="card-title" @click="infoProduct(list.id)"> {{list.name}}</h5>
                    <p class="card-text">  <b> {{list.price}}€ </b></p>
                    
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
import ParametrageService from '../services/ParametrageService'
import ToggleFavorite from './ToggleFavorite.vue'
import CartService from '../services/CartService'
import { mapGetters } from 'vuex'

export default {
    name:'Products',
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
            this.listproductos=resp.data
        })
        await CategoryService.getallCategories().then(resp=> {
            this.categorias = resp.data
        })
        this.busqueda = localStorage.getItem('busqueda')
        localStorage.removeItem('busqueda')
        this.buscar()
    },
    methods:{
        async filtrarCategoria(num){
            await ProductService.getAllProductsByCategory(num).then(resp=> {
                this.listproductos=resp.data
                this.buscar()
            })
        },
        async filtrarSubcategoria(num){
            await ProductService.getAllProductsBySubcategory(num).then(resp=> {
                this.listproductos=resp.data
                this.buscar()
            })
        },
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
        buscar(){
            this.listproductosfiltered = [];
            if(this.busqueda != null && this.busqueda != '') {
                for(var i = 0; i < this.listproductos.length; i++){
                    if(this.listproductos[i].name.includes(this.busqueda)){
                        console.log(this.listproductos[i].name)
                        this.listproductosfiltered.push(this.listproductos[i])
                    }
                }
            } 
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
  },  
}
</script>



<style scoped>

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