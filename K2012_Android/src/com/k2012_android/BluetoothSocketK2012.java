/***
ProjectK2012\K2012_Android
By
   Christophe Bocher
   Stanislas Le Guennec
***/
package com.k2012_android;

import java.io.*;
import java.text.DecimalFormat;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothSocketK2012 implements Runnable{

	//Target NXT
	final String strnxt = "Nelthent";

	BluetoothAdapter localAdapter;
	BluetoothSocket socket_nxt;
	BluetoothDevice nxt;

	OutputStream outStream;
	InputStream inStream;
	MainActivity mainActivity;
	boolean isRunning = false;
	
	public BluetoothSocketK2012(MainActivity mainActivity ){

		this.mainActivity = mainActivity;
		//R�cup�re le t�l�phone pour utilise son bluetooth
	    localAdapter = BluetoothAdapter.getDefaultAdapter();
	    
	    //Permet d'activer le BT s'il n'est pas activ�
		activerBT();
	    
	}

	@Override
	public void run() {
	       Log.d("K2012", "Lancement du thread");
	       isRunning= true;
		while(isRunning){
			try {
					// count the available bytes form the input stream
			       int count = inStream.available();
			      
			       // create buffer
			       byte[] bs = new byte[count];
			       
			       // read data into buffer
			       inStream.read(bs);
			       
			       //Connversion bytes->String->double[]
			       String str = new String(bs);
			       double[] db = toDouble(str);
			       
			       //Appel de la m�thode d'affichage
			       mainActivity.display(db);
			       
			} catch (IOException e) {
				Log.e("K2012", e.getMessage());
			}
		}
		try{
			//Fermeture des flux
			outStream.close();
			inStream.close();
			socket_nxt.close();
		}
		catch(Exception e){
	       Log.e("K2012", e.getMessage());
		}
	}
	
	public void connect()
	{
		try {
	    	//R�cup�re l'appareil ayant cette adresse mac
	    	nxt = localAdapter.getRemoteDevice("00:16:53:00:FC:30");
	    	
	    	//Cr�er la connexion gr�ce � l'adresse du port s�rie
			socket_nxt = nxt.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			
			//annule la recherche par bluetooth
			localAdapter.cancelDiscovery();
			
			//Etablit la connexion
			socket_nxt.connect();
			//R�cup�re le stream entrant
			inStream = socket_nxt.getInputStream();

			//R�cup�re le stream sortant
			outStream = socket_nxt.getOutputStream();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void disconnect(){
		this.isRunning = false;

		double[] info = { 1664.00, 0.00};
		try {
			send(info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(double[] tmp) throws IOException{
			outStream.write(toByteArray(tmp), 0, toByteArray(tmp).length);
	}

	public boolean IsRunning(){
		return this.isRunning;
	}
	
	private void activerBT(){
	    //Sit le bluetooth n'est pas activ� on l'active
	    if(localAdapter != null){
		    if(localAdapter.isEnabled()==false){
		        localAdapter.enable();    
		        try {
		        	//Processus bloquant pour �viter d'utiliser le BT pendant son activation
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					Log.e("K2012", e.getMessage());
				}
		    }
	    }
	}
	
	private byte[] toByteArray(double[] values) {
		//Ecris un string pour la serialisation
		DecimalFormat df = new DecimalFormat("0.000"); 
	    String msg = String.format("%s;%s",df.format(values[0]),df.format(values[1]));
	    byte[] bytes = msg.getBytes();
	    return bytes;
	}
		
	private double[] toDouble(String str) {		
		double[] doubles = new double[2];
		String s1 = "";
		String s2 = "";
		boolean first = true;
		for(int i = 0; i<str.length(); i++)
		{
			if(str.charAt(i) != ';'){
				if(first){		
					if(str.charAt(i) == ','){
						s1 += '.';
					}else{
						s1 +=str.charAt(i);
					}
				}else{
					if(str.charAt(i) == ','){
						s2 += '.';
					}else{
						s2 +=str.charAt(i);
					}
				}
			}else{
				first = false;
			}
		}
		if(!s1.equals("") || !s2.equals("")){
			doubles[0] = Double.parseDouble(s1);
			doubles[1] = Double.parseDouble(s2);		
		}		
	    return doubles;
	}
}
