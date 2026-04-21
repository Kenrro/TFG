SIP HUB

ℹ️ Descripción

Este proyecto es una aplicación basada en arquitectura de microservicios
desarrollada con Spring boot y react. Permite la gestión de usuarios, establecimientos, productos, incentivos y transacciones.

🏗️ Arquitectura

El sistema está dividido en varios microservicios independientes:

• Auth service
• Stablishment service
• Product service
• Incentive service
• Transaction service
• Frontend

Cada servicio vuenta con su propia base de datos PostgreSQL.

🐳 Tecnologías utilizadas

• Java + spring boot
• PostgreSQL
• React + vite
• Docker y docker compose
• JWT para autenticación
• Swagger para documentación de apis

🚀 Cómo ejecutar el proyecto

1. Requisitos

• Docker
• Docker compose

2. Clonar repositorio

git clone https://github.com/Kenrro/TFG.git

3. Ejecutar la aplicación

docker compose up

4. Acceso a la aplicación

Frontend:
hhtp://localhost:3000

APIs

• Auth Service → http://localhost:8081
• Stablishment Service → http://localhost:8080
• Product Service → http://localhost:8082
• Incentive Service → http://localhost:8083
• Transaction Service → http://localhost:8084

🔐Autenticación

El sistema utiliza JWT para la autenticación. Los tokens se generan en el Auth service y se utilizan en el resto de servicios.

📡Endpoints

Cada microservici expone su documentación en:

http://localhost:{puerto}/swagger-ui.html

Notas

• El proyecto está preparado para ejecutarse en entorno Docker.
• No es necesario instalar dependencias adicionales.
• Las bases de datos se crean automáticamente.

Kevin Misahel Zelaya Peña
TFG / SIP HUB