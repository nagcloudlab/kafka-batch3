# Kafka Monitoring using jmx_prometheus_exporter, Prometheus and Grafana

---

download the jmx_prometheus_exporter latest jar file from the below link

Ref: https://github.com/prometheus/jmx_exporter?tab=readme-ov-file

```bash
curl -O https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/1.0.1/jmx_prometheus_javaagent-1.0.1.jar
```

Create a configuration file for the jmx_prometheus_exporter
jmx_exporter_kafka_broker.yml ( )

<!-- zookeeper -->

```bash
./bin/zookeeper-server-start.sh config/zookeeper.properties
```

update kafka-run-class.sh file to add the below line for KAFKA_JMX_OPTS

```bash
  KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -Djava.net.preferIPv4Stack=true -javaagent:/home/me/jmx_prometheus_javaagent-1.0.1.jar=$JMX_PROMETHEUS_PORT:/home/me/jmx_exporter_kafka_broker.yml"
```

start the kafka broker-1 with the below command

```bash
export JMX_PROMETHEUS_PORT=7071

./bin/kafka-server-start.sh config/kraft/server-1.properties
```

start the kafka broker-2 with the below command

```bash
export JMX_PROMETHEUS_PORT=7072

./bin/kafka-server-start.sh config/kraft/server-2.properties
```

start the kafka broker-3 with the below command

```bash
export JMX_PROMETHEUS_PORT=7073

./bin/kafka-server-start.sh config/kraft/server-3.properties
```

install the prometheus-server in ubuntu

https://www.notion.so/nagcloudlab/Install-Prometheus-b50e13a36bb34d948b9c1f21e162fbf1

Update Prometheus file, to scrap metrics from JMX ports

```bash
sudo nano /etc/prometheus/prometheus.yml
```

```properties
  - job_name: 'kafka'
    static_configs:
    - targets: ['localhost:7071', 'localhost:7072', 'localhost:7073']
```

```bash
sudo systemctl restart prometheus
```

install the grafana-server in ubuntu

https://www.notion.so/nagcloudlab/Install-Grafana-cbdb2927eda24d9592bd34b447eb4625

Configure Provisioning and Dashboards
Create the Provisioning Directory:

```sh
sudo mkdir -p /etc/grafana/provisioning/datasources
sudo nano /etc/grafana/provisioning/datasources/datasource.yml
```

datasource.yml

```yml
# config file version
apiVersion: 1

# list of datasources that should be deleted from the database
deleteDatasources:
  - name: Prometheus
    orgId: 1

# list of datasources to insert/update depending on
# what's available in the database
datasources:
  # <string, required> name of the datasource. Required
  - name: Prometheus
    # <string, required> datasource type. Required
    type: prometheus
    # <string, required> access mode. proxy or direct (Server or Browser in the UI). Required
    access: proxy
    # <int> org id. will default to orgId 1 if not specified
    orgId: 1
    # <string> url
    url: http://localhost:9090
    # <string> database password, if used
    password:
    # <string> database user, if used
    user:
    # <string> database name, if used
    database:
    # <bool> enable/disable basic auth
    basicAuth:
    # <string> basic auth username
    basicAuthUser:
    # <string> basic auth password
    basicAuthPassword:
    # <bool> enable/disable with credentials headers
    withCredentials:
    # <bool> mark as default datasource. Max one per org
    isDefault: true
    # <map> fields that will be converted to json and stored in json_data
    jsonData:
      httpMethod: POST
      manageAlerts: true
      prometheusType: Prometheus
      prometheusVersion: 2.47.0
      cacheLevel: "High"
      tlsAuth: false
      tlsAuthWithCACert: false
    # <string> json object of data that will be encrypted.
    secureJsonData:
      tlsCACert: "..."
      tlsClientCert: "..."
      tlsClientKey: "..."
    version: 1
    # <bool> allow users to edit datasources from the UI.
    editable: true
```

```sh
sudo mkdir -p /etc/grafana/provisioning/dashboards
sudo nano /etc/grafana/provisioning/dashboards/kafka.yml
```

kafka.yml

```yml
apiVersion: 1

providers:
  - name: "default"
    orgId: 1
    folder: ""
    type: file
    disableDeletion: false
    editable: true
    updateIntervalSeconds: 3 #how often Grafana will scan for changed dashboards
    options:
      path: /var/lib/grafana/dashboards
```

place sample dashboards ( dashboards directory) in /var/lib/grafana/dashboards directory
restart grafana server
