import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    host: '0.0.0.0',  // 모든 IP에서 접근 가능
    port: 5173,  // 원래 포트 유지
    strictPort: true,
    hmr: {
      //host: '192.168.203.68', // PC 로컬 IP, HMR(WebSocket) 외부 접속용
      protocol: 'ws',           // WebSocket 사용
    },
    proxy: {
      '/api': {
        target: 'http://localhost:9090/',
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, ''), // 필요 시 주석 해제
      },
    },
    
  },
})
