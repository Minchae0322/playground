import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {from} from "node-vibrant";
const app = createApp(App)


app.config.globalProperties.$apiBaseUrl = "http://localhost:8080/api";

app.use(createPinia())
app.use(router)
app.mount('#app')
app.use(ElementPlus)