<template>
<div v-if="isAdmin" class="container">
    <div class="row" style="background:white; margin-top: 15px; border-radius: 20px;">

        <div class="col-4">
            <h1>Filtros</h1>
            <h4>Buscar por id</h4>
            <input class="select" type="text" v-model="id_product">
            <button class="btn" @click="buscarProductById(id_product)"><b>Buscar</b> </button>
            
            <h4>Filtrar por Categoria</h4>
            <select class="select" id="select-option"  v-model="selectedCategory">
                <option  v-for="category in listcategorias" :key="category.id" :value="category"> 
                    {{category.name}}
                </option>
            </select>
            <button class="btn" @click="filtrarCategoria()"><b>Filtrar</b></button>

            <h4>Filtrar por Subcategoria</h4>
            <select class="select" id="select-option-sub" v-model="selectedSubcategory">
                <option  v-for="subcategoria in subcategorias" :key="subcategoria.id" :value="subcategoria" > 
                    {{subcategoria.name}}
                </option>
            </select>
            <button class="btn" @click="filtrarSubcategoria()"><b>Filtrar</b></button>
            <!--
             <input type="text">
             <h4>Ordenar Por</h4>
             <input type="text">
             <div class="col-md-2">
                <a href="/admin/products/create" class="btn">Crear Producto</a>
             </div>
             -->
        </div>

        <div class="col-8" >
            <h1>Productos</h1>
            <div>
                <a style="margin=0px;" href="/admin/products/create" class="btn">Crear Producto</a>
            </div>
            
            <table class="table" style="background:white;">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Nombre</th>
                        <th>Cateoria</th>
                        <th>Precio</th>   
                        <th></th>
                        <th></th>
                    </tr>

                </thead>
                <tbody v-for="list in listproductos" :key="list.id">
                    <tr  class="fila-productos">
                        <td>{{list.id}}</td>
                        <td>{{list.name}}</td>
                        <td>{{list.category.name}}</td>
                        <td>{{list.price}}€</td>
                        <td>
                            <a @click="deleteProduct(list)" class="delete-icon">
                                <font-awesome-icon icon="trash"/>
                            </a>
                        </td>
                        <td>
                            <a @click="updateProduct(list.id)" class="delete-icon">
                                <font-awesome-icon icon="edit"/>
                            </a>
                        </td>
                    </tr>
                </tbody>

            </table>

        </div>


    </div>
</div>
    
</template>

<script>
import ProductService from '../services/ProductService'
import CategoryService from '../services/CategoryService'
export default {
    name:'AdminListProducts',
    data(){
        return{
            id_product: "",
            listproductos: null,
            listcategorias: null,
            selectedCategory: null,
            selectedSubcategory: null
        }
    },
    computed: {
        subcategorias () {
            if(this.selectedCategory) {
                return this.selectedCategory.subcategories
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
    async created(){
        ProductService.getAllProducts().then(resp=> {
            console.log(resp.data)
            this.listproductos=resp.data
        })
        CategoryService.getallCategories().then(resp=> {
            console.log("f"+resp.data)
            this.listcategorias=resp.data
        })
    },
    methods:{
        async deleteProduct(product){
            try {
                const response = await ProductService.deleteProduct(product.id)
                if (response.status === 200) {
                    const index = this.listproductos.indexOf(product)
                    if (index !== -1) {
                        this.listproductos.splice(index, 1)
                    }
                } else {
                    console.log('No se pudo borrar')
                }
            } catch (err) {
                console.log('error: ' + err)
            }
            
        },
        updateProduct(index)
        {
             this.$router.push({
                    name: "AdminUpdateProduct",
                    params:{
                        id: index,
                    }
                });
        },
        async filtrarCategoria(){
            console.log("Categoriaid "+ this.selectedCategory.id)
            ProductService.getAllProductsByCategory(this.selectedCategory.id).then(resp=> {
                this.listproductos=resp.data
            })
        },
        async filtrarSubcategoria(){
            console.log("Subcategoriaid "+ this.selectedSubcategory.id)
            ProductService.getAllProductsBySubcategory(this.selectedSubcategory.id).then(resp=> {
                this.listproductos=resp.data
            })
        },
        async buscarProductById(id){
            console.log("id")
               
            if(id!="")
            {
                await  ProductService.getAllProductsById(id).then(resp=> {
                    this.listproductos=[]
                    this.listproductos.push(resp.data)
                    })
            }
            else
            {
                ProductService.getAllProducts().then(resp=> {
                            console.log(resp.data)
                            this.listproductos=resp.data
                })
            }


        }
    }
    
}
</script>

<style scoped>
.fila-productos:hover{
    background: rgb(13, 96, 204);
    color:white
}
h4{
    margin-top: 5%;

}
.select {
  width: 60%;
  padding: 5px 20px;
  height: 40px;
  border-radius: 4px;
  border-color: rgb(22, 72, 180);
}

.btn {
  text-decoration: none;
  border: 1px solid rgb(146, 148, 248);
  position: relative;
  overflow: hidden;
  margin: 15px;
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

.delete-icon {
    cursor:pointer;
    color: #212529
}
</style>