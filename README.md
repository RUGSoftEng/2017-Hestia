# 2017-Hestia
A home automation system.

## General Description 
This repository contains the clientside code of the **Hestia** home automation system. 
**Hestia** is a home automation system. The system consists of a server which has to be set up at home and an app.
The server is used to manage all home automation devices. The app, which is available as an apk for the Android framework,
connects to the server and provides an interface to control the devices. 

The current code represents a Minimally Viable Product (MVP), which supports the Philips Hue system. We will extend this system in the near future to add support for more plugins. 

## Setup
To install the app, all that is needed is the apk. The apk can be obtained by pulling the current release (0.3) and then compiling it using Android Studio. 
## Usage
To use the app, a connection to the server is required. For this connection, the IP-address of the server has to be known. Once we are connected to the server, the devices registered at the server are displayed on the screen. Each device also has a button and some sliders to control different aspects of it from the app. Using the app, it is possible to turn a light on or off, or change its color, for example. It is possible to add new devices to the server using the plus button. A menu will be created and the required fields have to be filled out by the user. Finally, it is also possible to remove a device from the server.

## Additional Information
For the serverside code please refer to https://github.com/RUGSoftEng/2017-Hestia-Server
