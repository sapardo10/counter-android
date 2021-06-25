# Counter Cornershop

This repository contains the code of the counter application that is presented by Sergio Pardo as the coding challenge for Cornershop.

## Introduction

This readme will contain some info on how to run the code and also some explanations about the decisions, implementations and libraries used within the project.

### How to run it?

It is quite simple to run it. You only have to download this project, have android studio configurated with an emulator or a debugable device and press run!

If you have to locally execute the server I recommend you to start it on localhost and the use a tool as ngrok to expose that port as a final url. It is important to note that the application can only consume https endpoints, so be sure to expose it like that.

### Architecture

This project followed the architecture proposed by "Uncle Bob" named clean arquitecture. The application is divided into three main layers/modules: Application, Domain and Data. The outer layers know the inner layers but not the other way around, meaning this that the application depends on the domain, but the domain does not depend on application. The advantages of using clear architecture are several; from separation of concerns to even writing multiplatform code. Since the inner layers are written in pure kotlin, it does not depend on any specification so you can use it anywhere.

The normal data flow for any given feature goes something like: View > View Model > Use Case > Repository > Datasource. Even though the repository depends on the datasources and these classes must have implmenetation logic that depends on the android framework or third-party implementations, the only way to achieve having pure kotlin modules was through the use of dependency injection with the help of HILT. This helped to create the implementation of the datasources on the application layer, but then inject it to the repositories , since the formers depend not on implementation but on the interfaces.

On the presentation/application level a MVVM code structure was used with the help of Jetpack, using the popular ViewModel and LiveData. This allows for us to build and incredible reactive app while not having to worry too much about memory management since the live data observers are lifecycle aware.

### Feature organization

The features of the application were divided into differente packages following the amount of screens presented as long as the screen had one purpose. For more complexs screens, fragments were used to increase the flexibility and functionality of those screens.

### Dependencies used

The main libraries used are:
- Retrofit -> API consumption
   - It gives an easy way to implement an http client with having to give it only a couple of details and the api structure on an interface
- ROOM -> Local database
   - It abstracts the SQLite implementation to a simpler level while not losing any performance
- androidx. -> Views and architecture
  - Implementation of several features with less boilerplate code.
  - lifecycle aware operations / components
- Coroutines -> Asynchronous jobs
  - It makes asynchronous code handling as simple as synchronous code handling
- Mockito -> Test without real dependencies
  - So you can unit test a class without having to worry about children implementations.
- Material -> UI design
  - Some of the components presented on the figma were material ones, so it made it easier to implement them.
