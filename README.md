## JAK #1 CloudNative Micronaut

### Running

Start service with:

> ./gradlew run

Entrypoint class is *com.photomath.roundtable.Application*

# Configuration

Micronaut standard configuration is src/main/resources/application.yml

Sorting service URL endpoint list can be passed in environment variable *SORTING_URLS*.
URLs should be comma separated i.e. SORTING_URLS=http://ss1/v1/,http://ss2/v1/

### Building

> ./gradlew assemble

### Running Fat JAR

> java -jar "build/libs/JAK Micronaut-0.1-all.jar"

### Stress test

Sample locust stress test file is included. Start test with:

> locust --headless -t 15m -u 200 -r 10 -H http://localhost:8080

### Prometheus Metrics

Prometheus metrics are provided at http://localhost:8080/prometheus

### Logging

For logging, we use logback (src/main/resources/_logback.xml), writing to *roundtable.log* and standard output.  