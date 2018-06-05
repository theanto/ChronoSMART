#include <f401reMap.h>

#define RedLed pinMap(3)
#define YellowLed pinMap(4)

HardwareSerial mySerial(USART6); // rreceives from the master

HardwareSerial mySerial2(USART1); //send to the app

int accensione=0;
const int triggerPort1 =  pinMap(10); 
const int echoPort1 = pinMap(9);
float tempo_base;
int state = 0;
int flagB = 0;
float tempoBaseSec;
float tempoI;
float tempoF;
float tempo;
unsigned int var = 0;
unsigned int var2 = 0;

void setup() 
{    
  pinMode(triggerPort1, OUTPUT);
  pinMode(echoPort1, INPUT);
  Serial.begin(9600);
  mySerial2.begin(9600);
  mySerial.begin(9600);
  pinMode(13, OUTPUT);
  pinMode(RedLed, OUTPUT);
  pinMode(YellowLed, OUTPUT);
}

void loop() 
{
  /*------------------------------ App code to send the turn on signal ------------------------------------------------- */
 
  if (mySerial2.available() > 0)
  {
    accensione = mySerial2.read();
    if (accensione == 1);  
    {
      mySerial.write(1);  
    } 
  }

  /*------------------------------- Comunication with the other board and time calculation ------------------------------- */
     
 if(mySerial.available() > 0)
 {
    state = mySerial.read(); // Reads the data from the serial port
    Serial.print("State e': ");
      Serial.println(state);
      delay(1000);
    if (state == 1)
    {
      tempo_base = millis();  
      tempoBaseSec= tempo_base/1000;
      flagB = 1;
      digitalWrite(YellowLed, LOW);
      digitalWrite(RedLed, HIGH);
    }
 }

 while (flagB == 1)
 {
   digitalWrite( triggerPort1, LOW );
   digitalWrite( triggerPort1, HIGH );
   delayMicroseconds( 10 );
   digitalWrite( triggerPort1, LOW );
   long durata1 = pulseIn(echoPort1, HIGH );  
   if (durata1 < 8000)
    {
      tempoI = millis();
      tempoF = tempoI/1000;
      tempo = tempoF - tempoBaseSec;
      Serial.print("IL tempo e': ");
      Serial.println(tempo);
      delay(1000);
      char tempo2 = tempo;
        
     /*----------------------------------- Code to send the time to the App ---------------------------------- */
       mySerial2.println(tempo);
      digitalWrite(RedLed, LOW);
      digitalWrite(YellowLed, HIGH);
  
       
      /*----------------------- Code to reset the system ----------------------------------*/
      flagB = 0;
      mySerial.write(0);
      accensione = 0;
    }
 }
}

 
