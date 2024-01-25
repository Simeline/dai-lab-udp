mvn clean package
===> Le fichier JAR sera créé dans le répertoire target.


docker build -t dai/musician ./pathToDockerfile

docker run -d -p 9904:9904/udp musician/image

mvn clean package
docker build -t dai/auditor ./pathToDockerfile
docker run -d -p 2205:2205 dai/auditor
nc localhost 2205
docker run -d dai/musician piano
docker logs <file>


sudo tcpdump -i en0 udp port 9904
sudo tcpdump -i eth0 udp port 9904

-- pour mac c'est en0 au lieu de eth0'
