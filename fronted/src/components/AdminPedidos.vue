<template>
    <div v-if="isAdmin" class="container">
    <h1/>
    <div class="row" style="background:white; margin-top: 15px; border-radius: 20px;">
        <h1>Pedidos</h1>
        <div class="col-4" style="background:white; border-radius: 20px;">
            
            <h3>Filtros</h3>
            <h5>Buscar por id</h5>
            <input class="select" type="number" v-model="inputId">
            <button class="btn" @click="buscarPedidoById(inputId)"><b>Buscar</b></button>
            
            <h5>Filtrar por Estado</h5>
            <select class="select" id="select-option"  v-model="inputEstado">
                <option selected></option>
                <option  v-for="estadoPedido in estadosPedido" :key="estadoPedido" :value="estadoPedido"> 
                    {{estadoPedido}}
                </option>
            </select>
            <button class="btn" @click="buscarPedidoByEstado(inputEstado)"><b>Buscar</b></button>

        </div>
        <div class="col-4" style="background:white; border-radius: 20px;">
            <h3>Listado de pedidos</h3>
            <p v-if="pedidosNotFound" class=" rojo">No se encontraron pedidos</p>
            <div v-if="!pedidosNotFound" class="list-group">
                <a class="list-group-item list-group-item-action flex-column align-items-start"
                :class="{ selected: isPedidoSelected(key) }"
                v-for="(pedido, key, i) in pedidos" :key="i"
                @click="selectPedido(key, pedido)">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">Pedido: #{{ pedido.id }}</h5>
                    </div>
                    <p class="mb-1">Importe: {{ pedido.totalPrice }}</p>
                    <p class="mb-1">Estado: {{ pedido.state }}</p>
                </a>
            </div>

        </div>
        <div class="col-4" style="background:white; border-radius: 20px;">

            <h3>Estado</h3>
            <h5 v-if="selectedPedido">Pedido #{{ selectedPedido.id }}</h5>
            <h5 v-if="selectedPedido">Estado: {{ selectedPedido.state }}</h5>
            <select v-if="selectedPedido" class="select" id="select-option"  v-model="selectedEstado">
                <option  v-for="estadoPedido in estadosPedido" :key="estadoPedido" :value="estadoPedido"
                :selected="estadoPedido === selectedPedido.state"> 
                    {{estadoPedido}}
                </option>
            </select>
            <button v-if="selectedPedido" class="btn" @click="changeState()"><b>Editar</b></button>
            <p v-if="successMessage">{{ successMessage }}</p>
            <p v-if="errorMessage" class="rojo">{{ errorMessage }}</p>
        </div>
    </div>
</div>
</template>

<script>
import OrderService from '../services/OrderService'

export default {
    name: 'AdminPedidos',
    data () {
        return {
            pedidos: null,
            selectedPedidoItem: -1,
            selectedPedido: null,
            inputId: null,
            inputEstado: null,
            pedidosNotFound: false,
            estadosPedido: [
                "EN PROCESO",
                "PAGADO",
                'PENDIENTE',
                'SERVIDO',
                'CANCELADO'
            ],
            selectedEstado: null,
            successMessage: null,
            errorMessage: null
        }
    },
    async created () {
        this.cargarPedidos()
    },
    computed: {
        isAdmin(){
            console.log('check if admin')
            if(this.$store.state.user){
                const adminRole = this.$store.state.user.groups.filter( group => group.name === 'ADMIN' )
                return adminRole.length > 0
            }
            return false
        }
    },
    methods: {
        selectPedido (key, pedido) {
            
            this.selectedPedidoItem = key
            this.selectedPedido = pedido
            console.log('selectedpedido')
            console.log(this.selectedPedido)
            console.log(this.selectedPedido.state === this.estadosPedido[0])
        },
        isPedidoSelected (key) {
            return key === this.selectedPedidoItem
        },
        cargarPedidos () {
            OrderService.getAllPedidos().then(resp=> {
                this.pedidos = resp
                if (!resp) {
                    this.pedidos = null
                    this.pedidosNotFound = true
                }
            })
        },
        buscarPedidoByEstado (state) {
            this.pedidosNotFound = false
            if (state) {
                OrderService.getPedidosByState(state).then(resp=> {
                    this.pedidos = resp
                    if (!resp) {
                        this.pedidos = null
                        this.pedidosNotFound = true
                    }
                })
            } else {
                this.cargarPedidos()
            }
        },
        buscarPedidoById (id) {
            this.pedidosNotFound = false
            if (id) {
                OrderService.getPedido(id).then(resp=> {
                    this.pedidos = [resp]
                    if (!resp) {
                        this.pedidos = null
                        this.pedidosNotFound = true
                    }
                })
            } else {
                this.cargarPedidos()
            }
        },
        async changeState () {
            this.successMessage = null
            this.errorMessage = null
            console.log(this.selectedPedido.id)
            console.log(this.selectedEstado)
            try {
                const response = await OrderService.changeStatePedido(this.selectedPedido.id, this.selectedEstado)
                if (response.status === 200) {
                    this.successMessage = 'Se ha cambiado el estado a ' + this.selectedEstado
                    var index = this.pedidos.indexOf(this.selectedPedido)
                    this.pedidos[index].state = this.selectedEstado
                } else {
                    this.errorMessage = 'No se pudo cambiar el estado'
                }
            } catch (err) {
                console.log('error: ' + err)
            }
        }
    }
}
</script>

<style scoped>

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

.selected {
    background-color: rgb(166, 168, 248);
    color: #495057
}

.rojo {
    color: red
}
</style>