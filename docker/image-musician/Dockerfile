# Image de base
FROM openjdk:21-slim

# Création du répertoire de l'application
WORKDIR /app

# Copie du fichier JAR Musicien dans le conteneur
COPY target/Musician-1.0-jar-with-dependencies.jar /app/app.jar

# Exposition du port utilisé par l'application
EXPOSE 9904/udp

# Commande d'exécution de l'application Musicien
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
