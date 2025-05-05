This is a High-performance scaled Game Engine

# 🧠 GameEngine – Scalable Game Scoring & Insights Backend

A modular, production-grade game scoring and user interaction system built with **Java 21**, **Spring Boot**, **MySQL**, and **Redis**, featuring LLM integration for real-time insights, intelligent user interaction, and scalable performance.

> This is not a game engine — it’s an enterprise-level backend solution for managing game scores, stats, and interactions across sessions and users.

---

## 🔧 Tech Stack

- 💻 **Java 21**
- 🚀 **Spring Boot 3**
- 🐬 **MySQL** – Persistent relational data store
- 🧠 **Redis** – High-speed caching and session storage
- 🧊 **Docker** – Containerized microservice environment
- 🤖 **LLM (OpenAI/GPT)** – Smart insights, chat interactions
- 🧪 **JUnit** – Test coverage for endpoints and services
- 📦 **Maven** – Build & dependency management

---

## 📦 Features

- 🎯 **Accurate Score Tracking**: Store and analyze player performance
- 🗂️ **User Profiles**: Manage users and their info [MySQL]
- 🧠 **LLM Integration**: 
  - Game feedback
  - Answer player queries using game context
- 🧰 **REST API**: Clean, extensible endpoints for integration
- ⚡ **Caching**: Redis-backed optimization for high performance

- **Important Metrics** :API Response time **<17ms**, Redis interaction **<2ms**. Can manage **134k+ concurrent games**.
---

## 🐳 Run with Docker

```bash
# Clone the repository
git clone https://github.com/KeshavSM10/GameEngine.git
cd GameEngine

# Build and run containers
docker-compose up --build
