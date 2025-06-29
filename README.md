# 💬 LiveTalk - Real-Time Chat Platform

LiveTalk is a modern, scalable chat application built with **Spring Boot** and **WebSocket**, designed for seamless live communication. It supports real-time messaging, superchat (priority messages via payment), and room-based interactions ideal for **streamers**, **teachers**, and **community managers**.

[![Watch the Demo](https://img.youtube.com/vi/D3XPxYfJ3DE/0.jpg)](https://youtu.be/D3XPxYfJ3DE)

Click the image above to watch a short demo of **LiveTalk**, a real-time chat app powered by **WebSocket** and **RabbitMQ**.

---

## 🚀 Features

- 🔐 **Authentication** and User Sessions
- 💬 **Real-time Messaging** using WebSocket + RabbitMQ
- 🏠 **Room Creation & Joining**
- 🗳️ **Message Voting** (Likes)
- 💸 **Super Chat Support** with Stripe Integration
- 🛠️ **Admin Control** (Kick / Ban Users)
- 📊 **Scalable Architecture** using Microservices
- 🔁 **Versioning System** for MVP and future updates

---

## 🧠 Use Case

LiveTalk is perfect for:
- **Live Streamers** to interact with audiences
- **Teachers** for conducting online interactive sessions
- **Startup Teams** and **Communities** for private communication rooms

---

## 🛠 Tech Stack

| Layer            | Tech Used                              |
|------------------|----------------------------------------|
| Backend          | Spring Boot, WebSocket,RabbitMq, STOMP, 
|                  | REST API                               |
| Frontend         | HTML, CSS, JavaScript                  |
| Payment Gateway  | Stripe                                 |
| Database         | PostgreSQL  , MongoDB                  |
| Service Registry | Eureka (Spring Cloud Netflix)          |
| API Gateway      | Spring Cloud Gateway                   |
| Build Tool       | Maven                                  |
| Versioning       | Manual versioning (v1.0.0 format)      |

---

## 📂 Project Structure (Microservices Overview)

- `user-service` – Manages user data and login
- `chat-service` – Handles WebSocket chat logic
- `room-service` – Manages rooms (create/join)
- `notification-service` - send higher priority notification
- `moderation-service` - Manage room level moderation(Kick/Ban users)
- `gateway-service` – Routes requests to microservices
- `Eureka-service` – Eureka server for service discovery
- `payment-service` – Handles Stripe-based super chats

---

## 📦 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/LiveTalk.git
cd LiveTalk
