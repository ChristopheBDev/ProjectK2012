package com.k2012_android;

import java.io.*;
import java.util.UUID;
import org.jfree.util.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.view.KeyEvent.DispatcherState;
import android.widget.Toast;

public class BluetoothSocketK2012 implements Runnable{

	//Target NXT
	final String strnxt = "Nelthent";

	BluetoothAdapter localAdapter;
	public BluetoothSocket socket_nxt;
	public BluetoothDevice nxt;

	public OutputStream outStream;
	public InputStream inStream;
	
	public BluetoothSocketK2012(){

		//Récupère le téléphone pour utilise son bluetooth
	    localAdapter = BluetoothAdapter.getDefaultAdapter();
	    
	    //Permet d'activer le BT s'il n'est pas activé
		activerBT();
	    
	    try{
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
			
	    }catch(Exception e){
	    	Log.error("Error Bluetooth : ", e);
	    }
	}

	@Override
	public void run() {
		try {
				// count the available bytes form the input stream
		       int count = inStream.available();
		      
		       // create buffer
		       byte[] bs = new byte[count];
		       
		       // read data into buffer
		       inStream.read(bs);
		       
		       String str = new String(bs);
		       
		       double[] db = toDouble(str);
		       
		       
		       /*
		       synchronized (MainActivity.textDistance) {
			       MainActivity.textDistance.setText(" " + db[1] + " m");
			}
		       synchronized (MainActivity.textVitesse) {
			       MainActivity.textVitesse.setText(db[0] + " m/s");
			}
		       //MainActivity.textVitesse.setText(str);*/
		       
		} catch (IOException e) {
			e.printStackTrace();
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
