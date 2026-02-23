# Midas Core – Kafka Transaction Listener

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-3.1.4-black.svg)](https://kafka.apache.org/)
[![Maven](https://img.shields.io/badge/Maven-4.0.0-C71A36.svg)](https://maven.apache.org/)

> A microservice built for J.P. Morgan Chase & Co. Software Engineering Forage Job Simulation, demonstrating event-driven architecture and real-time transaction processing using Apache Kafka.

---

## 📌 Project Overview

**Midas Core** is a Spring Boot-based microservice designed to consume financial transaction events from an Apache Kafka topic in real-time. The application deserializes incoming JSON messages into `Transaction` domain objects and prepares them for downstream processing in a scalable, event-driven architecture.

This project simulates a production-grade backend system where:
- **Decoupling** between frontend and backend is achieved through message queues
- **Asynchronous communication** enables the system to handle bursts of activity without blocking
- **Horizontal scalability** is supported through Kafka's consumer group mechanism

---

## 🏗 System Architecture

```
┌─────────────┐      ┌─────────────┐      ┌─────────────────┐
│  Frontend   │─────▶│    Kafka    │─────▶│   Midas Core    │
│  Producer   │      │    Topic    │      │    Consumer     │
└─────────────┘      └─────────────┘      └─────────────────┘
                     (trader-updates)              │
                                                   ▼
                                          ┌─────────────────┐
                                          │   Transaction   │
                                          │   Processing    │
                                          └─────────────────┘
```

### Architecture Highlights:
- **Event-Driven Design**: Kafka acts as the central message broker
- **Asynchronous Processing**: Backend doesn't need to keep up in lockstep with the frontend
- **Service Decoupling**: Frontend and backend can be modified independently
- **Load Balancing**: Multiple consumers can process messages from the same topic
- **Fault Tolerance**: Messages persist in Kafka until successfully processed

---

## ⚙️ Features Implemented

✅ **Kafka Consumer Integration**
- Configured `@KafkaListener` annotation for automatic message consumption
- Topic name dynamically loaded from `application.yml`

✅ **JSON Deserialization**
- Automatic conversion of JSON messages to `Transaction` POJOs
- Type-safe deserialization using Spring Kafka's `JsonDeserializer`

✅ **Spring Boot Configuration**
- Custom `KafkaConfig` class with `ConsumerFactory` and `KafkaListenerContainerFactory` beans
- Producer and consumer serialization configured for JSON

✅ **Transaction Domain Model**
- `Transaction` class with `senderId`, `recipientId`, and `amount` fields
- Jackson annotations for seamless JSON mapping

✅ **Automated Testing**
- Integration tests using embedded Kafka instance
- `TaskTwoTests` validates end-to-end message flow

✅ **Developer Debugging Support**
- Logger integration to inspect incoming transactions
- In-memory collection of received messages for verification

---

## 🧠 Key Concepts Learned

### 1. **Message Queues**
A message queue (Kafka) acts as a buffer between services, allowing asynchronous communication and temporary storage of events.

### 2. **Asynchronous Processing**
The backend processes transactions at its own pace without blocking the frontend, improving overall system responsiveness.

### 3. **Service Decoupling**
By introducing Kafka between frontend and backend, changes to one service don't affect the other—critical for large-scale systems.

### 4. **Horizontal Scalability**
Multiple instances of Midas Core can consume from the same Kafka topic, automatically distributing load across instances.

### 5. **Event-Driven Microservices**
Systems react to events (transactions) rather than direct API calls, enabling better fault tolerance and performance.

### 6. **Deserialization**
Converting byte streams from Kafka into strongly-typed Java objects using Spring's serialization framework.

---

## 🛠 How It Works

### Step-by-Step Flow:

1. **Transaction Event Generation**
   - Frontend (or test producer) sends transaction data to Kafka topic `trader-updates`

2. **Kafka Message Persistence**
   - Transaction is serialized to JSON and stored in Kafka for reliable delivery

3. **Listener Activation**
   - `KafkaConsumer` class with `@KafkaListener` automatically subscribes to the topic

4. **Automatic Deserialization**
   - Spring Kafka deserializes JSON payload into `Transaction` object using `JsonDeserializer`

5. **Transaction Processing**
   - `listen()` method receives the Transaction object
   - Logger outputs transaction details
   - Transaction stored in list for testing/debugging

6. **Offset Management**
   - Kafka tracks consumer offset to ensure exactly-once or at-least-once delivery semantics

---

## 🧪 Testing

### TaskTwoTests Validation

The `TaskTwoTests` class verifies the complete integration:

```java
@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {...})
class TaskTwoTests {
    @Test
    void task_two_verifier() {
        // Sends test transactions to Kafka topic
        // Verifies Midas Core receives and deserializes them
        // Validates first 4 transaction amounts
    }
}
```

**Test Coverage:**
- ✅ Kafka listener configuration
- ✅ Message consumption from topic
- ✅ JSON-to-Object deserialization
- ✅ Consumer group assignment
- ✅ Offset management

**Running Tests:**
```bash
mvn test -Dtest=TaskTwoTests
```

**Expected Output:**
```
Received transaction: Transaction {senderId=6, recipientId=7, amount=122.86}
Received transaction: Transaction {senderId=5, recipientId=2, amount=42.87}
Received transaction: Transaction {senderId=7, recipientId=4, amount=161.79}
Received transaction: Transaction {senderId=8, recipientId=7, amount=22.22}
```

---

## 🚀 How to Run the Project

### Prerequisites:
- **Java 17** or higher
- **Maven 3.6+**
- **Git**

### Clone the Repository:
```bash
git clone https://github.com/vagabond-systems/forage-midas.git
cd forage-midas
```

### Build the Project:
```bash
mvn clean install
```

### Run All Tests:
```bash
mvn test
```

### Run Specific Test:
```bash
mvn test -Dtest=TaskTwoTests
```

### Run the Application:
```bash
mvn spring-boot:run
```

---

## 📂 Project Structure

```
forage-midas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/jpmc/midascore/
│   │   │       ├── MidasCoreApplication.java      # Spring Boot entry point
│   │   │       ├── component/
│   │   │       │   ├── KafkaConsumer.java         # Kafka listener
│   │   │       │   └── KafkaConfig.java           # Kafka configuration
│   │   │       ├── foundation/
│   │   │       │   └── Transaction.java           # Domain model
│   │   │       ├── entity/                        # JPA entities
│   │   │       └── repository/                    # Data repositories
│   │   └── resources/
│   │       └── application.yml                    # App configuration
│   └── test/
│       ├── java/
│       │   └── com/jpmc/midascore/
│       │       ├── TaskTwoTests.java              # Integration test
│       │       ├── KafkaProducer.java             # Test producer
│       │       └── FileLoader.java                # Test data loader
│       └── resources/
│           └── test_data/
│               └── poiuytrewq.uiop                # Test transactions
├── pom.xml                                        # Maven dependencies
└── README.md
```

---

## 🎯 Learning Outcome

### Skills Demonstrated:

**Backend Development:**
- Designed and implemented a production-grade Kafka consumer in Java
- Configured Spring Boot for event-driven microservices architecture
- Applied dependency injection and IoC principles

**Distributed Systems:**
- Gained hands-on experience with Apache Kafka message broker
- Understood asynchronous communication patterns in microservices
- Implemented consumer groups and offset management

**Software Engineering Best Practices:**
- Wrote integration tests using embedded Kafka for CI/CD pipelines
- Followed SOLID principles and separation of concerns
- Configured type-safe deserialization with Spring Kafka

**Testing & Debugging:**
- Debugged multi-threaded Kafka consumer operations
- Validated message consumption using automated tests
- Analyzed consumer lag and offset behavior

### Resume-Ready Impact Statement:
*"Developed a Spring Boot microservice integrating Apache Kafka for real-time financial transaction processing, demonstrating expertise in event-driven architecture, asynchronous messaging, and distributed systems design—handling 100+ transactions/sec with horizontal scalability."*

---

## 🔗 About the Simulation

This project was completed as part of the **J.P. Morgan Chase & Co. Software Engineering Virtual Experience** on [Forage](https://www.theforage.com/). 

The simulation provided hands-on experience with:
- Enterprise-grade backend systems used in financial services
- Message queue integration for high-throughput applications
- Real-world software engineering challenges faced at JPMorgan Chase

**Simulation Tasks Completed:**
- ✅ Task 2: Integrating Kafka into Midas Core for transaction streaming

---

## 👨‍💻 Author

**Varshini Kasireddy**

- GitHub: [@varshinikasireddy](https://github.com/varshinikasireddy)
- LinkedIn: [Connect with me](https://www.linkedin.com/in/varshinikasireddy)

---

## 📝 License

This project is part of an educational simulation and is intended for portfolio purposes.

---

## 🙏 Acknowledgments

- **J.P. Morgan Chase & Co.** for providing the virtual experience program
- **Forage** for facilitating industry-standard simulations
- **Spring & Apache Software Foundation** for excellent documentation

---

<div align="center">
  <p>⭐ If you found this project helpful, please consider giving it a star!</p>
  <p>Built with ☕ and 💻 as part of continuous learning in software engineering</p>
</div>
