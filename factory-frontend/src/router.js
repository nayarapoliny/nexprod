import { createRouter, createWebHistory } from 'vue-router';
import Optimizer from './views/Optimizer.vue';
import RawMaterials from './views/RawMaterials.vue';
import Products from './views/Products.vue';

const routes = [
  {
    path: '/',
    name: 'Optimizer',
    component: Optimizer,
  },
  {
    path: '/raw-materials',
    name: 'RawMaterials',
    component: RawMaterials,
  },
  {
    path: '/products',
    name: 'Products',
    component: Products,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
