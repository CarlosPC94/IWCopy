<template>
    <h1>Pruebas</h1>
</template>

<script>
// import ProductService from '../services/ProductService'
// import CategoryService from '../services/CategoryService'
import axios  from 'axios'

export default {
    name: 'Pruebas',
    async created(){
        /*
        ProductService.getAllProducts().then(resp=> {
            console.log(resp.data)
            this.listproductos=resp.data
        })
        CategoryService.getallCategories().then(resp=> {
            console.log("f"+resp.data)
            this.listcategorias=resp.data
        })*/
        const url="http://67.205.141.219:8080/api/products"

        const response = await axios.get(url + '/all')
        console.log(response)
    }
}
</script>