deploy one zookeeper

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

verify zookeeper is running

```bash
bin/zookeeper-shell.sh localhost:2181
ls /
```
