Open a Terminal window and go to the root of the repository, then start the test environment by running:

```bash
docker-compose up -d
```

Connect to Kafka container dedicated to MirrorMaker:

```bash
docker exec -it mirror-maker /bin/bash
```

From the inside of the container, start MirrorMaker:

```bash
connect-mirror-maker /tmp/kafka/config/mm2.properties
```

If you get a FileNotFoundException, like

java.io.FileNotFoundException: /usr/bin/../config/connect-log4j.properties (No such file or directory)

don’t panic! If you want to remove that exception you just need to define the connect-log4j.properties file position in an environment variable as follow

```bash
export KAFKA_LOG4J_OPTS="-Dlog4j.configuration=file:/etc/kafka/connect-log4j.properties"
```

Open a new Terminal window and connect to a broker of the source cluster:

```bash
docker exec -it broker1A /bin/bash
```

Let’s create a topic “topic1” into both clusters:

```bash
kafka-topics --bootstrap-server broker1A:29092 --create --topic topic1 --partitions 3 --replication-factor 3
```

Open a new Terminal window, then connect to one of the broker in the target cluster

```bash
docker exec -it broker1B /bin/bash
```

create a topic “topic1”:

```bash
kafka-topics --bootstrap-server broker1B:29093 --create --topic topic1 --partitions 3 --replication-factor 3
```

and checkout the topics list into both clusters by running

```bash
kafka-topics --bootstrap-server broker1A:29092 --list
kafka-topics --bootstrap-server broker1B:29093 --list
```

From the broker1A Docker container, to which we connected at step 5, run a console-producer and send some sample events to topic1:

```bash
kafka-console-producer --bootstrap-server broker1A:29092 --topic topic1
```
