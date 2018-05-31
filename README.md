# ChronoSMART - (Alpha V1)

Revolutionary app for record time in a completely automatic way.
Used for athletic races of 100m, 200m and 400m.

# Hardware materials
- 2x Nucleo board [STM32F401](http://www.st.com/en/microcontrollers/stm32f401.html?querycriteria=productId=LN1810) 
- 3x Bluetooth module [HC-SR04](http://www.radiofo.it/files/articoli/sku026931new.pdf) 
- 3x Led (optional)
- Some cable.  
- Chronosmart Application


![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/arduino-hc05-kullanc4b1mc4b1.jpg "Architecture")


We use 3 bluetooth module.
Two module are connected with master and slave method.

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



Code for Arduino IDE (MASTER and SLAVE) is avalible on : 
### https://github.com/LuigiZ91/Chronosmart


# How to work

The athlete positions the two devices, one on the starting line, respectively, while the other on the finish line.
The athlete from the Chronosmart app, enables the device. Next it is positioned near the sensor and the light on the device comes on. When the athlete starts the light goes out and the device starts counting the time.
Quando il corridore passa davanti il sensore posto alla fine della corsa, il tempo viene inviato all'app e il corridore sceglierà se salvarlo o rifiutarlo.

![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/howtowork.jpg "Architecture")



# ChronoSMART - General task


The user must register in the app and login for use all the functionality. You can set up a training session (enabling the Nucleo board to record the time) and can check the results.


![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/Screenshot.jpg "ScreenApp")


# ChronoSMART - Record time

The user must press "Start" button, and after , when athlete finish to run, the time appears on the screen, along with two buttons, one to save the session and one to delete it.

![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/start.jpg "Record Time")


# ChronoSMART - Show the results

The user can access his statistics by clicking on the menu in the appropriate button.
The statistics are shown as a graph, with the possibility of being zoomed, containing the times, dates and weather information.

![screen](https://github.com/theanto/ChronoSMART/blob/master/Screenshot/result.jpg "Result")


# Team member 

Links to our LinkedIn accounts:

- [Antonio Luciano](https://www.linkedin.com/in/antonio-luciano-b04bb915a/) 
- [Antonio Ionta](https://www.linkedin.com/in/antonio-ionta-a349b515a/) 
- [Luigi Zollo](https://www.linkedin.com/in/luigi-zollo-85056915a/) 

# Slideshow links

- First presentation - Idea : https://www.slideshare.net/secret/LcyxXcwAr3uGUI
- Second presentation - Focus on idea : https://www.slideshare.net/secret/yBZDNS6EJlqRk5
- MVP : https://www.slideshare.net/AntonioLuciano12/chronosmart
- Final presentation - Chronosmart :
