config-processor
---

it reads configuration files in yaml, json, and properties format

for all tree-like configurations all configurations and resolved values are available for use,
for example


        service1:
          name: geo
          user: user1
          password: "super secret password"
        secret:
          create: true
          name: ${service1/name} 
          
getting resolved to           

        service1:
          name: geo
          user: user1
          password: "super secret password"
        secret:
          create: true
          name: geo
       
or can support multiple sources and tasks       
       
    java -jar target/config-processor-0.0.2-SNAPSHOT-jar-with-dependencies.jar \
            --release=release1 \
            --config=src/test/resources/configs \
            --task=src/test/resources/templates/values.yaml:target/values.yaml \
            --task=src/test/resources/templates/some.sql:target/some.sql
            
    cat   target/values.yaml
    cat   target/some.sql           
       
note that release name is optional, if not set then v1 is used

it can be also compiled down to native image and invoked like this:
       
        ./config-processor \
           --release=release1 \
           --config=src/test/resources/configs \
           --task=src/test/resources/templates/values.yaml:target/values.yaml \
           --task=src/test/resources/templates/some.sql:target/some.sql
        
        cat   target/values.yaml
        cat   target/some.sql


 native

![native](docs/native-timing.png)   

Java jar execution

![java jar](docs/java-timing.png)
