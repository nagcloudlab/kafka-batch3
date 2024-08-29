https://github.com/nagcloudlab/strimzi/blob/main/week-1/play-with-kafka/005-perf-test/01.md

```bash
bin/kafka-topics.sh \
--create \
--topic baz-topic \
--partitions 3 \
--replication-factor 3 \
--config retention.ms=86400000 \
--config min.insync.replicas=1 \
--bootstrap-server localhost:9092
```

```bash
bin/kafka-producer-perf-test.sh \
--topic baz-topic \
--num-records 3000000 \
--record-size 1024 \
--throughput -1 \
--producer-props \
    bootstrap.servers=localhost:9092
```

3000000 records sent, 54285.869388 records/sec (53.01 MB/sec), 557.32 ms avg latency, 1224.00 ms max latency, 472 ms 50th, 1027 ms 95th, 1118 ms 99th, 1176 ms 99.9th.

3000000 records sent, 74729.106987 records/sec (72.98 MB/sec), 366.78 ms avg latency, 1193.00 ms max latency, 294 ms 50th, 801 ms 95th, 978 ms 99th, 1139 ms 99.9th.
