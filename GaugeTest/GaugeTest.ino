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
int lastY = -1;
uint8_t frame = 0;

void drawMenu(){

  byte inMenu = 1;
  byte createGauge = 0;

  while(inMenu){
    GD.Clear();
    GD.ColorA(50);
    Dashboard.display();
    GD.ColorA(255);
    GD.get_inputs();
    switch (GD.inputs.tag) {
    case 100:
      //GD.cmd_text(149, 146, 18, 0, "Create Analog");
      createGauge = true;
      break;
    case 250:
      inMenu = 0;
      break;
    }
    GD.Tag(100);
    GD.cmd_button(5, 5, 140, 30, 28, OPT_FLAT | OPT_NOBACK,  "Create Gauge");
    GD.Tag(250);
    GD.cmd_button(395, 235, 80, 30, 28, OPT_FLAT | OPT_NOBACK,  "Exit"); 

    if(createGauge) {
      GD.Tag(201);
      GD.cmd_button(5, 5, 140, 30, 28, OPT_FLAT | OPT_NOBACK,  "Analog");
      GD.Tag(202);
      GD.cmd_button(5, 45, 140, 30, 28, OPT_FLAT | OPT_NOBACK,  "Digital");
      GD.Tag(203);
      GD.cmd_button(5, 85, 140, 30, 28, OPT_FLAT | OPT_NOBACK,  "Bargraph");
      GD.Tag(204);
      GD.cmd_button(5, 125, 140, 30, 28, OPT_FLAT | OPT_NOBACK,  "Indicator");
    }
    if(inMenu)GD.swap();
  }

}

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

    lastY = GD.inputs.y;

  GD.Clear();
  GD.get_inputs();

  if (lastY > 0 && lastY < 20 && GD.inputs.y > 20)
    drawMenu();

  GD.cmd_number(50,200,20,0, lastY);
  GD.cmd_number(50,230,20,0, GD.inputs.y);

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

