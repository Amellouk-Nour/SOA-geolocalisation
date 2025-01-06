# Dockerfile.mongodb
FROM mongo:latest

# Copier le script d'initialisation
COPY mongo-init.js /docker-entrypoint-initdb.d/

# S'assurer que le script a les bonnes permissions
RUN chmod 755 /docker-entrypoint-initdb.d/mongo-init.js