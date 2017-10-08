# YCSB_BASE
FROM debian:jessie
ENV MYSQL_DATABASE ycsb
ENV PATH "/usr/local/bin:$PATH"

# install Java (must specify version later)
RUN apt-get update && apt-get install -y \
	default-jdk \
	libmysql-java \
	maven \
	python \
	curl

# set YCSB version to use
ENV YCSB_VERSION 0.12.0

WORKDIR /
# fetch and extract YCSB source
RUN curl --progress-bar -sLo YCSB-${YCSB_VERSION}.tar.gz  https://github.com/brianfrankcooper/YCSB/archive/${YCSB_VERSION}.tar.gz
RUN tar -xvf YCSB-${YCSB_VERSION}.tar.gz
RUN rm YCSB-${YCSB_VERSION}.tar.gz
#ADD  https://github.com/brianfrankcooper/YCSB/archive/${YCSB_VERSION}.tar.gz .

# add workload files
ADD workloads /workloads
COPY db.properties /

# compile the source code
WORKDIR /YCSB-${YCSB_VERSION}

ARG type
COPY $type/DefaultDBFlavor.java /YCSB-${YCSB_VERSION}/jdbc/src/main/java/com/yahoo/ycsb/db/flavors/
COPY $type/JdbcDBClient.java /YCSB-${YCSB_VERSION}/jdbc/src/main/java/com/yahoo/ycsb/db/

RUN mv /YCSB-${YCSB_VERSION}/jdbc/src/test /YCSB-${YCSB_VERSION}/jdbc/src/.test

# Configure the jdbc pom file to include the json-simple library 
COPY pomDep.txt .
RUN sed -i "/<\/dependencies>/ s/.*/$(cat pomDep.txt | sed 's/\//\\\//g')\n&/" /YCSB-${YCSB_VERSION}/jdbc/pom.xml
RUN rm pomDep.txt

# compile everything
RUN mvn -pl com.yahoo.ycsb:jdbc-binding -am clean package
# extracts the compiled distribution of ycsb
RUN tar -xvf jdbc/target/ycsb-jdbc-binding-${YCSB_VERSION}.tar.gz
RUN mkdir /ycsb && mv ycsb-jdbc-binding-${YCSB_VERSION}/* /ycsb/
# copy the connector jar file to ycsb so it does not have to be specified when running ycsb
RUN cp /usr/share/java/mysql-connector-java.jar /ycsb/lib/
# or if the class path works use it instead
ENV CLASSPATH=/usr/share/java/mysql-connector-java.jar:$CLASSPATH

WORKDIR /
RUN rm -r /YCSB-${YCSB_VERSION}

COPY $type/InitDB.java .
RUN javac InitDB.java

RUN mv InitDB.class /ycsb/
RUN mv db.properties /ycsb/
RUN mv workloads/* /ycsb/workloads

WORKDIR /ycsb
# CMD java InitDB && ./bin/ycsb load jdbc -P workloads/workloada -P db.properties

