<template>
    <div>
        <p>PEDIDOS</p>
        <table class="table table-dark">
            <thead>
                <tr>
                    <th scope="col">ID PEDIDO</th>
                    <th scope="col">Fecha</th>
                    <th scope="col">Estado</th>
                    <th scope="col">Precio</th>
                    <th scope="col">Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(pedido) in pedidos" :key="pedido.id">
                    <th scope="row">{{pedido.id}}</th>
                    <td>{{pedido.date.split("T")[0]}}</td>
                    <td>{{pedido.state}}</td>
                    <td>{{pedido.totalPrice}} €</td>
                    <td><button class="btn" @click="orderInfo(pedido.id)">Ver Pedido</button></td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import OrderService from '../services/OrderService'
export default {
    name: 'MyOrders',
    data(){
        return {
            pedidos: [],
    
        }
    },
    async created(){
        this.pedidos = await OrderService.getPedidos(localStorage.getItem('user'))
       
    
    },
    methods:{
        orderInfo(order_id){
            this.$router.push({
                    name: "OrderInfo",
                    params:{
                        id: order_id,
                    }
                });
        },
    }
}
</script>
<style scoped>

.btn {
  text-decoration: none;
  border: 1px solid rgba(57, 29, 65, 0.473);
  position: relative;
  overflow: hidden;
  width: 30%;
  border-radius: 15px;
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