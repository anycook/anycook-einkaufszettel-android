anycook-einkaufszettel-android
===========
[![Build Status](http://teamcity.anycook.de/app/rest/builds/buildType:(id:Anycook_Einkaufszettel_Build)/statusIcon)](http://teamcity.anycook.de/viewType.html?buildTypeId=Anycook_Einkaufszettel_Build&guest=1)

mobile grocery list for anycook

![App Screenshots](https://s3-eu-west-1.amazonaws.com/images.anycook.de/miscellaneous/150215-android-screenshots.png)

[![Get it on Google Play](http://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=de.anycook.einkaufszettel)

## Core Idea

Which anycook recipes are favorites around my location?
What do I need to buy for a selected recipe?
With the anycook Einkaufszettel (anycook shopping list) you can add ingredients (of recipes from [anycook](anycook.de)) to a shopping list. Favorite recipes in your location will be highlighted.

## Features

- view/edit grocery list
- add/check/delete groceries
- add ingredients of anycook recipes
- change amount of ingredients depending on the number of guests
- Level-of-detail approach: detailed recipe view on demand
- If-Modified-Since: Uses modified since header to save traffic

## Required dependencies
- [Java7 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
- [Android SDK](https://developer.android.com/sdk/index.html)
  - Android 6.0 (API 23)
  - Extras: Android Support Repository
  - Extras: Android Support Library

### Libraries (automatically installed by Gradle) 
- [Google GSON 2.5](https://github.com/google/gson)
- [Google Guava 18.0](https://github.com/google/guava)
- [Android-Logger 1.3.5](http://noveogroup.github.io/android-logger/)

### Test Libraries
- [Robolectric 3.0](https://github.com/robolectric/robolectric)
- [JUnit 4.12](http://junit.org)

## Development environment
- [IntelliJ](https://www.jetbrains.com/idea/)

## Running Unit Tests in IntelliJ
You have to [enable](https://www.bignerdranch.com/blog/triumph-android-studio-1-2-sneaks-in-full-testing-support/) unit testing in IntelliJ.

## Issues
We are using [YouTrack](http://anycook.myjetbrains.com/youtrack) for issue tracking. Feel free to submit issues there.
