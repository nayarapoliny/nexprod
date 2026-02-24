import { createApp } from 'vue'
import './assets/theme.css'
import App from './App.vue'
import i18n from './i18n' // Import the i18n instance
import router from './router' // Import the router instance

createApp(App)
  .use(i18n) // Use the i18n plugin
  .use(router) // Use the router plugin
  .mount('#app')
