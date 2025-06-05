Instala Node.js
Asegúrate de tener instalada una versión LTS de Node.js (≥18), que ya incluye npm. Puedes descargarla de
https://nodejs.org/

Instala el Angular CLI
Abre una terminal y ejecuta:
npm install -g @angular/cli
Esto instala la herramienta ng de forma global.

Comprueba la instalación
Verifica las versiones:


node --version
npm --version
ng version


EN MI CASO ESATSA SONA LAS VERSIONES
C:\Users\DIAZ>node --version
v22.15.0

C:\Users\DIAZ>npm --version
10.9.2

C:\Users\DIAZ>ng version

     _                      _                 ____ _     ___
    / \   _ __   __ _ _   _| | __ _ _ __     / ___| |   |_ _|
/ △ \ | '_ \ / _` | | | | |/ _` | '__|   | |   | |    | |
/ ___ \| | | | (_| | |_| | | (_| | |      | |___| |___ | |
/_/   \_\_| |_|\__, |\__,_|_|\__,_|_|       \____|_____|___|
|___/


Angular CLI: 19.2.13
Node: 22.15.0
Package Manager: npm 10.9.2
OS: win32 x64

Angular:
...

Package                      Version
------------------------------------------------------
@angular-devkit/architect    0.1902.13 (cli-only)
@angular-devkit/core         19.2.13 (cli-only)
@angular-devkit/schematics   19.2.13 (cli-only)
@schematics/angular          19.2.13 (cli-only)


C:\Users\DIAZ>



LUEGO PARA EJECUTAR ;
cd mi-proyecto
npm install      # instala las dependencias
ng serve         # compila y levanta el servidor de desarrollo
Abre tu navegador
Ve a
http://localhost:4200
