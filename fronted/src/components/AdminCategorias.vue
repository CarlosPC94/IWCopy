<template>
<div class="container">
    <h1></h1>

    <div class="row">
        <div class="column">
            <h2>Categorías</h2>
            <h4>Añadir categoria</h4>
            <input v-model="categoryInput" placeholder="Nueva categoría" class="form-control" type="text">
            <div>
                  <button v-on:click="addCategory()" class="btn">Añadir</button>
            </div>
            <div class="list-group">
                <a class="list-group-item list-group-item-action flex-column align-items-start"
                :class="{ selected: isCategorySelected(key) }"
                v-for="(categoria, key, i) in categorias" :key="i"
                @click="selectCategory(key, categoria)">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">{{ categoria.name }}</h5>
                        <small>
                            <a @click="deleteCategory(key, categoria)" class="delete-icon">
                                <font-awesome-icon icon="trash"/>
                            </a>
                        </small>
                    </div>
                    <p class="mb-1">id: {{ categoria.id }}</p>
                    <p v-if="alertMessageKey == key" class="mb-1 rojo">No se puede borrar mientras contenga subcategorias o productos</p>
                </a>
            </div>
        </div>
        <div class="column">
            <h2>Subcategorías</h2>
            <h4>Añadir subcategoria</h4>
            <input v-model="subcategoryInput" placeholder="Nueva subcategoría" class="form-control" type="text">
            <div>
                  <button 
                    v-on:click="addSubcategory()"
                    :disabled="selectedCategoryItem < 0"
                    class="btn">Añadir</button>
            </div>
            <h5>Listado de subcategorias</h5>
            <div class="list-group">
                <a class="list-group-item list-group-item-action flex-column align-items-start"
                v-for="(subcategoria, key, i) in subcategorias" :key="i">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">{{ subcategoria.name }}</h5>
                        <small>
                            <a @click="deleteSubcategory(key, subcategoria)" class="delete-icon">
                                <font-awesome-icon icon="trash"/>
                            </a>
                        </small>
                    </div>
                    <p class="mb-1">id: {{ subcategoria.id }}</p>
                    <p v-if="subAlertMessageKey == key" class="mb-1 rojo">No se puede borrar mientras contenga productos</p>
                </a>
            </div>
        </div>
    </div>

</div>
</template>

<script>
import CategoryService from '../services/CategoryService'

export default {
    name: 'AdminCategorias',
    data () {
        return {
            categorias: null,
            subcategorias: null,
            selectedCategoryItem: -1,
            selectedCategory: null,
            categoryInput: '',
            subcategoryInput: '',
            alertMessageKey: -1,
            subAlertMessageKey: -1
        }
    },
    async created () {
        CategoryService.getallCategories().then(resp=> {
            this.categorias = resp.data
        })
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
        selectCategory (key, category) {
            this.selectedCategoryItem = key
            this.selectedCategory = category
            console.log('selected category: ')
            console.log(category)
            this.subcategorias = category.subcategories
        },
        isCategorySelected (key) {
            return key === this.selectedCategoryItem
        },
        async addCategory () {
            if (this.categoryInput) {
                try{
                    const newCategory = await CategoryService.addCategory(this.categoryInput)
                    this.categorias.push(newCategory)
                    this.categoryInput = ''
                } catch (err) {
                    console.log('error: ' +  err)
                }
            }
        },
        async addSubcategory () {
            if (this.subcategoryInput && this.selectedCategory) {
                try {
                    const newSubategory = 
                        await CategoryService.addSubcategory(this.selectedCategory.id, this.subcategoryInput)
                    this.subcategorias.push(newSubategory)
                    this.subcategoryInput = ''
                } catch (err) {
                    console.log('error:' + err)
                }
            }
        },
        async deleteCategory (key, category) {
            this.alertMessageKey = -1
            console.log('Borrar categoria ' + category.name)
            try {
                const response = 
                    await CategoryService.deleteCategory(category.id)
                if (response.status === 200) {
                    const index = this.categorias.indexOf(category)
                    if (index !== -1) {
                        this.categorias.splice(index, 1)
                    }
                }
            } catch (err) {
                console.log('error:' + err)
                this.alertMessageKey = key
            }
        },
        async deleteSubcategory (key, subcategory) {
            this.subAlertMessageKey = -1
            console.log('Borrar subcategoria ' + subcategory.name)
            try {
                const response = 
                    await CategoryService.deleteSubcategory(subcategory.id)
                if (response.status === 200) {
                    const index = this.subcategorias.indexOf(subcategory)
                    if (index !== -1) {
                        this.subcategorias.splice(index, 1)
                    }
                }
            } catch (err) {
                this.subAlertMessageKey = key
                console.log('error:' + err)
            }
        }
    }
}
</script>

<style scoped>
.row {
  display: flex;
}

.column {
  flex: 50%;
  background: white;
}

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

.delete-icon {
    cursor:pointer;
    color: #212529
}

.rojo {
    color: red
}
</style>

