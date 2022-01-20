<template>
<div class="container">
    <div class="row" style="background:white; margin-top: 15px; border-radius: 20px;">
        <div >
            <h1>Usuarios</h1>
            
            <table class="table" style="background:white">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Nombre</th>
                        <th>Email</th> 
                        <th></th>
                    </tr>

                </thead>
                <tbody v-for="user in users" :key="user.id">
                    <tr  class="fila-productos">
                        <td>{{user.id}}</td>
                        <td>{{user.name}}</td>
                        <td>{{user.email}}</td>
                        <td>
                            <a @click="deleteUser(user.id)" class="delete-icon">
                                <font-awesome-icon icon="trash"/>
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
import UserService from "../services/UserService";

export default {
  name: "Users",
  data() {
    return {
      users: null,
    };
  },
  userService: null,
  created() {
    this.userService = new UserService();
  },
  mounted() {
    this.userService.getAll().then((data) => {
      this.users = data.data;
    });
  },
  methods: {
    deleteUser(user){
      this.userService.deleteUser(user)
    }
  }
};
</script>

<style scoped>

.gradient-custom-3 {
  /* fallback for old browsers */
  background: #84fab0;

  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to right, rgba(0, 0, 0, 0.5), rgba(255, 255, 255, 0.692));

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to right, rgba(56, 66, 197, 0.5), rgba(238, 233, 226, 0.801))
}
.gradient-custom-4 {
  /* fallback for old browsers */
  background: #84fab0;

  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to right, rgba(56, 66, 197, 0.5), rgba(238, 233, 226, 0.801));

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to right, rgba(56, 66, 197, 0.5), rgba(238, 233, 226, 0.801))
}

.bg-image {

  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;

}

</style>
