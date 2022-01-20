<template>
  <div id="app" class="bg-image" style="background-image: url('https://static.vecteezy.com/system/resources/previews/001/873/028/large_2x/abstract-white-wave-lines-on-gray-background-and-texture-free-vector.jpg">
    <Navbar></Navbar> 
  </div>
</template>

<script>
//import Register from './components/Register.vue'
import Navbar from './components/Navbar.vue'
import ParametrageService from './services/ParametrageService'
import axios  from 'axios'
import TvpService from "./services/TvpService"
export default {
  name: 'App',
  components: {
    Navbar
  },
  created(){
    let usuario_logueado =localStorage.getItem('user')
    if(usuario_logueado!=null)
    {
       axios.get(ParametrageService.BASE_API_URL + "/users/cart/"+usuario_logueado).then((resp)=>{
                   this.$store.commit('carrito',resp.data)
        })
        TvpService.tokenComercio().then(resp=>{
            this.$store.commit('token_comercio',resp.data.authToken)
              localStorage.setItem('token_comercio',resp.data.authToken)
          })
    }
  }
}


</script>

<style scoped>

.bg-image {

  background-repeat: repeat;
  background-position: center center;
  background-size: cover;
  background-attachment: fixed;
  overflow: scroll;
  height: 100vh;
  

}

</style>
