<template>
  <section class="vh-120 overf" >
  <div class="mask d-flex align-items-center h-100 gradient-custom-3">
    <div class="container h-100">
      <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-12 col-md-9 col-lg-7 col-xl-6">
          <div class="card" style="border-radius: 15px; margin-top: 105px; margin-bottom: 20px">
            <div class="card-body p-5 ">
              <h2 class="text-uppercase text-center mb-5">Create an account</h2>



                <div class="form-outline mb-4">
                  <p v-if="mensajesError.nombre" class="rojo">{{ mensajesError.nombre }}</p>
                  <input type="text" id="name" v-model="name" class="form-control form-control-lg" />
                  <label class="form-label" for="name">Your Name</label>
                </div>

                <div class="form-outline mb-4">
                  <p v-if="mensajesError.email" class="rojo">{{ mensajesError.email }}</p>
                  <input type="email" id="email" v-model="email" class="form-control form-control-lg" />
                  <label class="form-label" for="email">Your Email</label>
                </div>

                <div class="form-outline mb-4">
                  <p v-if="mensajesError.password" class="rojo">{{ mensajesError.password }}</p>
                  <input type="password" id="password" v-model="password" class="form-control form-control-lg" />
                  <label class="form-label" for="password">Password</label>
                </div>

                <div class="form-outline mb-4">
                  <p v-if="mensajesError.rePassword" class="rojo">{{ mensajesError.rePassword }}</p>
                  <input type="password" id="repassword" v-model="rePassword" class="form-control form-control-lg" />
                  <label class="form-label" for="password">Repeat your password</label>
                </div>

                 <div class="form-outline mb-4">
                   <p v-if="mensajesError.birthdate" class="rojo">{{ mensajesError.birthdate }}</p>
                  <input type="date" id="birthdate" v-model="birthdate" class="form-control form-control-lg" />
                  <label class="form-label" for="birthdate">Your brithdate</label>
                </div>
                
                <div class="form-outline mb-4">
                  <input type="address" id="address" v-model="deliveryAddress" class="form-control form-control-lg" />
                  <label class="form-label" for="address">Delivery address</label>
                </div> 

                <div class="form-outline mb-4">           
                  <label class="btn btn-default"> Upload your profile image
                      <input type="file" id="imagen"  class="btn"  accept="image/*" value="" @change="cargarImg($event)" hidden />
                  </label> 
                  <div id="preview">
                    <img id="previ" v-if="url" :src="url" />
                  </div>                  
                </div>
                
                                                              

                <div class="form-check d-flex justify-content-center mb-5">
                  <input
                    class="form-check-input me-2"
                    type="checkbox"
                    value=""
                    id="form2Example3cg"
                  />
                  <label class="form-check-label" for="form2Example3g">
                    I agree all statements in <a href="#!" class="text-body"><u>Terms of service</u></a>
                  </label>
                </div>

                <div class="d-flex justify-content-center">
                  <button v-on:click="register()" class="btn">Register</button>
                </div>

                <p class="text-center text-muted mt-5 mb-0">Have already an account? <a href="/login" class="fw-bold text-body"><u>Login here</u></a></p>



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
import ParametrageService from '../services/ParametrageService'

export default {
  name: "Register",
  data(){
        return{
            name:'',
            email:'',
            password:'',
            birthdate:'',
            deliveryAddress:'',
            imagenCargada:null,
            url: null,
            mensajesError: {},
            rePassword: ''      
        }
    },
  methods:{
      cargarImg(event){
             this.imagenCargada= event.target.files[0];
             this.onFileChange(event)
         },
      async register() {

        if(this.validar()){
            let data = new FormData();
            data.append('userName',this.name)
            data.append('userEmail',this.email)
            data.append('userPassword',this.password)
            data.append('userDate',this.birthdate)
            data.append('userAddress',this.deliveryAddress)
            data.append('file',this.imagenCargada);
            let config = 
            {
              header : {
              'Content-Type': 'multipart/form-data'
              }               
            }
            await axios.post(ParametrageService.BASE_API_URL + "/auth/register", data, {headers: config.header}).then((data) => {
            if (data.status == 201) {
              window.location.href = '/login';
            }
          });
        } else {
          console.log("Datos no válidos")
        }

      },
      onFileChange(e) {
        const file = e.target.files[0];
        this.url = URL.createObjectURL(file);
    },
    validar () {
        this.mensajesError = {}
        return this.validarNombre() & this.validarEmail() & this.validarPass() & this.validarDate() & this.validarRePass()
    },
    validarNombre () {
        if(!this.name) {
            this.mensajesError.nombre = 'Introduce un nombre de usuario' 
            return false
        }
        return true
    },
    validarEmail () {
        if(this.email == '' || !this.email.includes('@')) {
            this.mensajesError.email = 'Introduce un Email válido' 
            return false
        }
        return true
    },
    validarPass () {
        if( this.password == '' || this.password.length<6 ) {
            this.mensajesError.password = 'Introduce una contraseña válida (mínimo 6 carácteres)' 
            return false
        }
        return true
    },
    validarRePass() {
      if(this.password != this.rePassword) {
        this.mensajesError.rePassword = 'Las contraseñas no coinciden'
        return false
      }
      return true
    },
    validarDate () {
      if(!this.birthdate) {
          this.mensajesError.birthdate = 'Introduce tu fecha de nacimiento' 
          return false
      }
      return true
    },
    validarAddress () {
      if(this.deliveryAddress == '') {
          this.mensajesError.address = 'Introduce tu dirección' 
          return false
      }
      return true
    }
  }
};
</script>

<style scoped>

#previ {
  width: 80px;
  border-radius: 20px;
  float: left;
  margin-top: 10px;
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
