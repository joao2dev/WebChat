# 💬 Chat Web

Aplicação de chat em tempo real desenvolvida com **Java Spring Boot** e **WebSocket/STOMP**, com autenticação via **JWT** armazenado em cookie HttpOnly.

## 🚀 Tecnologias

- Java 17
- Spring Boot
- Spring Security + JWT
- WebSocket + STOMP
- PostgreSQL
- Flyway
- Docker

## 🏗️ Arquitetura

### Fluxo de Autenticação

Diagrama com o fluxo completo de login com Spring Security e o ciclo de requisições autenticadas via JWT.

![Fluxo de autenticação](docs/chatweb.drawio (1))

### Modelo do Banco de Dados

Entidades e relacionamentos do sistema.

![Modelo do banco de dados](docs/Modelo do Db.drawio (2))

**Entidades:**
- `User` — usuário da plataforma
- `Friendship` — relação de amizade entre dois usuários (status: PENDING, ACCEPTED)
- `Group` — grupo de conversa criado por um usuário
- `GroupMember` — relação N:N entre usuários e grupos
- `Conversation` — conversa direta (DM) entre dois usuários
- `GroupMessage` — mensagens enviadas em grupos
- `DirectMessage` — mensagens enviadas em conversas diretas

## ⚙️ Como rodar

> Em breve.

## 📄 Licença

MIT
