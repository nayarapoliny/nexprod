<script setup>
import { ref, onMounted } from 'vue';
import { getProducts, createProduct, updateProduct, deleteProduct, getRawMaterials } from '../services/api';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

// Main data
const products = ref([]);
const rawMaterials = ref([]); // For the form dropdown

// State management
const isLoading = ref(true);
const error = ref(null);

// Form management
const isFormVisible = ref(false);
const editingProduct = ref(null);
const form = ref({
  code: '',
  name: '',
  value: 0,
  composition: [],
});

async function loadInitialData() {
  try {
    isLoading.value = true;
    const [productsResponse, rawMaterialsResponse] = await Promise.all([
      getProducts(),
      getRawMaterials(),
    ]);
    products.value = productsResponse;
    rawMaterials.value = rawMaterialsResponse;
  } catch (err) {
    error.value = t('products.error.load');
  } finally {
    isLoading.value = false;
  }
}

function showAddForm() {
  editingProduct.value = null;
  form.value = { code: '', name: '', value: 0, composition: [] };
  isFormVisible.value = true;
}

function showEditForm(product) {
  editingProduct.value = { ...product };
  // Map composition for the form
  const compositionForForm = product.composition.map(comp => ({
    rawMaterialId: comp.rawMaterial.id,
    quantity: comp.quantity,
  }));
  form.value = { ...product, composition: compositionForForm };
  isFormVisible.value = true;
}

function hideForm() {
  isFormVisible.value = false;
}

function addCompositionItem() {
  form.value.composition.push({ rawMaterialId: null, quantity: 1 });
}

function removeCompositionItem(index) {
  form.value.composition.splice(index, 1);
}

async function handleSubmit() {
  try {
    if (editingProduct.value) {
      await updateProduct(editingProduct.value.id, form.value);
    } else {
      await createProduct(form.value);
    }
    hideForm();
    await loadInitialData(); // Refresh the list
  } catch (err) {
    error.value = t('products.error.save');
  }
}

async function handleDelete(id) {
  if (confirm('Are you sure you want to delete this product?')) {
    try {
      await deleteProduct(id);
      await loadInitialData(); // Refresh the list
    } catch (err) {
      error.value = t('products.error.delete');
    }
  }
}

onMounted(loadInitialData);
</script>

<template>
  <div class="card-nex">
    <h1 class="text-2xl font-bold text-nex-dark mb-6">{{ t('products.title') }}</h1>

    <div v-if="isLoading" class="p-4 text-center bg-nex-ice rounded">{{ t('products.loading') }}</div>
    <div v-if="error" class="p-4 mb-4 bg-red-50 border border-red-200 rounded">
      <p class="text-red-600 font-bold">{{ error }}</p>
    </div>

    <div v-if="!isLoading && !isFormVisible">
      <div class="mb-6">
        <button @click="showAddForm" class="btn-nex-primary">{{ t('products.addNew') }}</button>
      </div>
      <div class="overflow-x-auto">
        <table class="w-full border-collapse">
          <thead>
            <tr class="bg-nex-ice text-nex-dark">
              <th class="p-3 text-left border-b border-nex-light">{{ t('products.table.code') }}</th>
              <th class="p-3 text-left border-b border-nex-light">{{ t('products.table.name') }}</th>
              <th class="p-3 text-left border-b border-nex-light">{{ t('products.table.value') }}</th>
              <th class="p-3 text-left border-b border-nex-light">{{ t('products.table.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50">
              <td class="p-3 border-b border-nex-light">{{ product.code }}</td>
              <td class="p-3 border-b border-nex-light">{{ product.name }}</td>
              <td class="p-3 border-b border-nex-light font-bold text-nex-success">R$ {{ product.value.toFixed(2) }}</td>
              <td class="p-3 border-b border-nex-light flex gap-2">
                <button @click="showEditForm(product)" class="px-3 py-1 border border-nex-light rounded text-nex-dark hover:bg-nex-light/10">{{ t('rawMaterials.actions.edit') }}</button>
                <button @click="handleDelete(product.id)" class="px-3 py-1 border border-red-200 rounded text-red-600 hover:bg-red-50">{{ t('rawMaterials.actions.delete') }}</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div v-if="isFormVisible" class="mt-6 p-6 bg-nex-ice rounded-lg">
      <h2 class="text-xl font-bold mb-4">{{ editingProduct ? t('products.form.editTitle') : t('products.form.addTitle') }}</h2>
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block font-bold mb-1" for="code">{{ t('products.table.code') }}</label>
          <input id="code" v-model="form.code" type="text" class="input-nex w-full" required />
        </div>
        <div>
          <label class="block font-bold mb-1" for="name">{{ t('products.table.name') }}</label>
          <input id="name" v-model="form.name" type="text" class="input-nex w-full" required />
        </div>
        <div>
          <label class="block font-bold mb-1" for="value">{{ t('products.table.value') }}</label>
          <input id="value" v-model.number="form.value" type="number" class="input-nex w-full" required min="0" />
        </div>

        <fieldset class="border border-nex-light p-4 rounded">
          <legend class="px-2 font-bold text-nex-dark">{{ t('products.form.composition') }}</legend>
          <div v-for="(item, index) in form.composition" :key="index" class="flex flex-col md:flex-row gap-2 mb-3">
            <select v-model="item.rawMaterialId" class="input-nex flex-1" required>
              <option :value="null" disabled>{{ t('rawMaterials.loading') }}</option>
              <option v-for="material in rawMaterials" :key="material.id" :value="material.id">
                {{ material.name }}
              </option>
            </select>
            <input v-model.number="item.quantity" type="number" class="input-nex md:w-24" min="1" required />
            <button @click.prevent="removeCompositionItem(index)" class="text-red-600 px-2 hover:bg-red-50 rounded">{{ t('rawMaterials.actions.delete') }}</button>
          </div>
          <button @click.prevent="addCompositionItem" class="mt-2 text-nex-dark border border-nex-light px-3 py-1 rounded hover:bg-nex-light/10 text-sm">{{ t('products.form.addMaterial') }}</button>
        </fieldset>

        <div class="flex gap-4 pt-2">
          <button type="submit" class="btn-nex-primary">{{ t('rawMaterials.form.save') }}</button>
          <button @click="hideForm" type="button" class="px-4 py-2 bg-white border border-nex-light rounded-md">{{ t('rawMaterials.form.cancel') }}</button>
        </div>
      </form>
    </div>
  </div>
</template>
