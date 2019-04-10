# A example project to demonstrate SpringBoot with Kafka

 ## Install and configure
 
 ### Download & install Kafka
 Link: https://kafka.apache.org/quickstart
 
 ### Start ZooKeeper
 > zookeeper-server-start.bat ..\..\config\zookeeper.properties
 
 ### Start the Kafka cluster locally
 > kafka-server-start.bat ..\..\config\server.properties
  
 ### Issue the post request
 > http://localhost:9000/api/publish?topic=new_post&message=HelloWorld!