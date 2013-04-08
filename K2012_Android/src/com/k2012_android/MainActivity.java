package com.k2012_android;

import java.io.IOException;
import java.text.DecimalFormat;

import org.jfree.util.Log;
import com.k2012_android.R;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends Activity {

	//Variable qui vont contenir les éléments présents dans activity_main
	Button stop = null;
	Button klaxonN = null;
	Button klaxonT = null;
	Button klaxonP = null;
	ImageView ring = null;
	TextView textPuissance = null;	
	Switch switchConnexion = null;
	public static TextView textDistance = null;
	public static TextView textVitesse = null;
	
	int timeActivity = 4; //Temps d'affichage des toast
	float rayon = 150f;//Rayon du cercle
	int puissanceMax = 1000;//Puissance maximale des moteurs
	BluetoothSocketK2012  bluetoothSoc = null; //Socket permettant la connexion
	int wait = 150;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Méthodes de base de création de la fenêtre
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		//Récupération des éléments
		stop = (Button)findViewById(R.id.button1);
		klaxonN = (Button)findViewById(R.id.button2);
		klaxonT = (Button)findViewById(R.id.button3);
		klaxonP = (Button)findViewById(R.id.button4);
		ring = (ImageView)findViewById(R.id.imageView1);		
		textPuissance = (TextView)findViewById(R.id.textView7);	
		textDistance = (TextView)findViewById(R.id.textView5);	
		textVitesse = (TextView)findViewById(R.id.textView3);		
		switchConnexion = (Switch)findViewById(R.id.switch1);
		
		//Affetion des listeners
		stop.setOnClickListener(buttonTouched);
		klaxonN.setOnClickListener(buttonKlaxonN);
		klaxonT.setOnClickListener(buttonKlaxonT);
		klaxonP.setOnClickListener(buttonKlaxonP);
		ring.setOnTouchListener(touched);		
		switchConnexion.setOnCheckedChangeListener(switchTouched);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private OnClickListener buttonTouched = new OnClickListener(){
		@Override
		public void onClick(View v) {
			double[] info = { 1664.00, 0.00};
			if (bluetoothSoc != null){				
				try {
					bluetoothSoc.outStream.write(toByteArray(info),0,toByteArray(info).length);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						bluetoothSoc.outStream.flush();
						Thread.sleep(wait);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			Toast.makeText(MainActivity.this, "Voiture arrêtée !", timeActivity).show();		
		}
	};
	
	private OnClickListener buttonKlaxonN = new OnClickListener(){
		@Override
		public void onClick(View v) {
			double[] info = { 8888.00, 0.00};
			if (bluetoothSoc != null){				
				try {
					bluetoothSoc.outStream.write(toByteArray(info),0,toByteArray(info).length);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						bluetoothSoc.outStream.flush();
						Thread.sleep(wait);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private OnClickListener buttonKlaxonT = new OnClickListener(){
		@Override
		public void onClick(View v) {
			double[] info = { 7777.00, 0.00};
			if (bluetoothSoc != null){				
				try {
					bluetoothSoc.outStream.write(toByteArray(info),0,toByteArray(info).length);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						bluetoothSoc.outStream.flush();
						Thread.sleep(wait);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private OnClickListener buttonKlaxonP = new OnClickListener(){
		@Override
		public void onClick(View v) {
			double[] info = { 6666.00, 0.00};
			if (bluetoothSoc != null){				
				try {
					bluetoothSoc.outStream.write(toByteArray(info),0,toByteArray(info).length);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						bluetoothSoc.outStream.flush();
						Thread.sleep(wait);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private OnCheckedChangeListener switchTouched = new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if(switchConnexion.isChecked())	{
				bluetoothSoc = new BluetoothSocketK2012();
				
				Thread th = new Thread(bluetoothSoc);					
				th.start();
			}
			else{
				if(bluetoothSoc != null){
					try {					
						bluetoothSoc.outStream.close();
						bluetoothSoc.inStream.close();
						bluetoothSoc.socket_nxt.close();
					} catch (IOException e) {
						Log.error("Error Bluetooth : ", e);
					}finally{
						bluetoothSoc = null;					
					}
				}
			}
		}		
	};
	
	private OnTouchListener touched = new OnTouchListener(){
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			float x = event.getX() - rayon;
			float y = event.getY() - rayon;
			
			//Mise à niveau pour le répère orthonormal
			y = -y;
			
			if(isInCircle(x, y, rayon)){
				//Calcul de la puissance
				double puissance = (y * puissanceMax)/rayon;
				
				//Calcul de l'angle
				double angle = Math.toDegrees(Math.atan(x/y));

				textPuissance.setText(" "+puissance);
				
				double[] info = {puissance, angle};
				
				//TODO Ajouter une vérification pour savoir si on est toujours connecté
				if (bluetoothSoc != null){
					
					try {
						bluetoothSoc.outStream.write(toByteArray(info),0,toByteArray(info).length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						try {
							bluetoothSoc.outStream.flush();
							Thread.sleep(wait);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else{
					switchConnexion.setPressed(false);
				}
			}
			return true;
		}		
	};
	
	private boolean isInCircle(float x, float y, float rayon){
		//Vérifie que le point est bien dans le cercle
		double result = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		//Si le résultat est inférieur au rayon alors il est à l'intérieur
		if((float)result < rayon){
			return true;
		}else{
			return false;
		}
	}
	
	public byte[] toByteArray(double[] values) {
		//Ecris un string pour la serialisation
		DecimalFormat df = new DecimalFormat("0.000"); 
	    String msg = String.format("%s;%s",df.format(values[0]),df.format(values[1]));
	    byte[] bytes = msg.getBytes();
	    return bytes;
	}

}
