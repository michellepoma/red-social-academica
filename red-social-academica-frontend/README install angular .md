# 🚀 Instalación de Angular

¡Bienvenido! Este documento te guiará paso a paso para instalar y configurar Angular en tu entorno de desarrollo local.  
Con Angular podrás construir potentes aplicaciones web modernas y escalables. 🌐✨

---

## 📦 Requisitos Previos

Antes de instalar Angular, asegúrate de tener lo siguiente instalado:

- [Node.js](https://nodejs.org/) (versión recomendada: **≥ 18.x**)
- [npm](https://www.npmjs.com/) (se instala junto con Node.js)

Verifica si ya están instalados:

```bash
node -v
npm -v

⚙️ Instalación del CLI de Angular
Instala Angular CLI globalmente con el siguiente comando:
npm install -g @angular/cli
Verifica que se instaló correctamente:
ng version

🛠️ Crear un Proyecto Nuevo
Crea tu primera aplicación Angular:
ng new mi-aplicacion

Ingresa al directorio de tu nueva app:
cd mi-aplicacion

Levanta el servidor local con:
ng serve

Abre tu navegador en:
👉 http://localhost:4200

🧪 Comandos Útiles

| Comando                        | Descripción               |
| ------------------------------ | ------------------------- |
| `ng serve`                     | Inicia servidor local     |
| `ng build`                     | Compila el proyecto       |
| `ng test`                      | Ejecuta pruebas unitarias |
| `ng generate component nombre` | Crea un nuevo componente  |
| `ng generate service nombre`   | Crea un nuevo servicio    |
| `ng generate module nombre`    | Crea un nuevo módulo      |
