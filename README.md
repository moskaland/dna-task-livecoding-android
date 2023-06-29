## Environment requirements

Android Studio 2021+

```
android {
    compileSdk 33
    defaultConfig {
        minSdk 21
        targetSdk 33
   }
}
```

```
plugins {
    id 'com.android.application' version '7.2.2' 
    id 'com.android.library' version '7.2.2'
    id 'org.jetbrains.kotlin.android' version '1.8.0' 
}
```

Please be ready to share Emulator or connected device using [Sharing tool](https://github.com/Genymobile/scrcpy).


# dna-task-livecoding-android


The goal is to implement an app that will work on a POS (point-of-sale) device that will allow to select products from the available set and pay for them with a card.

You should enhance product list with functionality to select/unselect products on the list and show the summary of selected items.

Once items are selected you should be able to pay for them using provided APIs.
