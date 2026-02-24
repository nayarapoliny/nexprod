<script setup>
import { ref, onMounted } from 'vue';
import { getRawMaterials, createRawMaterial, updateRawMaterial, deleteRawMaterial } from '../services/api';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

const rawMaterials = ref([]);
const isLoading = ref(true);
const error = ref(null);

const isFormVisible = ref(false);
const editingRawMaterial = ref(null);
const form = ref({
  code: '',
  name: '',
  stockQuantity: 0,
});

async function loadRawMaterials() {
  try {
    isLoading.value = true;
    const response = await getRawMaterials();
    rawMaterials.value = response;
  } catch (err) {
    error.value = t('rawMaterials.error.load');
  } finally {
    isLoading.value = false;
  }
}

function showAddForm() {
  editingRawMaterial.value = null;
  form.value = { code: '', name: '', stockQuantity: 0 };
  isFormVisible.value = true;
}

function showEditForm(material) {
  editingRawMaterial.value = { ...material };
  form.value = { ...material };
  isFormVisible.value = true;
}

function hideForm() {
  isFormVisible.value = false;
}

async function handleSubmit() {
  try {
    if (editingRawMaterial.value) {
      await updateRawMaterial(editingRawMaterial.value.id, form.value);
    } else {
      await createRawMaterial(form.value);
    }
    hideForm();
    await loadRawMaterials(); // Refresh the list
  } catch (err) {
    error.value = t('rawMaterials.error.save');
  }
}

async function handleDelete(id) {
  if (confirm(t('rawMaterials.actions.confirmDelete'))) {
    try {
      await deleteRawMaterial(id);
      await loadRawMaterials(); // Refresh the list
    } catch (err) {
      error.value = t('rawMaterials.error.delete');
    }
  }
}

onMounted(loadRawMaterials);
</script>

<template>
  <div class="card-nex">
    <h1 class="text-2xl font-bold text-nex-dark mb-6">{{ t('rawMaterials.title') }}</h1>

    <div v-if="isLoading" class="p-4 text-center bg-nex-ice rounded">{{ t('rawMaterials.loading') }}</div>
    <div v-if="error" class="p-4 mb-4 bg-red-50 border border-red-200 rounded">
      <p class="text-red-600 font-bold">{{ error }}</p>
    </div>

    <div v-if="!isLoading && !isFormVisible">
      <div class="mb-6">
        <button @click="showAddForm" class="btn-nex-primary">{{ t('rawMaterials.addNew') }}</button>
      </div>
      <div class="overflow-x-auto">
        <table class="w-full border-collapse">
          <thead>
            <tr class="bg-nex-ice text-nex-dark">
              <th class="p-3 text-left border-b border-nex-light">{{ t('rawMaterials.table.code') }}</th>
              <th class="p-3 text-left border-b border-nex-light">{{ t('rawMaterials.table.name') }}</th>
              <th class="p-3 text-left border-b border-nex-light">{{ t('rawMaterials.table.stock') }}</th>
              <th class="p-3 text-left border-b border-nex-light">{{ t('rawMaterials.table.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="material in rawMaterials" :key="material.id" class="hover:bg-gray-50">
              <td class="p-3 border-b border-nex-light">{{ material.code }}</td>
              <td class="p-3 border-b border-nex-light">{{ material.name }}</td>
              <td class="p-3 border-b border-nex-light font-bold text-nex-gold">{{ material.stockQuantity }}</td>
              <td class="p-3 border-b border-nex-light flex gap-2">
                <button @click="showEditForm(material)" class="px-3 py-1 border border-nex-light rounded text-nex-dark hover:bg-nex-light/10">{{ t('rawMaterials.actions.edit') }}</button>
                <button @click="handleDelete(material.id)" class="px-3 py-1 border border-red-200 rounded text-red-600 hover:bg-red-50">{{ t('rawMaterials.actions.delete') }}</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div v-if="isFormVisible" class="mt-6 p-6 bg-nex-ice rounded-lg">
      <h2 class="text-xl font-bold mb-4">{{ editingRawMaterial ? t('rawMaterials.form.editTitle') : t('rawMaterials.form.addTitle') }}</h2>
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block font-bold mb-1" for="code">{{ t('rawMaterials.form.codeLabel') }}</label>
          <input id="code" v-model="form.code" type="text" class="input-nex w-full" required />
        </div>
        <div>
          <label class="block font-bold mb-1" for="name">{{ t('rawMaterials.form.nameLabel') }}</label>
          <input id="name" v-model="form.name" type="text" class="input-nex w-full" required />
        </div>
        <div>
          <label class="block font-bold mb-1" for="stockQuantity">{{ t('rawMaterials.form.stockLabel') }}</label>
          <input id="stockQuantity" v-model.number="form.stockQuantity" type="number" class="input-nex w-full" required min="0" />
        </div>
        <div class="flex gap-4 pt-2">
          <button type="submit" class="btn-nex-primary">{{ t('rawMaterials.form.save') }}</button>
          <button @click="hideForm" type="button" class="px-4 py-2 bg-white border border-nex-light rounded-md">{{ t('rawMaterials.form.cancel') }}</button>
        </div>
      </form>
    </div>
  </div>
</template>
