package Reseau;


import java.io.*;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.comm.*;
import Principal.Information;

public class ReseauManager {

	private NXTConnection connection;
	private String nomTelephone;
	protected DataOutputStream dataOut;
	protected InputStream dataIn;
	
	

	public void setConnection(NXTConnection n){
		this.connection = n;
	}
	
	public NXTConnection getConnection(){
		return this.connection;
	}

	
	

	public void setNomTelephone(String n){
		this.nomTelephone = n;
	}
	
	public String getNomTelephone(){
		return this.nomTelephone;
	}

	

	public void setdataOut(DataOutputStream d){
		this.dataOut = d;
	}
	
	public DataOutputStream getdataOut(){
		return this.dataOut;
	}
	
	
	public void setdataIn(InputStream d){
		this.dataIn = d;
	}
	
	public InputStream getdataIn(){
		return this.dataIn;
	}
	
	
	
	
	public ReseauManager(){
		 connection = null;
		 nomTelephone ="";
		 dataOut = null;
		 dataIn = null;
	}
	
	
	
	@SuppressWarnings("finally")
	public boolean connexion(){
		boolean resultat = false;
		try{
			LCD.clear();
			LCD.drawString("En Attente de ",1,1);
			LCD.drawString("connection",2,2);
		if (( connection = Bluetooth.waitForConnection() ) != null  ){
			dataOut = connection.openDataOutputStream();
			dataIn = connection.openInputStream();
			LCD.clear();
			LCD.drawString("Connecter",1,1);
			resultat = true ;
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			return resultat;
		}
	}
	
	
	
	public boolean deconnexion() throws IOException{
		boolean resultat = false;
		if(connection != null){
			dataOut.flush();
			dataOut.close();
			dataIn.close();
			connection.close();
			resultat=true;
		}
		return resultat;
	}
	
	
	
	public void envoyerInformation(){
		Information info = new Information();
		
		try {
			dataOut.writeChars(info.ToString());
			dataOut.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void envoyerAccelerationMotorA(){
		try {
			dataOut.writeChars("A :"+Motor.A.getAcceleration());
			dataOut.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void envoyerAccelerationMotorB(){
		try {
			dataOut.writeChars("B :"+Motor.B.getAcceleration());
			dataOut.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
