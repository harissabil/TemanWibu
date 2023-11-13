# TemanWibu

<div align="center">
  <img src="https://github.com/harissabil/TemanWibu/blob/main/assets/logo/teman_wibu.png" width="480" alt="Centered Image">
</div>

TemanWibu is an Android-based application that facilitates users in managing their anime list, whether it’s planning to watch, currently watching, or having finished watching. This application allows users to add anime to their library and provide reviews and ratings based on their assessment. This application also allows you to see other people’s reviews so that users can get a variety of views on an anime from various perspectives.

## Table of Contents
- [Group Members](#group-members)
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
<table>
  <tbody>
    <tr>
      <td><img src="assets/screenshot/welcome.png?raw=true"/></td>
      <td><img src="assets/screenshot/login.png?raw=true"/></td>
      <td><img src="assets/screenshot/register.png?raw=true"/></td>
    </tr>
    <tr>
      <td><img src="assets/screenshot/home.png?raw=true"/></td>
      <td><img src="assets/screenshot/detail.png?raw=true"/></td>
      <td><img src="assets/screenshot/search.png?raw=true"/></td>
    </tr>
    <tr>
      <td><img src="assets/screenshot/library.png?raw=true"/></td>
      <td><img src="assets/screenshot/forum.png?raw=true"/></td>
      <td><img src="assets/screenshot/profile.png?raw=true"/></td>
    </tr>
  </tbody>
</table>

## Tech Stack

List of technologies used in this app.

- **Front-end**:
  - [Android](https://developer.android.com/)
  - [Kotlin](https://kotlinlang.org/)
  - [Jetpack Compose](https://developer.android.com/jetpack/compose)

- **Back-end**:
  - [PHP](https://www.php.net/)

- **Database**:
  - [PostgreSQL](https://www.postgresql.org/)

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
