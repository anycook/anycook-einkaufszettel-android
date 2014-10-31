anycook-einkaufszettel-android
===========
[![Build Status](https://jenkins.anycook.de/buildStatus/icon?job=anycook-einkaufzettel-android)](https://jenkins.anycook.de/job/anycook-einkaufzettel-android/)

mobile grocery list for anycook

![App Screenshots](https://s3-eu-west-1.amazonaws.com/images.anycook.de/miscellaneous/141031_screenshot_github.png)

[![Get it on Google Play](http://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=de.anycook.einkaufszettel)

## Core Idea

Which anycook recipes are favorites around my location?
What do I need to buy for a selected recipe?
With the ingredients anycook Einkaufszettel (anycook shopping list) you can add to a shopping list. Favorite recipes in your location will be highlighted.

![Sketch of view and edit mode for mobile grocery list.](https://dl.dropboxusercontent.com/u/1439361/sketch.png)

## Features

- view/edit grocery list
- add/check/delete groceries
- add ingredients of anycook recipes
- change amount of ingredients depending on the number of guests
- Location overview: map of favorite recipes
- Location-based information access: find nearest recipes
- Level-of-detail approach: detailed recipe view on demand

## Required dependencies
- [Java7 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
- [Android SDK](https://developer.android.com/sdk/index.html)
  - Android 4.4.2 (API 19)
  - Android 4.3 (API 18)
  - Android 4.2.2  (API 17)
  - Extras: Android Support Repository
  - Extras: Android Support Library

### Libraries (automatically installed by Gradle) 
- [Google GSON 2.2.4](https://code.google.com/p/google-gson/)
- [Google Guava 17.0](https://code.google.com/p/guava-libraries/)
- [Android-Logger 1.3.1](http://noveogroup.github.io/android-logger/)

### Test Libraries
- [Robolectric 2.3](https://github.com/robolectric/robolectric)
- [JUnit 4.11](http://junit.org) 

## Development environment
- [Android Studio](https://developer.android.com/sdk/installing/studio.html)
- USB Debugging
- Samsung Galaxy S

## Running Unit Tests in IntelliJ
If you see the following exception when you try to run tests, you have to make some configuration changes.

```
    !!! JUnit version 3.8 or later expected:
    java.lang.RuntimeException: Stub!
      at junit.runner.BaseTestRunner.<init>(BaseTestRunner.java:5)
      at junit.textui.TestRunner.<init>(TestRunner.java:54)
      at junit.textui.TestRunner.<init>(TestRunner.java:48)
      at junit.textui.TestRunner.<init>(TestRunner.java:41)
```

1. Go to Project Structure -> Modules -> anycook-einkaufszettel-android pane. 
In the Dependencies tab, move the Module SDK dependency 
(i.e. Android API 19 Platform) to be the last item in the list.
2. Go to Project Structure -> Modules -> anycook-einkaufszettel-android -> Paths. 
The value for 'Output path' should be filled in, but 'Test output path' will not be. 
Copy the text that's in 'Output path', paste into 'Test output path', but change the final 'build/classes/debug' to 
'build/test-classes'.

(Source: [robolectric/deckard-gradle](https://github.com/robolectric/deckard-gradle))

### Thanks to
[**lemaxm**](https://github.com/lemaxm) for the test device
