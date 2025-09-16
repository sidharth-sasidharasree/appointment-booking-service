# Appointment Booking System

## Overview
The **Appointment Booking System** is a full-stack application designed to help businesses manage appointments efficiently. It provides features such as user authentication, profile management, and appointment CRUD operations, allowing businesses and clients to interact seamlessly.

The system is built using **Angular** for the frontend and **Spring Boot (Java 21)** for the backend. Docker configurations are included for both frontend and backend to enable easy deployment.

---

## Tech Stack
- **Frontend:** Angular  
- **Backend:** Spring Boot (Java 21)  
- **Database:** H2 / MySQL / PostgreSQL (configurable)  
- **Authentication:** JWT-based login & registration  
- **Containerization:** Docker for frontend and backend  
- **Build Tools:** Maven / npm  

---

## Features
- **User Authentication:** Registration and login functionality with secure password storage.  
- **Profile Management:** Users can view and update their profiles.  
- **Appointment Management:**  
  - Create, Read, Update, Delete (CRUD) operations for appointments.  
  - Schedule appointments with date and time.  
- **Admin Functionality:** Manage users and appointments.  
- **Dockerized Deployment:** Frontend and backend run independently in containers.  

---

## Architecture
[Angular Frontend] <--> [Spring Boot Backend] <--> [Database]
|
v
Docker Containers
