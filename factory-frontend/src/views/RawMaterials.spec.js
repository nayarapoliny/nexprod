import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';
import RawMaterials from './RawMaterials.vue';
import * as api from '../services/api';
import { createI18n } from 'vue-i18n';

// Mock the full api module
vi.mock('../services/api');

const i18n = createI18n({
  legacy: false, // Use composition API
  locale: 'en',
  messages: {
    en: {
      rawMaterials: {
        title: 'Raw Materials Management',
        loading: 'Loading raw materials...',
        addNew: 'Add New Raw Material',
        table: { code: 'Code', name: 'Name', stock: 'Stock Quantity', actions: 'Actions' },
        form: {
          addTitle: 'Add Raw Material',
          editTitle: 'Edit Raw Material',
          codeLabel: 'Code',
          nameLabel: 'Name',
          stockLabel: 'Stock Quantity',
          save: 'Save',
          cancel: 'Cancel'
        },
        actions: { edit: 'Edit', delete: 'Delete', confirmDelete: 'Are you sure?' },
      }
    }
  }
});

const global = {
  plugins: [i18n],
};

describe('RawMaterials.vue', () => {
  const mockRawMaterials = [
    { id: 1, code: 'FE', name: 'Iron', stockQuantity: 100 },
    { id: 2, code: 'CU', name: 'Copper', stockQuantity: 50 },
  ];

  beforeEach(() => {
    vi.resetAllMocks();
    // Mock the API functions before each test
    api.getRawMaterials.mockResolvedValue(mockRawMaterials);
    api.createRawMaterial.mockResolvedValue({ id: 3, code: 'WD', name: 'Wood', stockQuantity: 200 });
  });

  it('fetches and displays raw materials on mount', async () => {
    const wrapper = mount(RawMaterials, { global });
    
    expect(api.getRawMaterials).toHaveBeenCalledTimes(1);
    
    // Wait for the component to update
    await wrapper.vm.$nextTick();
    await wrapper.vm.$nextTick();

    const rows = wrapper.findAll('tbody tr');
    expect(rows.length).toBe(mockRawMaterials.length);
    expect(wrapper.text()).toContain('Iron');
    expect(wrapper.text()).toContain('Copper');
  });

  it('shows the add form when "Add New Raw Material" button is clicked', async () => {
    const wrapper = mount(RawMaterials, { global });
    // Wait for the component to finish loading data and update the DOM
    await wrapper.vm.$nextTick();
    await wrapper.vm.$nextTick();

    expect(wrapper.find('.form-container').exists()).toBe(false);

    await wrapper.find('button.btn-primary').trigger('click');

    expect(wrapper.find('.form-container').exists()).toBe(true);
    expect(wrapper.find('h2').text()).toBe('Add Raw Material');
  });

  it('submits the form to add a new raw material and refreshes the list', async () => {
    const wrapper = mount(RawMaterials, { global });
    // Wait for the component to finish loading data and update the DOM
    await wrapper.vm.$nextTick();
    await wrapper.vm.$nextTick();

    // Show the form
    await wrapper.find('button.btn-primary').trigger('click');

    // Fill the form
    const newMaterial = { code: 'WD', name: 'Wood', stockQuantity: 200 };
    await wrapper.find('input#code').setValue(newMaterial.code);
    await wrapper.find('input#name').setValue(newMaterial.name);
    await wrapper.find('input#stockQuantity').setValue(newMaterial.stockQuantity);

    // Mock the second call to getRawMaterials after creation
    const updatedMaterials = [...mockRawMaterials, { id: 3, ...newMaterial }];
    api.getRawMaterials.mockResolvedValue(updatedMaterials);

    // Submit the form
    await wrapper.find('form').trigger('submit.prevent');

    // Check if createRawMaterial was called
    expect(api.createRawMaterial).toHaveBeenCalledWith(expect.objectContaining(newMaterial));

    // Check if the list was refreshed
    expect(api.getRawMaterials).toHaveBeenCalledTimes(2);
    
    // Wait for render updates
    await wrapper.vm.$nextTick();
    await wrapper.vm.$nextTick();

    // Check that the new material is in the table
    const rows = wrapper.findAll('tbody tr');
    expect(rows.length).toBe(3);
    expect(wrapper.text()).toContain('Wood');
  });
});
