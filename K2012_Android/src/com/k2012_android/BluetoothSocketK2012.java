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
	public BluetoothSocket socket_nxt;
	public BluetoothDevice nxt;

	OutputStream outStream;
	public InputStream inStream;
	private MainActivity mainActivity;
	private boolean isRunning = false;
	
	public BluetoothSocketK2012(MainActivity mainActivity ){

		this.mainActivity = mainActivity;
		//Récupère le téléphone pour utilise son bluetooth
	    localAdapter = BluetoothAdapter.getDefaultAdapter();
	    
	    //Permet d'activer le BT s'il n'est pas activé
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
			       
			       String str = new String(bs);
			       Log.d("K2012", str);
			       double[] db = toDouble(str);
			       
			       mainActivity.display(db);
			       
			} catch (IOException e) {
				Log.e("K2012", e.getMessage());
			}
		}
		try{
			outStream.close();
			inStream.close();
			socket_nxt.close();
		}
		catch(Exception e){
	       Log.e("K2012", e.getMessage());
		}
	}
	
	public void activerBT(){
	    //If Bluetooth not enable then do it
	    if(localAdapter != null){
		    if(localAdapter.isEnabled()==false){
		        localAdapter.enable();    
		        try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
		    }
	    }
	}
	public void deconnected(){
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

	private byte[] toByteArray(double[] values) {
		//Ecris un string pour la serialisation
		DecimalFormat df = new DecimalFormat("0.000"); 
	    String msg = String.format("%s;%s",df.format(values[0]),df.format(values[1]));
	    byte[] bytes = msg.getBytes();
	    return bytes;
	}
	
	
	public void connect()
	{
		try {
	    	//Récupère l'appareil ayant cette adresse mac
	    	nxt = localAdapter.getRemoteDevice("00:16:53:00:FC:30");
	    	
	    	//Créer la connexion grâce à l'adresse du port série
			socket_nxt = nxt.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			
			//annule la recherche par bluetooth
			localAdapter.cancelDiscovery();
			
			//Etablit la connexion
			socket_nxt.connect();
			//Récupère le stream entrant
			inStream = socket_nxt.getInputStream();

			//Récupère le stream sortant
			outStream = socket_nxt.getOutputStream();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean IsRunning(){
		return this.isRunning;
	}
	
	public static double[] toDouble(String str) {		
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
