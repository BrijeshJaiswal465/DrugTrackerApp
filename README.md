
Add Medication UI screens and Firebase Authentication

- Created 'My Medications' UI
- Created 'Search Medication' UI with search functionality
- Created 'Drug Details' UI
- Integrated getDrug API for fetching medications
- Enabled live search in Search Medication screen
- Implemented Firebase Email/Password Authentication"# DrugTrackerApp


- Implemented Room SQLite database integration  
- Added functionality to insert medicine records  
- Added functionality to remove medicine records  
- Displaying list of added medicines  
- Showing detailed view of selected medicine  







# DrugTrackerApp

An Android application that allows users to search for prescription drugs using the RxNorm API and track their personal medications. It includes features for drug search, detailed information display, and local tracking with SQLite.

---

## Project Overview

DrugTrackerApp enables users to:
- Register/Login using Firebase Authentication
- Search medications via the RxNorm API (filtered with `tty=SBD`)
- View detailed drug information
- Add up to 7 drugs from API and 3 custom drugs (max 10)
- Store and view drugs locally using Room (SQLite)
- Swipe to delete medications
- Prevent duplicate drug entries
- Designed for accessibility and supports animations (bonus)

---

## Features Implemented (Requirements)

- [x] Firebase Email/Password Authentication
- [x] RxNorm drug search API with `tty=SBD` and `expand=psn`
- [x] SQLite (Room) local database integration
- [x] Add drugs to local list (max 7)
- [x] Swipe to delete functionality
- [x] Search medications and show results in a clean list
- [x] Drug details screen
- [x] MVVM architecture used

---

## Bonus Features

- [x] Prevent re-adding already added drugs with appropriate message
- [x] Light animation used in transitions
- [x] Accessibility-aware color and text sizes
- [x] Status bar UI customized
- [x] Keyboard-aware UI behavior
- [x] Loading and error handling logic
- [x] Clean commit messages and GitHub-friendly code comments

---

## Tech Stack

- **Language:** Java
- **Architecture:** MVVM
- **Database:** Room (SQLite)
- **API Integration:** RxNorm (REST API)
- **Auth:** Firebase Authentication
- **UI:** Material Components, ConstraintLayout
- **Min SDK:** 24  
- **Target SDK:** 34  

---

## 🧠 If I Had More Time...

- Improve UI polish with full Jetpack Compose or custom designs
- Add daily medication reminder notifications
- Sync user data with Firebase Firestore for cloud backup
- Pagination support in drug search
