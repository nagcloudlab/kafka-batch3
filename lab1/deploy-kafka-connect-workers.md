deploy kafka connect worker-1

```bash
bin/connect-distributed.sh config/connect-distributed-worker-1.properties
```

deploy kafka connect worker-2

```bash
bin/connect-distributed.sh config/connect-distributed-worker-2.properties
```

get kafka connect worker status

```bash
curl http://localhost:8083/
```

get all connectors

```bash
curl http://localhost:8083/connectors
```

deploy file source connector

```bash
curl -X POST -H "Content-Type: application/json" --data '{
  "name": "file-source-connector",
  "config": {
    "connector.class": "org.apache.kafka.connect.file.FileStreamSourceConnector",
    "tasks.max": "1",
    "file": "/Users/nag/kafka-batch3/file1.txt",
    "topic": "test-topic"
  }
}' http://localhost:8083/connectors
```

get connector status

```bash
curl http://localhost:8083/connectors/file-source-connector/status
```

delete connector

```bash
curl -X DELETE http://localhost:8083/connectors/file-source-connector
```

deploy file sink connector

```bash
curl -X POST -H "Content-Type: application/json" --data '{
  "name": "file-sink-connector",
  "config": {
    "connector.class": "org.apache.kafka.connect.file.FileStreamSinkConnector",
    "tasks.max": "1",
    "file": "/Users/nag/kafka-batch3/file2.txt",
    "topics": "test-topic"
  }
}' http://localhost:8083/connectors
```

get connector status

```bash
curl http://localhost:8083/connectors/file-sink-connector/status
```
