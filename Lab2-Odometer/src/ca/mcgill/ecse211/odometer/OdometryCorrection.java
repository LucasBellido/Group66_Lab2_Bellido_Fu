/*
 * OdometryCorrection.java
 */
package ca.mcgill.ecse211.odometer;

import lejos.hardware.Sound;
import java.lang.Math;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class OdometryCorrection implements Runnable {
  private static final long CORRECTION_PERIOD = 10;
  private Odometer odometer;
    private int xCounter =0;
    private int yCounter =0;
    
    private static Port portColor;
    private static SensorModes myColor;
    private static float[] sampleColor;
    private static SampleProvider myColorSample;
    
    
    private static double myColorSensorValue;
    private static int sampleCounter;
  /**
   * This is the default class constructor. An existing instance of the odometer is used. This is to
   * ensure thread safety.
   * 
   * @throws OdometerExceptions
   */
    public OdometryCorrection(Odometer odometer, Port portColor, float[] sampleColor, SensorModes myColor, SampleProvider myColorSample) throws OdometerExceptions {

        this.odometer = odometer;
        this.portColor = portColor;
        this.sampleColor = sampleColor;
        this.myColor = myColor;
        this.myColorSample = myColorSample;

  }

  /**
   * Here is where the odometer correction code should be run.
   * 
   * @throws OdometerExceptions
   */
  // run method (required for Thread)
    
  public void run() {
    long correctionStart, correctionEnd;
    while(true) {
    		correctionStart = System.currentTimeMillis();
    		double distanceFromTrack = 4.9;
    	    double positions[] = odometer.getXYT();
    	    double thetaCorrection = positions[2];
    	        
    	    myColorSample.fetchSample(sampleColor, 0);
    	    myColorSensorValue = sampleColor[0]*1000;
    	    
    	    
    	    if (myColorSensorValue <230) {
    	    	Sound.beep();
    	    	sampleCounter++;
    	    	System.out.println(sampleCounter);
    	    }
    	    
    	        
        //use an average of samples && use absolute value
        //if (sensor value between a specific range where there is a line)
        //then 4 cases for each direction east,west,north,south
        
        // odometer.setX(10);

      // this ensure the odometry correction occurs only once every period
      correctionEnd = System.currentTimeMillis();
      if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
        try {
          Thread.sleep(CORRECTION_PERIOD - (correctionEnd - correctionStart));
        } catch (InterruptedException e) {
          // there is nothing to be done here
        }
      }
    }
  }
}
