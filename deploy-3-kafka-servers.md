create 3 kafka servers with different broker.id, listeners, log.dirs, zookeeper.connect

```bash
cat <<EOF > config/server-101.properties
broker.id=101
listeners=PLAINTEXT://:9092
log.dirs=/tmp/kafka-logs-101
zookeeper.connect=localhost:2181
EOF

cat <<EOF > config/server-102.properties
broker.id=102
listeners=PLAINTEXT://:9093
log.dirs=/tmp/kafka-logs-102
zookeeper.connect=localhost:2181
EOF

cat <<EOF > config/server-103.properties
broker.id=103
listeners=PLAINTEXT://:9094
log.dirs=/tmp/kafka-logs-103
zookeeper.connect=localhost:2181
EOF

```

start 3 kafka servers

```bash
bin/kafka-server-start.sh config/server-101.properties
bin/kafka-server-start.sh config/server-102.properties
bin/kafka-server-start.sh config/server-103.properties
```
