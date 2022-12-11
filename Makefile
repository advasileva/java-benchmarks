init:
	mvn archetype:generate \
          -DinteractiveMode=false \
          -DarchetypeGroupId=org.openjdk.jmh \
          -DarchetypeArtifactId=jmh-java-benchmark-archetype \
          -DgroupId=org.benchmark \
          -DartifactId=. \
          -Dversion=1.0

build:
	mvn clean verify

run:
	java -jar target/benchmarks.jar