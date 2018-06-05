#include <f401reMap.h>
#include <SPI.h>   

HardwareSerial mySerial(USART6);

#define ledPin pinMap(12)

const int triggerPort1 =  pinMap(10); 
const int echoPort1 = pinMap(9); 
const int led1 =  pinMap(12);
int flagA = 0;
int flagB = 0;
int flagC = 0;
int  val = 0; 
int iniz = 0;
int potValue = 0;
void setup() {
  pinMode(triggerPort1, OUTPUT);
  pinMode(echoPort1, INPUT);
  pinMode(ledPin, OUTPUT);
  pinMode(BUTTON, INPUT);
  digitalWrite(ledPin, LOW);
  mySerial.begin(9600); // Default communication rate of the Bluetooth module
  Serial.begin(9600);
}
void loop() 
{
  
  if(mySerial.available() > 0)  // Checks whether data is comming from the serial port
  {
  iniz = mySerial.read(); // Reads the data from the serial port
 } 
  if (iniz==1)
  {
    flagA=1;
    flagB=0;
    Serial.println("Starting...");
    Serial.println("\n");
  }

  
 //flag that activates the system via app, the system "see"
 if (flagA == 1) 
  {  
    
   digitalWrite( triggerPort1, LOW );
   digitalWrite( triggerPort1, HIGH );
   delayMicroseconds( 10 );
   digitalWrite( triggerPort1, LOW );
   long durata1 = pulseIn(echoPort1, HIGH );
   Serial.print("distanza: ");
   Serial.print(durata1 ,DEC);
   Serial.print("\n");
   
   if (durata1 < 8000) 
   {
      Serial.println("I detect, when I stop detecting, the chronometer starts");
      Serial.println("\n");
       Serial.println(flagA);
      Serial.println("\n");
      Serial.println(flagB);
      digitalWrite(led1, HIGH);
      
      while (flagB==0)
      {
        delayMicroseconds( 10000);
        digitalWrite( triggerPort1, LOW );
        digitalWrite( triggerPort1, HIGH );
        delayMicroseconds( 10 );
        digitalWrite( triggerPort1, LOW );
        durata1 = pulseIn(echoPort1, HIGH );
        Serial.print("distanza: ");
        Serial.print(durata1 ,DEC);
 Serial.print("\n");
       
        if (durata1 > 1500)
        {
          digitalWrite(led1, LOW);
          mySerial.write(1);
          Serial.print("partito");
          Serial.print("\n");
          
          flagB=1;
          flagA=0;
          iniz=0;
        }
           
            
      }
   }
  }
  
 
}
