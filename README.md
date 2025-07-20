# NFC Card Reader Project

This Android app demonstrates reading NFC cards using Android's **Reader Mode**. It supports the IsoDep protocol, which is commonly used in smart cards like Thales cards and other contactless payment or access cards.

![App Screenshot](images/nfc_scan.png "NFC Scan in action")

---

## Features

- **IsoDep Protocol Support:** Communicates directly with smartcards using APDU commands.
- **Two NFC Reading Methods:** Includes both Reader Mode and Foreground Dispatch for tag detection.
- **Lightweight UI:** Simple interface that displays the card response and ID.
- **Device Tested:** Successfully tested on Lenovo Vibe K4 Note running Android 6.0 (API 23).
- **Error Handling:** Detects unsupported tags and displays relevant messages.
- **NFC Service Tips:** Requires enabling NFC and disabling certain services (safe mode recommended) on Lenovo Vibe K4 Note for optimal functionality.

---

### Prerequisites

- Android device with NFC support.
- Android Studio for building and running the project.
- NFC card (e.g., Thales card) for testing.

