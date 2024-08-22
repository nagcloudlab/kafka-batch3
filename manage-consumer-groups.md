list consumer groups

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```

describe a consumer group

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my-group
```

delete a consumer group

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --delete --group my-group
```
