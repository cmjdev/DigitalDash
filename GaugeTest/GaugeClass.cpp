#include "GaugeClass.h" //include the declaration for this class
#include <EEPROM.h>
#include <SPI.h>
#include <GD2.h>

extern byte parameterValue[39];
GaugeClass::GaugeClass(){}

// update digital/bargraph
void GaugeClass::update(uint16_t xs, uint16_t ys, uint16_t wi, uint16_t he, uint8_t pa, uint8_t ty, String st){
x = xs;
y = ys;
w = wi;
h = he;
t = ty;
p = pa;
active = true;

st.toCharArray(label,8);
}

// update analog
void GaugeClass::update(uint16_t xs, uint16_t ys, uint16_t wi,  uint8_t pa, uint8_t ty, String st){
x = xs;
y = ys;
w = wi;
t = ty;
p = pa;
active = true;

st.toCharArray(label,8);
}
void GaugeClass::writeGauge(){
  switch(t) {
    case 0:
    GD.ColorRGB(0x0096ff);
     GD.cmd_gauge(x, y, w, OPT_FLAT, 10, 5, parameterValue[p], 255);
        GD.ColorRGB(0xffffff);
     GD.cmd_number(x, y+(w/2), 30, OPT_CENTER, parameterValue[p]);
     GD.cmd_text(x, y-(w/3), 24, OPT_CENTER, label);
     break;
    case 1:
     GD.ColorRGB(0x0096ff);
     GD.cmd_progress(x,y,w,h,OPT_FLAT,parameterValue[p],255);
     GD.ColorRGB(0xFFFFFF);
     GD.cmd_text((w/2)+x, y+20, 22, OPT_CENTER, label);
     break;
  }
}
//<<destructor>>
GaugeClass::~GaugeClass(){/*nothing to destruct*/}
