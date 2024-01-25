mvn clean package
===> Le fichier JAR sera créé dans le répertoire target.


docker build -t dai/musician ./pathToDockerfile

docker run -d -p 9904:9904/udp musician-image


docker build -t dai/auditor ./pathToDockerfile
docker run -d -p 2205:2205 dai/auditor