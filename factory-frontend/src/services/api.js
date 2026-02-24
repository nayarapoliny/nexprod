import axios from 'axios';

// Create an axios instance with a base URL.
// This is more convenient than repeating the URL everywhere.
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// --- Production Optimizer ---

export function fetchOptimizedProductionPlan() {
  // Using POST as it's an action that triggers a calculation
  return apiClient.post('/production/optimize');
}

// --- Raw Materials CRUD ---

export function getRawMaterials() {
  return apiClient.get('/raw-materials');
}

export function getRawMaterial(id) {
  return apiClient.get(`/raw-materials/${id}`);
}

export function createRawMaterial(data) {
  return apiClient.post('/raw-materials', data);
}

export function updateRawMaterial(id, data) {
  return apiClient.put(`/raw-materials/${id}`, data);
}

export function deleteRawMaterial(id) {
  return apiClient.delete(`/raw-materials/${id}`);
}

// --- Products CRUD ---

export function getProducts() {
  return apiClient.get('/products');
}

export function getProduct(id) {
  return apiClient.get(`/products/${id}`);
}

export function createProduct(data) {
  return apiClient.post('/products', data);
}

export function updateProduct(id, data) {
  return apiClient.put(`/products/${id}`, data);
}

export function deleteProduct(id) {
  return apiClient.delete(`/products/${id}`);
}

// Interceptor to handle responses and errors globally
apiClient.interceptors.response.use(
  (response) => {
    // Axios wraps the response data in a `data` property
    return response.data;
  },
  (error) => {
    // Log the error and re-throw it to be caught by the calling component
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error.response?.data || error.message);
  }
);
