<template>
    <div class="container emp-profile">
                <div class="row">
                    <div class="col-md-4">
                        <div class="profile-img">
                            <img src="../assets/logo.png" alt=""/>
                            <div class="file btn btn-lg btn-primary">
                                Change Photo
                                <input type="file" name="file" id="imagen" value="" @change="cargarImg($event)"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="profile-head">
                                    <h1>
                                        Edit Your Profile
                                    </h1>
                                    <h3>
                                        {{user.name}}
                                    </h3>
                                    <h6>
                                        {{user.email}}
                                    </h6>
                                    <br>                       
                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                <li class="nav-item">
                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Completa los campos</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <a href="/profile" class="btn">Profile</a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4">
                        <div class="profile-work">
                        </div>
                    </div>
                    <div class="col-md-8">
                        <div class="tab-content profile-tab" id="myTabContent">
                            <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="form-floating">
                                            <input type="email" class="form-control" id="userEmail" placeholder="name@example.com" v-model="email">
                                            <label for="floatingInputValue">Input Your New Email</label>
                                        </div>
                                        <div class="form-floating">
                                            <input type="password" class="form-control" id="userPassword" v-model="password" value="password">
                                            <label for="floatingInputValue">Input Your New Password</label>
                                        </div>                                        
                                        <div class="form-floating">
                                            <input type="text" class="form-control" id="userAddress" v-model="deliveryAddress">
                                            <label for="floatingInputValue">Input Your New Delivery Address</label>
                                        </div>
                                        <button v-on:click="register()" class="btn" id="save_button" style="margin-left: 10px"> SAVE</button>
                            </div>
                        </div>
                    </div>
                </div>        
        </div>
</template>
<script>
import UserService from "../services/UserService"
import ParametrageService from '../services/ParametrageService'
import axios from "axios"
export default {
    name: "EditProfile",
    data () {
        return {
            email:'',
            password:'',
            deliveryAddress:'',
            imagenCargada:null,
            user: null
        }
    },
    userService: null,
    created(){  
        this.userService = new UserService();
    },
    mounted(){
        this.userService.getUser().then(resp => {
            this.user = resp.data;
        })
            
    },
    methods: {
        cargarImg(event){
             this.imagenCargada= event.target.files[0];
         },
         async register() {
          let data = new FormData();
          data.append('userId', this.user.id)
          data.append('userEmail',this.email)
          data.append('userPassword',this.password)
          data.append('userAddress',this.deliveryAddress)
          data.append('file',this.imagenCargada);
          let config = 
          {
             header : {
            'Content-Type': 'multipart/form-data'
             }               
          }
          await axios.post(ParametrageService.BASE_API_URL + "/users/perfil", data, {headers: config.header}).then((data) => {
            console.log(data)
            window.location.href = '/profile';
        });

      }
    }
}
</script>

<style scoped>

.form-floating{
    margin: 10px
}

body{
    background: -webkit-linear-gradient(left, #3931af, #00c6ff);
}
.emp-profile{
    padding: 3%;
    margin-top: 3%;
    margin-bottom: 3%;
    border-radius: 0.5rem;
    background: #fff;
}
.profile-img{
    text-align: center;
}
.profile-img img{
    width: 70%;
    height: 100%;
}
.profile-img .file {
    position: relative;
    overflow: hidden;
    margin-top: -20%;
    width: 70%;
    border: none;
    border-radius: 0;
    font-size: 15px;
    background: #212529b8;
}
.profile-img .file input {
    position: absolute;
    opacity: 0;
    right: 0;
    top: 0;
}
.profile-head h5{
    color: #333;
}
.profile-head h6{
    color: #0062cc;
}
.profile-edit-btn{
    border: none;
    border-radius: 1.5rem;
    width: 70%;
    padding: 2%;
    font-weight: 600;
    color: #6c757d;
    cursor: pointer;
}
.proile-rating{
    font-size: 12px;
    color: #818182;
    margin-top: 5%;
}
.proile-rating span{
    color: #495057;
    font-size: 15px;
    font-weight: 600;
}
.profile-head .nav-tabs{
    margin-bottom:5%;
}
.profile-head .nav-tabs .nav-link{
    font-weight:600;
    border: none;
}
.profile-head .nav-tabs .nav-link.active{
    border: none;
    border-bottom:2px solid #0062cc;
}
.profile-work{
    padding: 14%;
    margin-top: -15%;
}
.profile-work p{
    font-size: 12px;
    color: #818182;
    font-weight: 600;
    margin-top: 10%;
}
.profile-work a{
    text-decoration: none;
    color: #495057;
    font-weight: 600;
    font-size: 14px;
}
.profile-work ul{
    list-style: none;
}
.profile-tab label{
    font-weight: 600;
}
.profile-tab p{
    font-weight: 600;
    color: #0062cc;
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

</style>