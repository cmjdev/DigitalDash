#include "DashClass.h"
#include <EEPROM.h>
#include <SPI.h>
#include <GD2.h>

DashClass::DashClass() {
  gaugeNumber = 0;
  GaugeClass g[8];
}

DashClass::~DashClass() {}

void DashClass::addGauge(uint16_t xs,uint16_t ys, uint16_t wi, uint8_t pa, uint8_t ty, String s) {

  g[gaugeNumber++].update(xs, ys, wi, pa, ty, s);

}

void DashClass::addGauge(uint16_t xs,uint16_t ys, uint16_t wi, uint16_t he, uint8_t pa, uint8_t ty, String s) {

  g[gaugeNumber++].update(xs, ys, wi, he, pa, ty, s);

}

void DashClass::display() {
  byte i = 0;
  
  while (g[i].active)
  g[i++].writeGauge();
}
