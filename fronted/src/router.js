import Vue from "vue";
import VueRouter from 'vue-router'

import Register from './components/Register.vue'
import Users from './components/Users.vue'
import Login from './components/Login.vue'
import Products from './components/ListProducts'
import InfoProducts from './components/InfoProduct'
import Profile from './components/Profile'
import EditProfile from './components/EditProfile'
import Images from './components/Images'

import WishList from './components/WishList'

import AdminListProducts from  './components/AdminListProducts'
import AdminCreateProduct from  './components/AdminCreateProduct'
import AdminUpdateProduct from  './components/AdminUpdateProduct'
import AdminPedidos from  './components/AdminPedidos'
import AdminCategorias from  './components/AdminCategorias'
//import OrderCreate from './components/OrderCreate'
import MyOrders from './components/MyOrders'
import OrderInfo from './components/OrderInfo'
import Pruebas from './components/Pruebas'
import ProcesarPedido from './components/ProcesarPedido'
import Home from './components/Home'

Vue.use(VueRouter);

const routes = [
    {
      path: "/register",
      name: "Register",
      component: Register
    },
    {
      path: "/users",
      name: "User",
      component: Users
    },
    {
      path: "/login",
      name: "Login",
      component: Login
    }, 
    {
      path: "/products",
      name: "Products",
      component: Products
    }, 
    {
      path: "/products/:id",
      name: "InfoProducts",
      component: InfoProducts
    },
    {
      path: "/wishlist",
      name: "WishList",
      component: WishList
    },
    {
      path: "/admin/categories",
      name: "AdminCategorias",
      component: AdminCategorias
    }, 
    {
      path: "/admin/orders",
      name: "AdminPedidos",
      component: AdminPedidos
    },
    {
      path: "/admin/products",
      name: "AdminListProducts",
      component: AdminListProducts
    }, 
    {
      path: "/admin/products/create",
      name: "AdminCreateProduct",
      component: AdminCreateProduct
    },
    {
      path: "/admin/products/update/:id",
      name: "AdminUpdateProduct",
      component: AdminUpdateProduct
    },
    {
      path: "/profile",
      name: "Profile",
      component: Profile
    },
    {
      path: "/editProfile",
      name: "EditProfile",
      component: EditProfile
    },
    {
      path: "/order/new",
      name: "ProcesarPedido",
      component: ProcesarPedido
    },
    {
      path: "/images/prueba",
      name: "Images",
      component: Images
    },
    {
      path: "/myOrders",
      name: "MyOrders",
      component: MyOrders

    },
    {
      path: "/orderInfo",
      name: "OrderInfo",
      component: OrderInfo
    },
    {
      path: "/pruebas",
      name: "Pruebas",
      component: Pruebas
    },
    {
      path: "/",
      name: "Home",
      component: Home
    }
]

const router = new VueRouter({
    mode: "history",
    routes
  });

  export default router;