install maven on ubuntu

```bash
sudo apt-get install maven
```

run java producer

```bash
cd /path/to/kafka-clients
mvn compile exec:java -Dexec.mainClass="com.example.ProducerClient"
```
