// filepath: tailwind.config.js
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'nex-dark': 'var(--nexprod-azul-profundo)',
        'nex-success': 'var(--nexprod-verde-sucesso)',
        'nex-gold': 'var(--nexprod-dourado-ambar)',
        'nex-light': 'var(--nexprod-azul-light)',
        'nex-ice': 'var(--nexprod-branco-gelo)',
      },
    },
  },
  plugins: [],
}