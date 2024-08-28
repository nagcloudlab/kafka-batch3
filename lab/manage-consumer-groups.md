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

reset offsets earliest for a consumer group

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-group --reset-offsets --to-earliest --execute --topic my-topic
```

reset offsets latest for a consumer group

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-group --reset-offsets --to-latest --execute --topic my-topic
```

reset offsets to a specific offset for a consumer group

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-group --reset-offsets --to-offset 1234 --execute --topic my-topic
```

reset offsets to a specific timestamp for a consumer group

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-group --reset-offsets --to-datetime 2019-01-01T12:00:00.000 --execute --topic my-topic
```

reset offsets to a specific timestamp for a consumer group

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-group --reset-offsets --by-duration PT30M --execute --topic my-topic
```
