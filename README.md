# Nexprod - Factory Management System

O **Nexprod** é um sistema de gestão fabril focado na otimização de produção e controle de inventário. Ele permite gerenciar matérias-primas, produtos e calcular o plano de produção ideal para maximizar o lucro com base no estoque disponível.

## 🚀 Tecnologias

### Frontend
- **Vue.js 3**: Utilizando a Composition API (`<script setup>`).
- **Vite**: Ferramenta de build ultra-rápida.
- **Tailwind CSS**: Framework de layout e estilização responsiva.
- **Vue I18n**: Suporte a multi-idiomas (Português e Inglês).
- **Axios**: Cliente HTTP para comunicação com a API.

### Backend
- **Java Spring Boot**: Framework principal.
- **Spring Data JPA**: Persistência de dados.
- **H2 Database**: Banco de dados em memória para desenvolvimento.

## 🛠️ Funcionalidades

1.  **Otimizador de Lucro**: Algoritmo que analisa o estoque de matérias-primas e sugere a quantidade ideal de produtos a serem fabricados.
2.  **Gestão de Matérias-Primas**: CRUD completo para controle de insumos e quantidades em estoque.
3.  **Gestão de Produtos**: Cadastro de produtos com definição de composição (quais matérias-primas são necessárias e em qual quantidade).
4.  **Layout Responsivo**: Interface adaptável para dispositivos móveis e desktops de alta resolução.

## 🎨 Identidade Visual (Paleta Nexprod)

O sistema utiliza uma paleta de cores personalizada definida no Tailwind:
- **Azul Profundo (`#1B263B`)**: Sidebar e textos principais.
- **Verde Sucesso (`#2D6A4F`)**: Botões de ação positiva.
- **Dourado/Âmbar (`#FFB703`)**: Alertas e destaques de valor.
- **Azul Light (`#8ECAE6`)**: Bordas e ícones secundários.
- **Branco Gelo (`#F8F9FA`)**: Fundo da aplicação.

## 🏁 Como Iniciar

### 1. Backend (Spring Boot)
Certifique-se de ter o JDK 17+ instalado.
```bash
# Na raiz do projeto
./mvnw spring-boot:run
```
O servidor iniciará em `http://localhost:8080`. Você pode acessar o console do banco de dados H2 em `/h2-console`.

### 2. Frontend (Vue.js)
Certifique-se de ter o Node.js instalado.
```bash
# Entre na pasta do frontend
cd factory-frontend

# Instale as dependências
npm install

# Inicie o servidor de desenvolvimento
npm run dev
```
O frontend estará disponível em `http://localhost:5173`.

## 🌍 Internacionalização (i18n)

O sistema suporta troca dinâmica de idiomas. As traduções estão localizadas em `src/i18n.js`.
- **EN**: English (Default)
- **PT**: Português

## 📱 Responsividade

O layout foi construído com uma abordagem **Mobile-First**:
- **Mobile**: Sidebar oculta com menu hambúrguer e tabelas com scroll horizontal.
- **Desktop**: Sidebar fixa à esquerda e contêineres centralizados com largura máxima de `1200px` a `1500px`.

---
Desenvolvido por Nayara como parte do projeto Nexprod.
