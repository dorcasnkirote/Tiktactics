# TikTok Phishing Detection Mobile App

## Table of Contents
- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [Repositories](#repositories)
- [How It Works](#how-it-works)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

The TikTok Phishing Detection Mobile App is designed to help users protect their TikTok accounts from phishing attacks. By utilizing a regex-based detection system, the app scans for potential threats and alerts users to suspicious links and content. The app requires users to log in with their Gmail accounts and generate an app password for enhanced security.

## Technologies Used

- **Mobile App**: 
  - Kotlin (Android)
- **Backend**:
  - Python (Django REST Framework)
  - Regex for phishing detection
- **Database**: PostgreSQL
- **Authentication**: Gmail OAuth 2.0 with 2FA

## Repositories

- **Mobile App Repository**: [Tiktactics Mobile App](https://github.com/dorcasnkirote/Tiktactics/)
- **Backend Repository**: [TikTacTics Backend](https://github.com/Coll76/TikTacTics)

## How It Works

1. **User Authentication**: Users log into their account through the mobile app.

2. **App Password Generation**:
   - After logging in, users are required to generate an app password from their Gmail account. This is a security feature that requires two-factor authentication (2FA) to be enabled.
   - Users can follow the prompts in the app to guide them through the process of generating an app password in their Gmail settings.

3. **Initiate Protection**:
   - Once the app password is generated, users enter it in the **"Initiate Protection"** field within the app.
   - This app password is securely stored and used to authenticate API requests made to the backend.

4. **Background Scan**:
   - Upon entering the app password, the app triggers a background scan for phishing attempts. This scan utilizes regex patterns implemented in the backend to identify potentially malicious links and phishing content.

5. **Results and Recommendations**:
   - After the scan is complete, users receive feedback on the security of their account and any detected threats. The app may also provide recommendations for further actions to enhance their security.

## Installation

### Prerequisites
- Android Studio
- Kotlin SDK
- Dependencies as specified in the `build.gradle` file

### Steps
1. Clone the mobile app repository:
   ```bash
   git clone https://github.com/dorcasnkirote/Tiktactics/
   cd Tiktactics
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an Android device or emulator.

For backend setup, refer to the [TikTacTics Backend Repository](https://github.com/Coll76/TikTacTics).

## Usage

1. Launch the app on your Android device.
2. Log in with your Gmail account.
3. Generate an app password from your Gmail account (ensure 2FA is enabled).
4. Enter the app password in the **"Initiate Protection"** field.
5. The app will perform a background scan for phishing attempts and provide feedback.
