# MacsCode

Welcome to **MacsCode**! This project is designed for the Programming Methodologies and Abstractions course, providing a platform where users can solve programming problems, submit their solutions, and engage in discussions.

## Project Structure

The project is organized into multiple services, each with its own purpose:


### Directory Overview

- **auth-service**: Contains code and configuration for user authentication and authorization services.

- **discussion-service**: Manages user discussions and comment.

- **docker**: Docker-related configurations and files.
    - `docker-compose.yml`: Defines services, networks, and volumes for Docker containers.
- **executor**: Handles code execution and grading of user-submitted solutions.
    - Contains necessary files and configurations to run code in a secure, isolated environment.
- **macscode-admin**: Spring Admin interface for microservices.
- **macscode-ui**: The frontend UI for the application.

- **problems**: Service for managing and retrieving problem statements and metadata.
- **problemset**: Contains problem definitions for methodologies and abstractions.
    - Includes files like `problem_migrations.py` for managing problem data migrations.

## Run with one command

### Prerequisites

- Docker
- Docker Compose

 **Clone the repository**:
```bash
git clone https://github.com/yourusername/leetcode-like-app.git
cd docker
docker-compose up -d
```