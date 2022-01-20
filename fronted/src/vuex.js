import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const state = {
    user: null,
    user_carrito:[],
    token_comercio:null
};

const store = new Vuex.Store({
    state,
    getters: {
        user: (state) => {
            return state.user;
        },
        user_carrito:(state) => {
            return state.user_carrito;
        }
    },
    actions: {
        user(context, user){
            context.commit('user', user);
        }
    },
    mutations: {
        user(state, user){
            state.user = user;
        },
        carrito(state,payload){
            state.user_carrito=payload
            console.log("Entro en  vuex")
        },
        token_comercio(state,payload){
            state.token_comercio=payload;
            console.log("Token Comercio Almacenado")

        },
        logOut(state){
            state.user_carrito=[],
            state.token_comerio=null,
            state.user=null
        }
    }
});

export default store;