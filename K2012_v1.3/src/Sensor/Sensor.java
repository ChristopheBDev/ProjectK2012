/***
ProjectK2012\K2012_v1.3
By
   Christophe Bocher
   Stanislas Le Guennec
***/
package Sensor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class Sensor {

	private SensorPort sp ;
	private TouchSensor touch;
	
	public Sensor(){
		sp = SensorPort.getInstance(3);
		touch = new TouchSensor(sp);		
	}
	
	public TouchSensor getTouchSensor(){
		return this.touch;
	}
	
	public boolean toucher(){
		boolean toucher = false;
		if (getTouchSensor().isPressed()){
			toucher = true;			
		}
		return toucher;	
	}	
}
