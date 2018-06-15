# ChronoSMART

Revolutionary app for record time in a completely automatic way.
Used for athletic races of 100.

# Hardware materials
- 2x Nucleo board [STM32F401](http://www.st.com/en/microcontrollers/stm32f401.html?querycriteria=productId=LN1810) 
- 2x Proximity sensors [HC-SR04](http://www.radiofo.it/files/articoli/sku026931new.pdf) 
- 3x Bluetooth module [HC-05](https://components101.com/wireless/hc-05-bluetooth-module)
- 3x Led (optional)
- Some cable.  
- Chronosmart Application

![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/photo_2018-06-07_11-32-24.jpg) 


![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/arduino-hc05-kullanc4b1mc4b1.jpg "Architecture")

We use 3 bluetooth module.
Two module are connected with master and slave method.

# Prototype of Chronosmart

#### The Master (position at starting point) 


*Outside*

![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/photo_2018-06-05_14-44-19.jpg "Master")

*Inside*


![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/photo_2018-06-05_14-44-266.jpg "Master")

#### The Slave (position at finish point) 


*Outside*
![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/photo_2018-06-05_14-44-29.jpg "Slave")

*Inside*


![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/photo_2018-06-05_14-44-23.jpg "Slave")

# AT mode for microcontrollers paring

Most useful AT commands are

- AT : Ceck the connection.
- AT+NAME : See default name
- AT+ADDR : see default address
- AT+VERSION : See version
- AT+UART : See baudrate
- AT+ROLE: See role of bt module(1=master/0=slave)
- AT+RESET : Reset and exit AT mode
- AT+ORGL : Restore factory settings
- AT+PSWD: see default password

Slave configuration 

![screen](https://howtomechatronics.com/wp-content/uploads/2016/04/Slave-Configuration-HC-05-Bluetooth-Module-Arduino.png )

Master configuration 

![screen](https://howtomechatronics.com/wp-content/uploads/2016/04/Master-Configuration-HC-05-Bluetooth-Module-Arduino.png )



# Code for Arduino IDE (MASTER and SLAVE)

The arduino code for make work the project "Chronosmart".
There are two file that you have to load on the boards after pairing them each other through the AT mode.

# Chronosmart - Arduino (Master.ino)
The master waits for the start signal from the slave wich in turn waits for the start of the app hence when the user click on the button "start" the slave send "1" to master and the system is turn On. When the runner is positioned in front of the first sensor the master turn on the green Led to signal that it will start to record time when the run starts.
Once the runner starts the Master send a signal to make the slave start to record time.


# Chronosmart - Arduino (Slave.ino)
The slave send the "1" to the master (when the user click on the "start button") and wait the signal (always from the master) to start the stopwatch. When the runner passes in front of the sensor, the slave stop the stopwatch and send the time to the Android app.


# Chronosmart - Evaluation of delay (wired vs Bluetooth connection) 

We took the times of the same phenomenon in two different ways:

- Connection by cable
- Connection via Bluetooth

We have made 10 attempts and the average delay time between the two connections is: 92.5
as shown in this screen





# Software Architecture 

The components of our project are:

- Android application
- database on hosting service
- Firebase Authentication and Firebase Realtime 

![screen]( https://github.com/theanto/ChronoSMART/blob/master/Screenshot/photo_2018-06-07_11-32-27.jpg )

# How to work

The athlete positions the two devices, one on the starting line, respectively, while the other on the finish line.
The athlete from the Chronosmart app, enables the device. Next it is positioned near the sensor and the light on the device comes on. When the athlete starts the light goes out and the device starts counting the time.
When the rider passes in front of the sensor at the end of the race, the time is sent to the app and the runner will choose whether to save or reject it.

![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/photo_2018-06-07_11-32-18.jpg "Architecture")



# ChronoSMART - General task


The user must register in the app and login for use all the functionality. You can set up a training session (enabling the Nucleo board to record the time) and can check the results.


![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/Screenshot.jpg "ScreenApp")


# ChronoSMART - Record time

The user must press "Start" button, and after , when athlete finish to run, the time appears on the screen, along with two buttons, one to save the session and one to delete it.

![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/recordtime1.jpg "Record Time")


# ChronoSMART - Show the results

The user can access his statistics by clicking on the menu in the appropriate button.
The statistics are shown as a graph, with the possibility of being zoomed, containing the times, dates and weather information.

![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/6s.jpg "Result")


# Team members 

Links to our LinkedIn accounts:

- [Antonio Luciano](https://www.linkedin.com/in/antonio-luciano-b04bb915a/) 
- [Antonio Ionta](https://www.linkedin.com/in/antonio-ionta-a349b515a/) 
- [Luigi Zollo](https://www.linkedin.com/in/luigi-zollo-85056915a/) 

# Slideshow links

- First presentation - Idea : https://www.slideshare.net/AntonioIonta/my-fitness-system-initial-ideas
- Overview and user evaluation : https://www.slideshare.net/AntonioIonta/chronosmart-overview-user-evaluation
- MVP: https://www.slideshare.net/AntonioIonta/chronosmart-mvp
- Final presentation - Chronosmart : https://www.slideshare.net/AntonioIonta/chronosmart-final-presentation-101093689

# Video of demo
https://youtu.be/Adssc22V0bk
