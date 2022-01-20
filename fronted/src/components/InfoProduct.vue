<template>
    <div class="container">
        <a  href="/products"> <b>Volver a Produtos</b> </a>
    <div class="row mt-5">
        <div class="col-md-6 col-sm-12" id="img_product">
            <ToggleFavorite :products="product" id="corazon"/>
            <img class="product-img" id="img_product" :src="imageUrl(product.link)" alt="">
        </div>
        <div class="col-md-6 col-sm-12">
            <div class="product-info" >
                <h1><b>{{product.name}}</b></h1>
                <h2><b>{{product.price}}€</b></h2>
                <h3><b>{{product.category.name}}</b></h3>
                <span>{{product.short_desc}}</span>
                <br>
                <button @click="añadir_carrito()" class="btn">Añadir al Carrito</button>
                
                <br>
                <b>Descripción</b>
                <br>
                <div class="product-longd_desc">{{product.long_desc}} </div>

            </div>
        </div>
       
    </div>

    </div>
</template>

<script>
import ProductService from '../services/ProductService'
import ToggleFavorite from './ToggleFavorite.vue'
import ParametrageService from '../services/ParametrageService'

export default {
    name:"InfoProduct",
    components: {
       ToggleFavorite,
    },
    data(){
        return{
            product:null
        }
    },
    created(){
        console.log("Entro")
        ProductService.getAllProductsById(this.$route.params.id).then(resp=>this.product=resp.data)

    },
    methods:{
        añadir_carrito(){
            this.$parent.$emit('carritoActualizado',this.product)
        },
        imageUrl(imageName){
            return ParametrageService.BASE_URL + '/user-images/' + imageName
        }  
    }
    
}
</script>

<style scoped>

.img_product {
    border-radius: 20px;
}

.product-longd_desc{
    height: 40%;
    overflow-y: scroll; 
    
}
.product-longd_desc::-webkit-scrollbar{
    display: none;
}

.product-info{
    background:white; padding: 5%;   
    font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;font-size: 20px;
    height: 600px;
    border-radius: 40px;
 
  
    
}
.product-img{
      width: 100%;
    height: 600px;
    border-radius: 40px;
}
.btn {
  text-decoration: none;
  border: 1px solid rgb(146, 148, 248);
  position: relative;
  overflow: hidden;
  width: 100%;
  margin-top: 10px;
  margin-bottom: 10px;
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

#corazon {
    width: 10%;
    position: absolute;
    background-color: #ffffff00;
    border: #ffffff00;
}

</style>