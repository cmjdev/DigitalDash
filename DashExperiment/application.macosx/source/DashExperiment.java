import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DashExperiment extends PApplet {



Serial myPort;  // Create object from Serial class

byte parameterValue[]    = {0,0,0,0,0,0,0,0,
                           0,0,0,0,0,0,0,0,
                           0,0,0,0,0,0,0,0,
                           0,0,0,0,0,0,0,0,
                           0,0,0,0,0,0,0};

String parameterName[] = {"secl","squirt","engine","baroADC",
                          "mapADC","matADC","cltADC","tpsADC",
                          "batADC","egoADC","egoCorrection","airCorrection",
                          "warmupEnrich","rpm100","pulsewidth1","accelEnrich",
                          "baroCorrection","gammaEnrich","veCurr1","pulsewidth2",
                          "veCurr2","idleDC","iTime1","iTime2",
                          "advance","afrTarget","fuelADC","egtADC",
                          "CltIatAngle","KnockAngle","egoCorrection2","porta",
                          "portb","portc","portd","stackL",
                          "tpsLast","iTimeX","bcdc"};
 
// parameter types: 0-no display, 1-slider, 2-toggle                         
byte parameterType[]    = {0,2,2,0,1,1,1,1,
                           1,0,1,0,1,1,1,0,
                           0,0,0,0,0,0,0,0,
                           1,1,0,0,0,0,0,2,
                           2,2,2,0,0,0,0};

int sliderx = 20;
int slidery = 20;
int togglex;
int toggley = 20;

ControlP5 cp5;

public void setup() {
  size(500,280);
  noStroke();
  
  println(Serial.list());
  String portName = Serial.list()[8];
  myPort = new Serial(this, portName, 9600);
  
  cp5 = new ControlP5(this);
  
  cp5.addButton("RandomData")
     .setValue(0)
     .setPosition(width-80,height-30)
     .setCaptionLabel("Random Data")
     .align(ControlP5.CENTER,ControlP5.CENTER,ControlP5.CENTER,ControlP5.CENTER);
     ;
       
  for(byte i=0; i < parameterValue.length; i++) {
    switch(parameterType[i]){
    case(1):
      cp5.addSlider(parameterName[i])
        .setId(i)
        .setPosition(sliderx, slidery)
        .setRange(0,255);  
      if (slidery >= height-40) {
        sliderx += 180;
        slidery = 20;
      } else
      slidery += 20;
      break;
    case(2):
      togglex = sliderx + 250;
      cp5.addTextlabel(parameterName[i])
          .setText(parameterName[i])
          .setPosition(togglex-40,toggley);
      for(byte j=0; j<8; j++){
        cp5.addToggle(parameterName[i] + String.valueOf(j))
          .setPosition(togglex+(j*25),toggley)
          .setSize(20,20)
          .setLabelVisible(false);
      }
      toggley += 25;
      break;
      
    }
      

  }

  
  // reposition the Label for controller 'slider'
  //cp5.getController("slider").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
  //cp5.getController("slider").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
  
  

}

public void draw() {
background(0);
}

public void updateValues() {
  for(byte i=0; i<parameterValue.length; i++){
    switch(parameterType[i]) {
      case(0): parameterValue[i] = 0;
        break;
      case(1): parameterValue[i] = PApplet.parseByte(cp5.getController(parameterName[i]).getValue());
        break;
      case(2):
        for(byte j=0; j<8; j++){
          if(PApplet.parseByte(cp5.getController(parameterName[i] + String.valueOf(j)).getValue()) == 1){
            parameterValue[i] = PApplet.parseByte(parameterValue[i] | 1 << j);
          } else
            parameterValue[i] = PApplet.parseByte(parameterValue[i] &= ~(1 << j));
        }
         break;
    }
  }
}

public void controlEvent(ControlEvent theEvent) {
  updateValues();
}

public void RandomData(int theValue) {
  for(byte i=0; i < parameterValue.length; i++){
    switch (parameterType[i]) {
      case(1): cp5.getController(parameterName[i]).setValue(random(100,255));
        break;
      case(2):
        for(int j=0; j<8; j++)
          cp5.getController(parameterName[i] + String.valueOf(j)).setValue(PApplet.parseInt(random(1) < .4f));
    }
  }
}

public void serialEvent(Serial myPort) {
  char var = (char)myPort.read();
  if (var == 'R')
    myPort.write(parameterValue);
}

/**
* ControlP5 Slider
*
*
* find a list of public methods available for the Slider Controller
* at the bottom of this sketch.
*
* by Andreas Schlegel, 2012
* www.sojamo.de/libraries/controlp5
*
*/

/*
a list of all methods available for the Slider Controller
use ControlP5.printPublicMethodsFor(Slider.class);
to print the following list into the console.

You can find further details about class Slider in the javadoc.

Format:
//ClassName : returnType methodName(parameter type)

controlP5.Slider : ArrayList getTickMarks() 
controlP5.Slider : Slider setColorTickMark(int) 
controlP5.Slider : Slider setHandleSize(int) 
controlP5.Slider : Slider setHeight(int) 
controlP5.Slider : Slider setMax(float) 
controlP5.Slider : Slider setMin(float) 
controlP5.Slider : Slider setNumberOfTickMarks(int) 
controlP5.Slider : Slider setRange(float, float) 
controlP5.Slider : Slider setScrollSensitivity(float) 
controlP5.Slider : Slider setSize(int, int) 
controlP5.Slider : Slider setSliderMode(int) 
controlP5.Slider : Slider setTriggerEvent(int) 
controlP5.Slider : Slider setValue(float) 
controlP5.Slider : Slider setWidth(int) 
controlP5.Slider : Slider showTickMarks(boolean) 
controlP5.Slider : Slider shuffle() 
controlP5.Slider : Slider snapToTickMarks(boolean) 
controlP5.Slider : Slider update() 
controlP5.Slider : TickMark getTickMark(int) 
controlP5.Slider : float getValue() 
controlP5.Slider : float getValuePosition() 
controlP5.Slider : int getDirection() 
controlP5.Slider : int getHandleSize() 
controlP5.Slider : int getNumberOfTickMarks() 
controlP5.Slider : int getSliderMode() 
controlP5.Slider : int getTriggerEvent() 
controlP5.Controller : CColor getColor() 
controlP5.Controller : ControlBehavior getBehavior() 
controlP5.Controller : ControlWindow getControlWindow() 
controlP5.Controller : ControlWindow getWindow() 
controlP5.Controller : ControllerProperty getProperty(String) 
controlP5.Controller : ControllerProperty getProperty(String, String) 
controlP5.Controller : Label getCaptionLabel() 
controlP5.Controller : Label getValueLabel() 
controlP5.Controller : List getControllerPlugList() 
controlP5.Controller : PImage setImage(PImage) 
controlP5.Controller : PImage setImage(PImage, int) 
controlP5.Controller : PVector getAbsolutePosition() 
controlP5.Controller : PVector getPosition() 
controlP5.Controller : Slider addCallback(CallbackListener) 
controlP5.Controller : Slider addListener(ControlListener) 
controlP5.Controller : Slider bringToFront() 
controlP5.Controller : Slider bringToFront(ControllerInterface) 
controlP5.Controller : Slider hide() 
controlP5.Controller : Slider linebreak() 
controlP5.Controller : Slider listen(boolean) 
controlP5.Controller : Slider lock() 
controlP5.Controller : Slider plugTo(Object) 
controlP5.Controller : Slider plugTo(Object, String) 
controlP5.Controller : Slider plugTo(Object[]) 
controlP5.Controller : Slider plugTo(Object[], String) 
controlP5.Controller : Slider registerProperty(String) 
controlP5.Controller : Slider registerProperty(String, String) 
controlP5.Controller : Slider registerTooltip(String) 
controlP5.Controller : Slider removeBehavior() 
controlP5.Controller : Slider removeCallback() 
controlP5.Controller : Slider removeCallback(CallbackListener) 
controlP5.Controller : Slider removeListener(ControlListener) 
controlP5.Controller : Slider removeProperty(String) 
controlP5.Controller : Slider removeProperty(String, String) 
controlP5.Controller : Slider setArrayValue(float[]) 
controlP5.Controller : Slider setArrayValue(int, float) 
controlP5.Controller : Slider setBehavior(ControlBehavior) 
controlP5.Controller : Slider setBroadcast(boolean) 
controlP5.Controller : Slider setCaptionLabel(String) 
controlP5.Controller : Slider setColor(CColor) 
controlP5.Controller : Slider setColorActive(int) 
controlP5.Controller : Slider setColorBackground(int) 
controlP5.Controller : Slider setColorCaptionLabel(int) 
controlP5.Controller : Slider setColorForeground(int) 
controlP5.Controller : Slider setColorValueLabel(int) 
controlP5.Controller : Slider setDecimalPrecision(int) 
controlP5.Controller : Slider setDefaultValue(float) 
controlP5.Controller : Slider setHeight(int) 
controlP5.Controller : Slider setId(int) 
controlP5.Controller : Slider setImages(PImage, PImage, PImage) 
controlP5.Controller : Slider setImages(PImage, PImage, PImage, PImage) 
controlP5.Controller : Slider setLabelVisible(boolean) 
controlP5.Controller : Slider setLock(boolean) 
controlP5.Controller : Slider setMax(float) 
controlP5.Controller : Slider setMin(float) 
controlP5.Controller : Slider setMouseOver(boolean) 
controlP5.Controller : Slider setMoveable(boolean) 
controlP5.Controller : Slider setPosition(PVector) 
controlP5.Controller : Slider setPosition(float, float) 
controlP5.Controller : Slider setSize(PImage) 
controlP5.Controller : Slider setSize(int, int) 
controlP5.Controller : Slider setStringValue(String) 
controlP5.Controller : Slider setUpdate(boolean) 
controlP5.Controller : Slider setValueLabel(String) 
controlP5.Controller : Slider setView(ControllerView) 
controlP5.Controller : Slider setVisible(boolean) 
controlP5.Controller : Slider setWidth(int) 
controlP5.Controller : Slider show() 
controlP5.Controller : Slider unlock() 
controlP5.Controller : Slider unplugFrom(Object) 
controlP5.Controller : Slider unplugFrom(Object[]) 
controlP5.Controller : Slider unregisterTooltip() 
controlP5.Controller : Slider update() 
controlP5.Controller : Slider updateSize() 
controlP5.Controller : String getAddress() 
controlP5.Controller : String getInfo() 
controlP5.Controller : String getName() 
controlP5.Controller : String getStringValue() 
controlP5.Controller : String toString() 
controlP5.Controller : Tab getTab() 
controlP5.Controller : boolean isActive() 
controlP5.Controller : boolean isBroadcast() 
controlP5.Controller : boolean isInside() 
controlP5.Controller : boolean isLabelVisible() 
controlP5.Controller : boolean isListening() 
controlP5.Controller : boolean isLock() 
controlP5.Controller : boolean isMouseOver() 
controlP5.Controller : boolean isMousePressed() 
controlP5.Controller : boolean isMoveable() 
controlP5.Controller : boolean isUpdate() 
controlP5.Controller : boolean isVisible() 
controlP5.Controller : float getArrayValue(int) 
controlP5.Controller : float getDefaultValue() 
controlP5.Controller : float getMax() 
controlP5.Controller : float getMin() 
controlP5.Controller : float getValue() 
controlP5.Controller : float[] getArrayValue() 
controlP5.Controller : int getDecimalPrecision() 
controlP5.Controller : int getHeight() 
controlP5.Controller : int getId() 
controlP5.Controller : int getWidth() 
controlP5.Controller : int listenerSize() 
controlP5.Controller : void remove() 
controlP5.Controller : void setView(ControllerView, int) 
java.lang.Object : String toString() 
java.lang.Object : boolean equals(Object) 


*/



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DashExperiment" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
