./config-processor \
   --release=release1 \
   --config=src/test/resources/configs \
   --task=src/test/resources/templates/values.yaml:target/values.yaml \
   --task=src/test/resources/templates/some.sql:target/some.sql

cat   target/values.yaml
cat   target/some.sql
