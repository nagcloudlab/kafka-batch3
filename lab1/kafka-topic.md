create tooic 'topic1' with 3 partitions and 3 replicas

```bash
./bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic topic1
```

describe topic 'topic1'

```bash
./bin/kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic topic1
```
