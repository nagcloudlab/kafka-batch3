Create topic

```bash
bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic1
```

List topics

```bash
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

Describe topic

```bash
bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic topic1
```

Delete topic

```bash
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic topic1 --if-exists
```

Delete all topics

```bash
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic '*' --if-exists
```

create tpic with replication factor 3

```bash
bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic1 --partitions 3 --replication-factor 3
```

```

```
