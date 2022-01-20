<template> 
<div class="list-body"> 
<h1 style="margin: 20px;"> Lista de Deseos</h1>
    <div class="row mt-5">

        <div class="col-md-4 col-xl-2" style="margin-left: 20px" v-for="(list,index) in listproductos" :key="list.id">
            
            <div class="card" > 
            <ToggleFavorite :fav=true :products="list" id="corazon"/>
                <img class="card-img-top" @click="infoProduct(list.id)" :src="imageUrl(list.link)" alt="Card image cap">         
                <div class="card-body" id="bottom-card" @click="quitarFav(list.id)"> 

                    <h5 class="card-title"> {{list.name}}</h5>
                    <p class="card-text">  <b> {{list.price}}€ </b></p>
                    <button  class="btn" @click="añadirCarrito(index, list.id)" ><img id="imagen-carrito" src="../assets/carrito.png"></button>
                </div>              
            </div>
        </div>
        
        
    </div>
   
</div>


</template>

<script>
import ProductService from '../services/ProductService'
import ToggleFavorite from './ToggleFavorite.vue'
import ParametrageService from '../services/ParametrageService'
import CartService from '../services/CartService'

export default {
    name:'Products',
    components: {
      ToggleFavorite
    },
    data(){
        return{
            listproductos:null,
        }
    },
    created(){   
        ProductService.getAllProductsByWishList().then(resp=> {
            console.log(resp);  
            this.listproductos=resp.data
        })
    },
    methods:{ 
        async añadirCarrito(index, id){
            console.log("Añadiendo"+id)
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
        }
    }
}
</script>

<style scoped lang="scss">

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
    height: 30vh;
    border-radius: 20px;
    border-top-right-radius: 50px;
}

#corazon {
    width: 10%;
    position: absolute;
    background-color: #ffffff00;
    border: #ffffff00;
}

.nav-categorias{
    background: rgb(4, 63, 141);
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
a {
  font-size: 1rem;
  padding: 0.5rem 1.5rem;
  color: #929292;
  text-transform: uppercase;
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

</style>