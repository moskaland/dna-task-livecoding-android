
To do a solid, quite bullet-proof MVP with a given scope is actually quite a big task.
I focused on showcasing my approach to app design and architecture (and also had some fun during the coding interview task). Unfortunately, I did not have time to polish my solution, so there will be quite a few things to improve.

# Refactor done
- Modularization, as per the image below. With the growing number of functionalities, I'd vote for introducing a Domain layer sooner rather than later
- Added Hilt dependency injection
- Introduced navigation component

<img width="1230" alt="Screenshot 2023-09-18 at 09 56 56" src="https://github.com/moskaland/dna-task-livecoding-android/assets/75085107/08119611-f5ae-4000-95d0-8d05415bfd0a">


# Notable shortcuts I made, due to time constraints
- Most of the classes are left untested... I've written some sample unit tests to showcase how I do write them. Production code would require a comprehensive set of unit and UI tests!
- I did not use persistent storage; once the app process is killed, the ongoing transaction/session is lost and not handled on app restart
- Lack of logging and documentation
- The self-review process was very limited
= I left the configuration mostly unchanged - Gradle files are messy, ProGuard is not configured, etc.

# Basic Flow in the app
On ProductScreen, items are added to the cart. User can select items and navigates to checkout.

On CheckoutScreen, the transaction is initialized with the merchant. User can click Order and Pay and navigates to payment.

On PaymentScreen, the user tries to use a card: gathers card data and if successful, pays. Once successfully paid, user is automatically navigated to the SummaryScreen.

On SummaryScreen, the merchant is notified to complete the order. If all goes right, the user is taken to the ProductScreen with an empty cart.  If merchants call fails, we try to refund the payment.

# Tips for using the app with a mock API
When the user tries to order a Snack Bar, then an order error will be generated.

When the user tries to order products under 5 EUR, then a payment error will be generated.

When the user tries to order a Gold Bar, then he will be able to pay but not confirm the order - money will be refunded.

When the user tries to use a card, it will fail randomly 50% of the time.


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
