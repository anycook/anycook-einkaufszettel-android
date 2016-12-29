anycook-einkaufszettel-android
===========
[![Build Status](https://jenkins.gesundkrank.de/job/anycook/job/anycook-einkaufszettel-android/job/master/badge/icon)](https://jenkins.gesundkrank.de/job/anycook/job/anycook-einkaufszettel-android/job/master/)

mobile grocery list for anycook

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
  - Android 7.1 (API 25)
  - Extras: Android Support Repository
  - Extras: Android Support Library

### Libraries (automatically installed by Gradle) 
- [Google GSON](https://github.com/google/gson)
- [Google Guava](https://github.com/google/guava)
- [Android-Logger](http://noveogroup.github.io/android-logger/)

### Test Libraries
- [JUnit](http://junit.org)

## Development environment
- [IntelliJ](https://www.jetbrains.com/idea/)

## Issues
We are using GitHub issues for issue tracking. Feel free to add any observed issues [here](https://github.com/anycook/anycook-einkaufszettel-android/issues)
