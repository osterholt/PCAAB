clear
mvn install
mv ./target/jumpstart-jar-with-dependencies.jar ./lib/jumpstart-jar-with-dependencies.jar
./bin/run -Dconfig.file=src/main/resources/reference.conf