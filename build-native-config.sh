$JAVA_HOME/bin/java -agentlib:native-image-agent=config-output-dir=target/graal-cfg  \
   -jar target/config-processor-0.0.2-SNAPSHOT-jar-with-dependencies.jar \
   --release=release1 \
   --config=src/test/resources/configs \
   --task=src/test/resources/templates/values.yaml:target/values.yaml \
   --task=src/test/resources/templates/some.sql:target/some.sql
