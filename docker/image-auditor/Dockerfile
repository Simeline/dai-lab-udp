# Image de base
FROM openjdk:21-slim

# Création du répertoire de l'application
WORKDIR /app

# Copie du fichier JAR Musicien dans le conteneur
COPY target/Auditor-1.0-jar-with-dependencies.jar /app

# Exposition du port utilisé par l'application
EXPOSE 2205

# Commande d'exécution de l'application Auditeur
ENTRYPOINT ["java", "-jar", "Auditor-1.0-jar-with-dependencies.jar"]