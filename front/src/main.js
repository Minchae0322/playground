import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
const app = createApp(App)
const apiBaseUrl = "http://localhost:8080/";
app.config.globalProperties.$apiBaseUrl = apiBaseUrl;
app.use(createPinia())
app.use(router)

app.mount('#app')
app.use(ElementPlus)