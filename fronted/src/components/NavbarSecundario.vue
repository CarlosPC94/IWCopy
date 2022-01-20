<template>
    <div class="nav-categorias">
        <span v-for="(menuItem, i) in menuItems" 
            :key="i" 
            @click="clickAction(menuItem.itemURL)"
            class="categorias">
            {{ menuItem.itemName }}
        </span>
    </div>
</template>

<script>
export default {
    name: 'NavbarSecundario',
    props: ['menuItems', 'clickAction']
}
</script>

<style scoped>
.nav-categorias{
    background-image: url("../assets/geo.jpg");
}
.categorias{
    font-size: 20px;
    margin:50px;
    cursor: pointer;
   color:white
}
.categorias:hover{
    color:rgb(95, 162, 240)
}
</style>