#ifndef DashClass_H
#define DashClass_H

#include "Arduino.h"
#include "GaugeClass.h"

class DashClass {
  private:
  byte gaugeNumber;  
  GaugeClass g[8];
  public:
  DashClass();
  ~DashClass();
  void addGauge(uint16_t,uint16_t,uint16_t,uint16_t,uint8_t, uint8_t,String);
  void addGauge(uint16_t,uint16_t,uint16_t,uint8_t, uint8_t,String);
  void display();
  

};

#endif
