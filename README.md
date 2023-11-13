# TemanWibu

<div align="center">
  <img src="https://github.com/harissabil/TemanWibu/blob/main/assets/logo/teman_wibu.png" width="480" alt="Centered Image">
</div>

TemanWibu is an Android-based application that facilitates users in managing their anime list, whether it’s planning to watch, currently watching, or having finished watching. This application allows users to add anime to their library and provide reviews and ratings based on their assessment. This application also allows you to see other people’s reviews so that users can get a variety of views on an anime from various perspectives.

## Table of Contents
- [Gorup Members](#group-members)
- [Screenshot](#screenshot)
- [Tech Stack](#tech-stack)
- [Setup](#setup)

## Group Members

| Name | Student Number |
| :-------- | :------- | 
[Muhammad Haris Sabil Al Karim](https://github.com/harissabil) | G6401221003 |
[Nurcahya Priantoro](https://github.com/)| G6401221049 |
[Raihana Luthfia](https://github.com/)| G6401221054 |
[Shafaya Sasikirana](https://www.github.com/)| G6401221092 |

## Screenshot
| Welcome Screen | Login Screen | Register Screen |
| --- | --- | --- | 
| ![](assets/screenshot/welcome.png?raw=true) | ![](assets/screenshot/login.png?raw=true) | ![](assets/screenshot/register.png?raw=true) |

| Home Screen | Detail Screen | Search Screen |
| --- | --- | --- | 
| ![](assets/screenshot/home.png?raw=true) | ![](assets/screenshot/detail.png?raw=true) | ![](assets/screenshot/search.png?raw=true) |

| Library Screen | Forum Screen | Profile Screen |
| --- | --- | --- | 
| ![](assets/screenshot/library.png?raw=true) | ![](assets/screenshot/forum.png?raw=true) | ![](assets/screenshot/profile.png?raw=true) |

## Tech Stack

List of technologies used in this app.

- Front-end
  - Android
  - Kotlin
  - Jetpack Compose

- Back-end
  - PHP

- Database
  - PostgreSQL

## Setup
1. **Setting up PostgreSQL Database**:
   - Install PostgreSQL from [PostgreSQL's official website](https://www.postgresql.org/).
   - Execute queries in [table.sql](./sql/table.sql) to create necessary tables for the application.

2. **Backend Configuration**:
   - Edit the `config.php` file in the backend folder with your PostgreSQL database configuration.

3. **Hosting the Backend**:
   - For local hosting, use tools like XAMPP or Laragon. Place backend files within the server directory.
   - For remote hosting, use a web hosting service and upload backend files.

4. **Android Project Setup**:
   - Open the Android project in Android Studio.
   - Create a `local.properties` file in the project root folder if it doesn’t exist.
   - Add `BACKEND_URL = "your_backend_url_here"` in the `local.properties` file.

5. **Building and Running the App**:
   - Build and run the app on an Android emulator or a physical Android device.
