<script setup>
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
// 1. IMPORTANDO A LOGO AQUI
import logoImg from './assets/logo-nexprod.png';

const isSidebarOpen = ref(false);
const toggleSidebar = () => isSidebarOpen.value = !isSidebarOpen.value;
const { t } = useI18n();
</script>

<template>
  <div class="min-h-screen bg-nex-ice flex flex-col md:flex-row">
    <header class="md:hidden bg-nex-dark text-white p-4 flex items-center justify-between shadow-md">
      <img :src="logoImg" alt="Nexprod Logo" class="h-8 w-auto" />
      
      <button @click="toggleSidebar" class="text-2xl">☰</button>
    </header>

    <aside 
      :class="[
        'bg-nex-dark text-white w-64 flex-shrink-0 transition-all duration-300 z-50',
        isSidebarOpen ? 'fixed inset-y-0 left-0' : 'hidden md:block sticky top-0 h-screen'
      ]"
    >
      <div class="p-6">
       <img :src="logoImg" alt="Nexprod Logo" class="h-12 w-auto mb-8 hidden md:block filter brightness-0 invert" />
        
        <nav class="flex flex-col gap-2">
          <router-link to="/" class="p-3 rounded hover:bg-white/10 transition-colors" active-class="bg-nex-light/20 text-nex-light font-bold" @click="isSidebarOpen = false">{{ t('nav.optimizer') }}</router-link>
          <router-link to="/raw-materials" class="p-3 rounded hover:bg-white/10 transition-colors" active-class="bg-nex-light/20 text-nex-light font-bold" @click="isSidebarOpen = false">{{ t('nav.rawMaterials') }}</router-link>
          <router-link to="/products" class="p-3 rounded hover:bg-white/10 transition-colors" active-class="bg-nex-light/20 text-nex-light font-bold" @click="isSidebarOpen = false">{{ t('nav.products') }}</router-link>
        </nav>
      </div>
    </aside>

    <div v-if="isSidebarOpen" @click="isSidebarOpen = false" class="fixed inset-0 bg-black/50 z-40 md:hidden"></div>

    <main class="flex-1 p-4 md:p-8">
      <div class="max-w-6xl mx-auto">
        <router-view />
      </div>
    </main>
  </div>
</template>