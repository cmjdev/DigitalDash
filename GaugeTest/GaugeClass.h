#ifndef GaugeClass_H
#define GaugeClass_H

#include "Arduino.h"

class GaugeClass {
  public:
  uint16_t x;
  uint16_t y;
  uint16_t w;
  uint16_t h;
  uint8_t t;
  uint8_t p;
  uint8_t active;
  String s;
  char label[8];
  GaugeClass();
  // update digital/bargraph
  void update(uint16_t, uint16_t, uint16_t, uint16_t, uint8_t,uint8_t, String);
  
  // update analog
  void update(uint16_t, uint16_t, uint16_t, uint8_t,uint8_t, String);
  
  void writeGauge();
  ~GaugeClass();
};

#endif
