# NoteApp

## Description

Simple Android application for note taking involving image scan, crop, and capture functionality.  Unique features including written text in captured image to editable word document.

# Features & Todos:

- Add scan/crop functionality on image capture (In progress... Currently working on Android 7.0/API 24 and below)
- Add directory/folder system for managing captures for specific courses (In progress...)
- Change “Take a photo” and “Return” buttons to better UI & more responsive camera/arrow icons
- Conversion of written text in image to word document with [Java OCR](https://sourceforge.net/projects/javaocr/)
- Improve existing display of image capture data
- Adding multithreading to improve scrolling of RecyclerView items
- Update [AndroidScannerDemo](https://github.com/jhansireddy/AndroidScannerDemo) for newer Android version compatibility
- Update unit tests

## Installation & Getting Started

Ensure following external libraries are installed and placed in respective folders in project:

External library [AndroidScannerDemo](https://github.com/jhansireddy/AndroidScannerDemo)

[OpenCV Framework](https://opencv.org/)

Note: Make sure the compileSdkVersion, minSdkVersion, and targetSdkVersion are the same across all build.gradle files.  Remove buildToolsVersion in scanlibrary build.gradle file as not required for newer versions of Gradle.

## Usage

Work in progress...

## Credits

App icon usage from [Daniel Bruce](https://www.flaticon.com/authors/daniel-bruce) and [Google](https://www.flaticon.com/authors/google) at www.flaticon.com.
