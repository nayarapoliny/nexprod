<script setup>
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { fetchOptimizedProductionPlan } from '../services/api.js';

const { t } = useI18n(); // Get the translation function

// Reactive state using the Composition API's ref()
const productionPlan = ref(null);
const isLoading = ref(false);
const error = ref(null);

// Function to be called on button click
async function handleCalculateClick() {
  isLoading.value = true;
  error.value = null;
  productionPlan.value = null;

  try {
    const data = await fetchOptimizedProductionPlan();
    productionPlan.value = data;
  } catch (err) {
    error.value = t('maximizer.error'); // Use i18n for error message
    console.error(err); // Log the actual error for debugging
  } finally {
    isLoading.value = false;
  }
}
</script>

<template>
  <div class="card-nex">
    <header class="text-center mb-8">
      <h1 class="text-3xl font-bold text-nex-dark">{{ t('maximizer.title') }}</h1>
      <p class="text-gray-600 mt-2">{{ t('maximizer.description') }}</p>
    </header>

    <div class="flex justify-center mb-8">
      <button @click="handleCalculateClick" :disabled="isLoading" class="btn-nex-primary text-lg px-8 py-3">
        {{ isLoading ? t('maximizer.calculatingButton') : t('maximizer.calculateButton') }}
      </button>
    </div>

    <div v-if="isLoading" class="feedback">
      <p>{{ t('maximizer.loading') }}</p>
    </div>

    <div v-if="error" class="feedback error">
      <p class="text-red-600 font-bold">{{ error }}</p>
    </div>

    <div v-if="productionPlan" class="mt-8 pt-8 border-t border-nex-light">
      <h2 class="text-xl font-bold text-nex-dark mb-4 text-center">{{ t('maximizer.resultsTitle') }}</h2>
      <div v-if="productionPlan.length > 0" class="overflow-x-auto">
        <table>
          <thead>
            <tr>
              <th>{{ t('maximizer.productColumn') }}</th>
              <th>{{ t('maximizer.quantityColumn') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in productionPlan" :key="item.productName">
              <td>{{ item.productName }}</td>
              <td class="font-bold text-nex-success text-right">{{ item.quantityToProduce }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-else>
        <p>{{ t('maximizer.noProducts') }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid var(--nexprod-azul-light);
}

th {
  background-color: var(--nexprod-branco-gelo);
  color: var(--nexprod-azul-profundo);
}
</style>
