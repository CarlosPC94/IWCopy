<template>
  <section class="vh-120 fondo " >
  <div class="mask d-flex align-items-center h-100 ">
    <div class="container h-100">
      <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-12 col-md-9 col-lg-7 col-xl-6">
          <div class="card" style="border-radius: 15px;">
            <div class="card-body p-5 ">
              <h2 class="text-uppercase text-center mb-5">Log in to your account</h2>

                <div class="form-outline mb-4">
                  <input type="email" id="email" v-model="email" class="form-control form-control-lg" />
                  <label class="form-label" for="email">Your Email</label>
                </div>

                <div class="form-outline mb-4">
                  <input type="password" id="password" v-model="password" class="form-control form-control-lg" />
                  <label class="form-label" for="password">Password</label>
                </div>

                <p v-if="mensaje" class="rojo">{{ mensaje }}</p>

                <div class="d-flex justify-content-center">
                  <button v-on:click="login()" class="btn">Login</button>
                </div>

                <p class="text-center text-muted mt-5 mb-0">No registered? <a href="/register" class="fw-bold text-body"><u>Create an account</u></a></p>



            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
</template>

<script>
import axios from "axios"
import User from "../modules/user"
import UserService from "../services/UserService"
import TvpService from "../services/TvpService"
import ParametrageService from '../services/ParametrageService'

export default {
    name: "Login",
    data () {
        return {
            email:'',
            password:'',
            user: new User('',''),
            mensaje: ''
        }
    },
    userService: null,
    created(){  
        this.userService = new UserService(); 
       
    },
    methods: {
        async login() {
            const params = new URLSearchParams()
            params.append('username', this.email)
            params.append('password', this.password)

            const config = {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              }
            }

            axios.post(ParametrageService.BASE_API_URL + "/auth/login", params, config)
              .then(async (result) => {
                console.log('result: ' + JSON.stringify(result))
                console.log('result: ' + JSON.stringify(result.data))
                localStorage.setItem('user', result.data.usuario)
                localStorage.setItem('token', result.data.accessToken)
                localStorage.setItem('wishlist', result.data.wishlist)
                localStorage.setItem('user_cart_id', result.data.cart_id)
                localStorage.setItem('user_name', result.data.user_name)

               await axios.get(ParametrageService.BASE_API_URL +"/users/cart/"+result.data.usuario).then((resp)=>{
                   this.$store.commit('carrito',resp.data)
                })

                ///Obtenemos el token del Comercio
                TvpService.tokenComercio().then(resp=>{
                  this.$store.commit('token_comercio',resp.data.authToken)
                    localStorage.setItem('token_comercio',resp.data.authToken)
                })


                this.userService.getUser().then(resp => {
                  this.$store.dispatch('user', resp.data);
                  const adminRole = this.$store.state.user.groups.filter( group => group.name === 'ADMIN' )
                  if(adminRole.length > 0) 
                    this.$router.push('/admin/products')
                  else
                    window.location.href = '/products'
                })
              })
              .catch((err) => {
                console.log('error: ' + err)
                this.mensaje = 'Credenciales inválidas'
              })
        }
    }

} 
</script>

<style scoped>

.fondo {
  height: 80%;
}

.card {
    box-shadow: #929292;
    box-shadow: 10px 10px 10px 10px #666;
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

.rojo {
    color: red
}

</style>