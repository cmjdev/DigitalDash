#include <TimerOne.h>

#include <EEPROM.h>
#include <SPI.h>
#include <GD2.h>
#include "DashClass.h"
#include "GaugeClass.h"

#define BG_COLOR 0x000
#define GAUGE_BG 0x000
#define GAUGE_FG 0x0000FF

byte parameterValue[39];
byte takeData = true;
DashClass Dashboard = DashClass();

void setup()
{
  Serial.begin(9600);
  GD.begin();
  GD.cmd_bgcolor(GAUGE_BG);
  GD.cmd_fgcolor(GAUGE_FG);
  Dashboard.addGauge(120,136,150,13, 0, "RPM");
  Dashboard.addGauge(360,136,150,4, 0, "MAP");
  Dashboard.addGauge(50,235,380,30,7,1, "THR");

}

void loop()
{
  Serial.write('R');
  GD.Clear();
  Dashboard.display();
  GD.swap();
    delay(60);
}


void serialEvent() {
  byte i = 0;
  while (Serial.available()) {
    // get the new byte:
    parameterValue[i++] = (byte)Serial.read(); 
  }
}

