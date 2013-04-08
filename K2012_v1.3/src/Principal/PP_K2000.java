package Principal;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.TouchFeatureDetector;

import Mouvement.*;

public class PP_K2000 {

	//Eléments utilisés dans manoeuvre
	public static double distance = 0.0;
	public static DataOutputStream dos = null;
	public static boolean isrunning = true;

	public static void main(String[] args) throws IOException{		
		
		//Elément par lequel on fait appel à tous les mouvements
		final Manoeuvre man = new Manoeuvre();
		
		//Partie Sensor
		TouchSensor touchS = new TouchSensor(SensorPort.S4);
        FeatureDetector touchD = new TouchFeatureDetector(touchS, 10, 10);
        touchD.addListener(new FeatureListener() {
        	//Evénement pourle sensor s'il touche on envoie une vitesse de 1664 : STOP
            public void featureDetected(Feature ftr, FeatureDetector fd) {
				man.deplacement(1664.00, 0.00);
            }
        });
		//Fin de partie Sensor

		try{			
		    LCD.clear();
		    LCD.drawString("Bienvenue",0,0);
			Thread.sleep(2000);
		    LCD.clear();
		
			//Sysème d'écriture : ("Texte", Colonne, Ligne)
			LCD.drawString("En attente",0,0);
			LCD.drawString("de connexion",0,1);

		    LCD.refresh();

		    //Attend une entrée/connexion bluetooth
		    NXTConnection btc = Bluetooth.waitForConnection();

		    //Etablie la connexion bluetooth pour un appareil autre que LEJOS
		    btc.setIOMode(NXTConnection.RAW);
		    
		    //Affichage qui ne se voit pas sauf ralentissement de l'ouverture du tunnel
		    LCD.clear();
		    LCD.drawString("Connecte",0,0);
		    LCD.drawString(btc.getAddress(),0,1);
		    Thread.sleep(2000);

		    //Le dataInputStream dans lequel les données vont arriver
		    DataInputStream dis=btc.openDataInputStream();	
		    
		    //Le dataOutputStream par lequel on va envoyer des données
		    dos = btc.openDataOutputStream();

	    	LCD.clear();
	    	
		    //Boucle principale  
		    while(isrunning){
		    	System.out.println("Attente donnees");
			   			   
			   	//Compte le nombre d'Octets arrivant sur le dis
		       	int count = dis.available();
		      
		       	//Création du buffer
		       	byte[] bs = new byte[count];
		       
		       	//Mise en place des données dans le buffer
		       	dis.read(bs);
		       
		       	//Transformation des données buffer en string par parsage
		       	String str = new String(bs);
		       
		       	//Parsage du string en double
		       	double[] db = toDouble(str);
		       	
		       	//Réalisation du déplacement
		       	man.deplacement(db[0], db[1]);
		    }
		    
		    //Fermeture du tunnel
		    LCD.clear();
		    LCD.drawString("Fin de",0,0);
		    LCD.drawString("transmission",0,1);
		   	Thread.sleep(2000);
		    dis.close();	 					
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
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
