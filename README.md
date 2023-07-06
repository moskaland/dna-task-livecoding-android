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

Task 1:
As an MVP you should enhance product list with functionality to select/unselect product on the list and ...

Task 2:
...be able to buy at least one product (MVP approach).

To perform payment you must:
- initiate purchase transaction
- call payment API using transaction identifier and card token read from reader API
- confirm purchase transaction after successful payment
