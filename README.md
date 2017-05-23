[![Build Status](https://travis-ci.org/RUGSoftEng/2017-Hestia-Client.svg?branch=master)](https://travis-ci.org/RUGSoftEng/2017-Hestia-Client)
[![codecov.io](https://codecov.io/gh/RUGSoftEng/2017-Hestia-Client/coverage.svg?branch=master)](https://codecov.io/gh/RUGSoftEng/2017-Hestia-Client?branch=master)

# 2017-Hestia
A home automation system.

## General Description
This repository contains the clientside code of the **Hestia** home automation system. The system consists of a server which has to be set up at home and an app.
The server is used to manage all home automation devices. The app, which is available as an apk for the Android framework,
connects to the server and provides an interface to control the devices.

The current code represents a Minimal Viable Product (MVP), which supports the Philips Hue system. We will extend this system in the near future to add support for more plugins.

## Setup
To install the app, all that is needed is the apk. The apk can be obtained by pulling the current release (0.5) from the master branch and then transferring the apk to the Android device. Alternatively, any version of the apk can be manually compiled using Android Studio. All you need to do is switch to the appropriate branch, open the project in Android Studio and compile.
## Usage
When opening the app for the first time, the user is presented with a login screen. For the MVP, this is just a mockup, and the user can login using "admin" for his username and "password" as the password.
To use the app, a connection to the server is required. For this connection, the IP-address of the server has to be known. Using this address we can connect to the server through a menu in the app. Once we have established a connection, the devices registered at the server are displayed on the screen. Each device has a button and some sliders to control different aspects of it from inside the app. Using the app, it is possible to turn a light on or off, or change its color, for example. It is possible to add new devices to the server using the plus button. A menu will be created and the required fields have to be filled out by the user. Finally, it is also possible to remove a device from the server.

## Additional Information
For the serverside code please refer to https://github.com/RUGSoftEng/2017-Hestia-Server
