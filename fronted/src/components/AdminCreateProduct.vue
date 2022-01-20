<template>
<div v-if="isAdmin" class="container">
    <h1/>
    <div class="row" style="background:white; margin-top: 15px; border-radius: 20px;">
        <h1>Añadir Producto</h1>
        <div>
            <button v-on:click="register()" class="btn">Crear</button>
        </div>
        <div class="col-4" style="background:white; border-radius: 20px;">
            
            <h4>Nombre</h4>
            <input class="form-control" type="text" v-model="new_product.name">
            <p v-if="mensajesError.nombre" class="rojo">{{ mensajesError.nombre }}</p>

            <h4>Descuento</h4>
            <input class="form-control" type="number" value='0' v-model="new_product.discount">

            <h4>Categoria</h4>
            <select class="form-control" id="select-option" v-model="new_product.category">
                <option  v-for="category in listcategorias" :key="category.id" :value="category"> 
                    {{category.name}}
                </option>
            </select>
            <p v-if="mensajesError.categoria" class="rojo">{{ mensajesError.categoria }}</p>

            <h4>Descripción breve</h4>
            <textarea class="form-control" name="short_desc" id="" cols="30" rows="2" v-model="new_product.short_desc"></textarea>
         
        </div>
        <div class="col-4" style="background:white; border-radius: 20px;">
            <h4>Precio</h4>
            <input class="form-control" type="number" v-model="new_product.price">
            <p v-if="mensajesError.precio" class="rojo">{{ mensajesError.precio }}</p>

             <h4>Cantidad</h4>
            <input class="form-control" type="number" v-model="new_product.quantity">
            <p v-if="mensajesError.cantidad" class="rojo">{{ mensajesError.cantidad }}</p>

            <h4>SubCategoria</h4>
            <select :disabled="!subcategories" class="form-control" id="select-option" v-model="new_product.subcategory">
                <option  v-for="subcategory in subcategories" :key="subcategory.id" :value="subcategory"> 
                    {{subcategory.name}}
                </option>
            </select>
            <p v-if="mensajesError.subcategoria" class="rojo">{{ mensajesError.subcategoria }}</p>
        
        <h4>Descripción larga</h4>
        <textarea  class="form-control" name="short_desc" id="" cols="30" rows="10" v-model="new_product.long_desc"></textarea>
        </div>

        <div class="col-4" style="background:white; border-radius: 20px;">

            <!-- <h4>Disponibilidad</h4> -->

            <h4>Imagen</h4>
            <div class="form-group" >
                <input type="file" class="form-control-file"  id="imagen"  accept="image/*" value="" @change="cargarImg($event)">
                Subir Imagen    
            </div>
            <p v-if="mensajesError.imagen" class="rojo">{{ mensajesError.imagen }}</p>
        </div>
    </div>
</div>
    
</template>

<script>
import CategoryService from '../services/CategoryService'
import ImagesService from '../services/ImagesService'
import ParametrageService from '../services/ParametrageService'
import Product from '../modules/product'
import ProductService from '../services/ProductService'
export default {
    name:'AdminListProducts',
    data(){
        return{
            new_product: new Product(),
            listcategorias:null,
            imagenCargada:null,
            mensajesError: {}
        }
    },
    computed: {
        subcategories () {
            if(this.new_product.category) {
                return this.new_product.category.subcategories
            }
            else {
                return null
            }
        },
        isAdmin(){
            console.log('check if admin')
            if(this.$store.state.user){
                const adminRole = this.$store.state.user.groups.filter( group => group.name === 'ADMIN' )
                return adminRole.length > 0
            }
            return false
        }
    },
    created(){
      
        CategoryService.getallCategories().then(resp=> {
            console.log(resp.data)
            this.listcategorias=resp.data
        })
    
    },
    methods:{
            cargarImg(event){
             this.imagenCargada= event.target.files[0];
         },
    
        async register() {
        
            if(this.validar()){
                const responseNewProduct = await ProductService.createProduct(this.new_product)
                await ImagesService.saveImage(this.imagenCargada, ParametrageService.BASE_API_URL + '/products/' + responseNewProduct.id + '/image')
                this.$router.push('/admin/products')
            } else {
                console.log('Datos no válidos')
            }
            
        },
        validar () {
            this.mensajesError = {}
            return this.validarSubcategoria() & this.validarCategoria() & this.validarNombre() & this.validarPrecio() & this.validarCantidad() & this.validarImagen()
        },
        validarCategoria () {
            if(!this.new_product.category){
                this.mensajesError.categoria = 'Debes seleccionar una categoria'
                return false
            }
            return true
        },
        validarSubcategoria () {
            if(this.new_product.subcategory){
                const esValida = this.new_product.category.subcategories.filter(subcategoria => {
                    return subcategoria.id === this.new_product.subcategory.id
                }).length > 0
                if(!esValida){
                    this.mensajesError.subcategoria = 'Debes seleccionar una subcategoria'
                    return false
                }
                return true
            }
            this.mensajesError.subcategoria = 'Debes seleccionar una subcategoria'
            return false
        },
        validarNombre () {
            if(!this.new_product.name) {
                this.mensajesError.nombre = 'Debes introducir un nombre' 
                return false
            }
            return true
        },
        validarPrecio () {
            if(!this.new_product.price) {
                this.mensajesError.precio = 'Debes introducir un precio válido'
                
            } else if (this.new_product.price < 0) {
                this.mensajesError.precio = 'El precio no puede ser negativo'
                return false
            }
            return true
        },
        validarCantidad () {
            if(!this.new_product.quantity) {
                this.mensajesError.cantidad = 'Debes introducir una cantidad'
                return false
            } else if (this.new_product.quantity < 0) {
                this.mensajesError.cantidad = 'La cantidad no puede ser negativa'
                return false
            }
            return true
        },
        validarImagen () {
            if(!this.imagenCargada) {
                this.mensajesError.imagen = 'El producto necesita imagen' 
                return false
            }
            return true
        }
    }
    
}
</script>

<style scoped>
.custom-input-file {

  box-shadow: inset 0px 0px 0px 0px rgb(245, 245, 245, 1);
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

.fila-productos:hover{
    background: rgb(13, 96, 204);
    color:white
}
h4{
   
    margin-top: 5%;

}
.select {
 background-color: #4CAF50;
  border: none;
  color: white;
  padding: 16px 32px;
  text-decoration: none;
  margin: 4px 2px;
  cursor: pointer;
}
.buscar{
    margin:10px;
    width: 30%;
    height: 50px;
    background: rgb(4, 63, 141);
    color:white;
}
.buscar:hover{
   
    cursor: pointer;
    background: rgb(44, 100, 206);
    transition: color 0.8s ;

}

.btn {
  text-decoration: none;
  border: 1px solid rgb(146, 148, 248);
  position: relative;
  overflow: hidden;
  margin: 1rem 0;
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

.rojo {
    color: red
}

</style>