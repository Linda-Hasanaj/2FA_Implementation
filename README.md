# Two-Factor Authentication Android App
This project implements a Two-Factor Authentication (2FA) system in an Android application using Java. 
The application uses SQLite as the database to store user credentials securely, including hashed passwords.

## Features
- User Registration:
    - User can register by providing full name, email and password.
    - A one-time password is generated and sent via email.
    - The OTP must be enterd on the specified field in order to signup successfylly.
 
- User Login:
    - Standard login with an email and password.
    -  A one-time password is generated and sent via email.
    - The OTP must be enterd on the specified field in order to login successfully.
 
## Technologies Used
- Language: Java
- IDE: Android Studio
- Database: SQLite
- Security: Hashing algorithm BCrypt.

## Setting up and running the application
- Clone the repository.
- Open the project in Android Studio.
- Go to Google Account Security and enable 2-Step Verification.
- Generate an App Password to use in the application.
- Build and run the application on an emulator or physical device.
  
