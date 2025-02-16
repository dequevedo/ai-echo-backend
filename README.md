### About
This is a personal project that I created in order to improve my skills. It is a backend for an App that can create and visualize Dungeons & Dragons character sheets (**WIP**).
Also, it`s a great way to remember how things works and apply it to other projects.

### ✅ Some features of this project
1. Factory and Strategy patterns
2. Docker and Docker Compose
3. Spring Batch
4. Python script that generates a .csv file with N characters
5. Deploy application on AWS EC2
6. (WIP) Jenkins
7. (TODO) Sync and Async functions
8. (TODO) Expose the (micro?)service on some API Gateway
9. (TODO) Implement something that uses a message broker (Maybe kafka)
10. (TODO) SNS
11. (TODO) SQS
12. (TODO) Endpoint to check progress on batch import
13. (TODO) Read file from Bucket S3 and import using Spring Batch
14. (TODO) OpenAPI documentation
15. (TODO) Unit Testing (Parameterized Tests)
16. (TODO) ChatGPT integration (Maybe for image, description generation)
17. (TODO) Custom annotations
18. (TODO) Consumer and Supplier

### Requirements to run this project locally
1. Java SDK 19
2. Docker

### Running Docker containers
1. Access the folder where docker-compose.yml is located
2. Run the command:
```
docker compose up -d 
```
### Connecting to Postgres using PGAdmin
1. Access PGAdmin on any web browser using the address: http://localhost:5050/
2. Create a server with the following configuration:
```
Name: Any Name
Host name/address: host.docker.internal
Port: 5432
Username: postgres
Password: changeme
```

### 💡 Testing Spring Batch by importing a local .csv file
In order to test the spring batch using a local file, I included a python script 
that generates Characters and stores them into a .csv file which can be imported by the application.

### 💡 Testing Spring Batch by importing a file from Bucket S3
In order to test the spring batch using S3, ... WIP

### 📖 Developer`s Notes
🟢 Why should we use **Spring Batch**?
1. Spring Batch is ideal for processing large volumes of data. 
2. State Management and Transactions: It provides resources to control transactions and avoid data loss.
3. Recovering: In case of an error it can recover from the last control point.
4. Chunks: The usage of chunks avoids high memory consumption

🟢 SOLID Principles (WIP)

* S 
* O 
* L 
* I 
* D 

🟢 Useful EC2 Commands
Connecting to an EC2 instance
```
ssh ec2-user@{ec2-ip}
```

Updating dependencies
```
sudo yum update
```

Installing Java
```
sudo yum install java-17
```

Packaging application into a .jar file
```
mvn clean package
```

Uploading .jar to EC2
```
scp target/poc-spring-boot-0.0.1-SNAPSHOT.jar ec2-user@{ec2-ip}:/home/ec2-user
```

Executing .jar from inside EC2
```
java -jar poc-spring-boot-0.0.1-SNAPSHOT.jar
```

Startup script
```
#!/bin/bash
cd /home/ec2-user
aws s3 cp s3://{bucket-name}/poc-spring-boot-0.0.1-SNAPSHOT.jar .
java -jar poc-spring-boot-0.0.1-SNAPSHOT.jar
```

Uploading .jar to S3
```
s3 cp target/poc-spring-boot-0.0.1-SNAPSHOT.jar s3://{bucket-name}/poc-spring-boot-0.0.1-SNAPSHOT.jar
```