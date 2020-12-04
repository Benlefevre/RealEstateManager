# Real Estate Manager

An android application which allows a real estate agent to quickly consult all the properties he owns under mandate.

## Description

This app is developped in Java.  

Training for use :

    - Room and SQLite
    - ViewBinding
    - GoogleMap
    - Navigation component (single activity)
    - SnapHelper
    - ExoPlayer
    - Different UI according to the device form factor and orientation
    - ContentProvider
    - Lifecycle / LiveData / ViewModel
    - MVVM

## Features

    - See all the properties stored in local database in a list with primary information
    - See a details screen with all the property's information and its location on a map
    - Add a property within the app in the local database
    - Edit a property within the app
    - Add photos and videos to illustrated a property
    - Search properties according to different criteria and see them in a list
    - Convert areas in square meters or square feet
    - Convert prices in Dollars or Euros

## Run on

After downloading or clone the repository, you need to create a new project in Google Cloud Platform (think to restrict your apikey with your SHA-1 and the correct API).

In Google Cloud Platform you have to activate :

    -Google Map

Add your google platform apikey in :

    - realestatemanager/gradle.properties
      - GOOGLE_MAPS_API_KEY="xxxxxxxxxxxxxxxxxxxxxx"

## Documentation

You can find a french documentation video  at :

    - assets/documentation/demo.mp4
[Go to documentation](https://github.com/Benlefevre/RealEstateManager/blob/master/assets/documentation/demo.mp4)

![demo](assets/documentation/demo.gif)

## Screenshots

### Home screen

![home screen](assets/screenshots/home.webp)

### Details screen

![details screen](assets/screenshots/detail.webp)

### Fullscreen

![full screen](assets/screenshots/fullscreen.webp)

### Edit screen

![edit screen](assets/screenshots/edit.webp)

### Add screen

![add screen](assets/screenshots/add.webp)

### Media permissions

![perm screen](assets/screenshots/photoperm.webp)

### Dialog media choice

![dialog1 screen](assets/screenshots/dialogchoice.webp)

### Dialog media source

![dialog2 screen](assets/screenshots/dialogchoice2.webp)

### Dialog save media

![savepic screen](assets/screenshots/savepic.webp)

### Drawer

![drawer screen](assets/screenshots/drawer.webp)

### Location permissions Dialog

![loc screen](assets/screenshots/location.webp)

### Map screen

![map screen](assets/screenshots/map.webp)

### Search screen

![search screen](assets/screenshots/search.webp)

### Settings screen

![settings screen](assets/screenshots/settings.webp)
