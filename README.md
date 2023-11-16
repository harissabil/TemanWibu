# TemanWibu

<div align="center">
  <img src="https://github.com/harissabil/TemanWibu/blob/main/assets/logo/teman_wibu.png" width="480" alt="Centered Image">
</div>

TemanWibu is an Android-based application that facilitates users in managing their anime list, whether it’s planning to watch, currently watching, or having finished watching. It allows users to add anime to their library and provide reviews and ratings based on their assessment. This application also allows users to see other people’s reviews so that they can get a variety of views on an anime from various perspectives. The application utilizes the [Jikan API](https://github.com/jikan-me/jikan) for fetching anime information data, ensuring up-to-date and comprehensive details for users.

This project was created to fulfill the final assignment for the Database subject at IPB University. Its purpose is to demonstrate the understanding and application of database concepts and showcase proficiency in the technologies used in the development process. Feel free to explore the codebase to learn more about the implementation and functionalities.

## Table of Contents
- [Group Members](#group-members)
- [Screenshots](#screenshots)
- [Tech Stacks](#tech-stacks)
- [Setup](#setup)

## Group Members
| Name | Student Number |
| :-------- | :------- | 
[Muhammad Haris Sabil Al Karim](https://github.com/harissabil) | G6401221003 |
[Nurcahya Priantoro](https://github.com/Cahyo23042004)| G6401221049 |
[Raihana Luthfia](https://github.com/raihanaluthfia)| G6401221054 |
[Shafaya Sasikirana](https://www.github.com/fayyaas)| G6401221092 |

## Screenshots
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

## Tech Stacks
List of technologies used in this app.

- **Front-end**:
  - Platform: [Android](https://developer.android.com/)
  - Language: [Kotlin](https://kotlinlang.org/)
  - UI Framework: [Jetpack Compose](https://developer.android.com/jetpack/compose)

- **Back-end**:
  - Language: [PHP](https://www.php.net/)

- **Database**:
  - DBMS: [PostgreSQL](https://www.postgresql.org/)

## Setup
1. **Download or Clone the Project**:
   - To download, click on the "Code" button, then select "Download ZIP".
   - To clone, use the following Git command: `git clone https://github.com/harissabil/TemanWibu.git`.
2. **Setting up PostgreSQL Database**:
   - Install PostgreSQL from [PostgreSQL's official website](https://www.postgresql.org/).
   - Create a new database using a PostgreSQL client (e.g., pgAdmin, psql).
   - Execute queries in [table.sql](./sql/table.sql) to create necessary tables for the application.

3. **Backend Configuration**:
   - Edit the `config.php` file in the backend folder with your PostgreSQL database configuration.

4. **Hosting the Backend**:
   - For local hosting, use tools like XAMPP or Laragon. Place backend files within the server directory.
   - For remote hosting, use a web hosting service and upload backend files.

5. **Android Project Setup**:
   - Open the Android project in Android Studio.
   - Create a `local.properties` file in the project root folder if it doesn’t exist.
   - Add `BACKEND_URL = "your_backend_url_here"` in the `local.properties` file.

6. **Building and Running the App**:
   - Build and run the app on an Android emulator or a physical Android device.
