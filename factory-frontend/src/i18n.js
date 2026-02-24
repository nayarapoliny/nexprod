import { createI18n } from 'vue-i18n';

// Define the translation messages
const messages = {
  en: {
    nav: {
      logoAlt: 'Nexprod Logo',
      optimizer: 'Optimizer',
      rawMaterials: 'Raw Materials',
      products: 'Products'
    },
    maximizer: {
      title: 'Profit Maximizer',
      description: 'Calculate the optimal production plan based on current raw material stock.',
      calculateButton: 'Calculate Optimal Plan',
      calculatingButton: 'Calculating...',
      loading: 'Analyzing stock and calculating plan...',
      error: 'An error occurred while calculating the plan. Please check the backend connection and try again.',
      resultsTitle: 'Recommended Production Plan',
      productColumn: 'Product',
      quantityColumn: 'Quantity to Produce',
      noProducts: 'No products could be produced with the current stock.'
    },
    rawMaterials: {
      title: 'Raw Materials Management',
      loading: 'Loading raw materials...',
      addNew: 'Add New Raw Material',
      error: {
        load: 'Error loading raw materials. Please check connection.',
        save: 'Error saving raw material.',
        delete: 'Error deleting raw material.'
      },
      table: {
        code: 'Code',
        name: 'Name',
        stock: 'Stock Quantity',
        actions: 'Actions'
      },
      actions: {
        edit: 'Edit',
        delete: 'Delete',
        confirmDelete: 'Are you sure you want to delete this material?'
      },
      form: {
        addTitle: 'Add Raw Material',
        editTitle: 'Edit Raw Material',
        codeLabel: 'Code',
        nameLabel: 'Name',
        stockLabel: 'Stock Quantity',
        save: 'Save',
        cancel: 'Cancel'
      }
    },
    products: {
      title: 'Products Management',
      loading: 'Loading products...',
      addNew: 'Add New Product',
      error: {
        load: 'Failed to load initial data.',
        save: 'Failed to save product.',
        delete: 'Failed to delete product.'
      },
      table: {
        code: 'Code',
        name: 'Name',
        value: 'Value',
        actions: 'Actions'
      },
      form: {
        addTitle: 'Add Product',
        editTitle: 'Edit Product',
        composition: 'Composition',
        addMaterial: 'Add Material'
      }
    }
  },
  pt: {
    nav: {
      logoAlt: 'Logo Nexprod',
      optimizer: 'Otimizador',
      rawMaterials: 'Matérias-Primas',
      products: 'Produtos'
    },
    maximizer: {
      title: 'Otimizador de Lucro',
      description: 'Calcule o plano de produção ideal com base no estoque de matéria-prima atual.',
      calculateButton: 'Calcular Plano Ideal',
      calculatingButton: 'Calculando...',
      loading: 'Analisando estoque e calculando plano...',
      error: 'Ocorreu um erro ao calcular o plano. Verifique a conexão com o back-end e tente novamente.',
      resultsTitle: 'Plano de Produção Recomendado',
      productColumn: 'Produto',
      quantityColumn: 'Quantidade a Produzir',
      noProducts: 'Nenhum produto pôde ser produzido com o estoque atual.'
    },
    rawMaterials: {
      title: 'Gestão de Matérias-Primas',
      loading: 'Carregando matérias-primas...',
      addNew: 'Adicionar Nova Matéria-Prima',
      error: {
        load: 'Erro ao carregar matérias-primas. Verifique a conexão.',
        save: 'Erro ao salvar matéria-prima.',
        delete: 'Erro ao excluir matéria-prima.'
      },
      table: {
        code: 'Código',
        name: 'Nome',
        stock: 'Qtd. em Estoque',
        actions: 'Ações'
      },
      actions: {
        edit: 'Editar',
        delete: 'Excluir',
        confirmDelete: 'Tem certeza que deseja excluir esta matéria-prima?'
      },
      form: {
        addTitle: 'Adicionar Matéria-Prima',
        editTitle: 'Editar Matéria-Prima',
        codeLabel: 'Código',
        nameLabel: 'Nome',
        stockLabel: 'Quantidade em Estoque',
        save: 'Salvar',
        cancel: 'Cancelar'
      }
    },
    products: {
      title: 'Gestão de Produtos',
      loading: 'Carregando produtos...',
      addNew: 'Adicionar Novo Produto',
      error: {
        load: 'Falha ao carregar dados iniciais.',
        save: 'Falha ao salvar produto.',
        delete: 'Falha ao excluir produto.'
      },
      table: {
        code: 'Código',
        name: 'Nome',
        value: 'Valor',
        actions: 'Ações'
      },
      form: {
        addTitle: 'Adicionar Produto',
        editTitle: 'Editar Produto',
        composition: 'Composição',
        addMaterial: 'Adicionar Material'
      }
    }
  }
};

// Create the i18n instance
const i18n = createI18n({
  legacy: false, // Desativa o modo legado para usar Composition API
  locale: 'en', // Set the default locale
  fallbackLocale: 'en', // Fallback locale
  messages, // Set the translation messages
});

export default i18n;
