# # Usa una versione più recente di Node.js
# FROM node:18 AS build

# # Imposta la working directory
# WORKDIR /app

# # Copia i file package.json e package-lock.json (se esiste) e installa le dipendenze
# COPY package*.json ./

# RUN npm install

# # Copia il resto dell'applicazione
# COPY . .

# # Compila l'applicazione Angular per la produzione
# RUN npm run build --prod

# # Usa Nginx per servire l'applicazione compilata
# FROM nginx:alpine AS production
# COPY --from=build /app/dist /usr/share/nginx/html


# Usa una versione più recente di Node.js
FROM node:18

# Imposta la working directory
WORKDIR /app

# Copia i file package.json e package-lock.json (se esiste) e installa le dipendenze
COPY package*.json ./
RUN npm install

# Copia il resto dell'applicazione
COPY . .

# Espone la porta su cui `ng serve` ascolterà
EXPOSE 4200

# Avvia il server di sviluppo Angular su tutte le interfacce di rete
CMD ["npm", "start", "--", "--host", "0.0.0.0"]