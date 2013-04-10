package Mouvement;

import java.io.DataOutputStream;

import Principal.PP_K2000;
import lejos.nxt.*;

public class Manoeuvre {
	
	private float coefEcartRoues = 0.15F;
	private int volume = 10;
	private boolean son = false;
	private Thread playMusic;
	DataOutputStream dos = null;

	public Manoeuvre(DataOutputStream dos){
		
		this.dos = dos;
		
		//Permet de régler la vitesse d'accélération
		/*
		Motor.A.setAcceleration(250);
		Motor.B.setAcceleration(250);
		*/
	}	
	
	//Méthode principale d'une manoeuvre
	public void deplacement(double puissance, double angle)
	{	
		LCD.clear();
		
		if(puissance != 0 && puissance < 1000.00)
		{
			if(angle > 0) //Tourne à droite
			{
				LCD.drawString("Direction :", 0, 1);
				LCD.drawString("droite", 0, 2);
				LCD.drawString("Angle : ", 0, 3);
				LCD.drawString("" + angle, 0, 4);
				
				//Calcul des vitesses
				double vitesse = (2 * Math.PI * puissance * coefEcartRoues)/(360/angle);	
				vitesse = vitesse + puissance;
				
				//Assignement des vitesses
				Motor.A.setSpeed((float)vitesse);
				Motor.B.setSpeed((float)puissance);
				
				
			}else if(angle < 0) //Tourne à gauche
			{
				LCD.drawString("Direction :", 0, 1);
				LCD.drawString("gauche", 0, 2);
				LCD.drawString("Angle : ", 0, 3);
				LCD.drawString("" + angle, 0, 4);
				
				//Calcul des vitesses
				double vitesse = (2 * Math.PI * puissance * coefEcartRoues)/(360/(-(angle)));					
				vitesse = vitesse + puissance;			
				
				//Assignement des vitesses
				Motor.A.setSpeed((float)puissance);		
				Motor.B.setSpeed((float)vitesse);
				
			}
			else // Va tout droit
			{				
				//Assignement des vitesses
				Motor.A.setSpeed((float)puissance);		
				Motor.B.setSpeed((float)puissance);	
			}	
			//Lancement des moteurs
			if(puissance<0){
				LCD.drawString("Avancer", 0, 0);
				Motor.A.backward();
				Motor.B.backward();
			}else{
				LCD.drawString("Reculer", 0, 0);
				Motor.A.forward();
				Motor.B.forward();
			}
		}
		else if(puissance == 1664.00)// Pas de puissance donc arrêt du véhicule
		{
			LCD.drawString("Arret", 0, 1);
			
			puissance = 0.0;
			
			//Arrêt de la musique
			son = false;
			if(playMusic != null && playMusic.isAlive())
			playMusic.interrupt();
			
			//Arrête les moteurs de façon ralentie
			Motor.B.flt();
			Motor.A.flt();			
		}
		else if(puissance == 8888.00){
			if(son == false)
			{
				son = true;
				playMusic = new Thread(new Runnable() {
					@Override
					public void run() {
						soundNyanCat();
						son = false;
						return;
					}
				});
				playMusic.start();
			}
			puissance = 0.0;
			   
		}else if(puissance == 6666.00){
			if(son == false)
			{
				son = true;
				playMusic = new Thread(new Runnable() {			
					@Override
					public void run() {
						soundPapaNoel();
						son = false;
						return;
					}
				});
				playMusic.start();
			}			
			puissance = 0.0;
			
		}else if(puissance == 7777.00){
			if(son == false)
			{
				son = true;
				playMusic = new Thread(new Runnable() {			
					@Override
					public void run() {
						soundTetris();
						son = false;
						return;
					}
				});
				playMusic.start();
			}
			puissance = 0.0;
		}
		
		//Calcul de la vitesse en m/s
		double[] tab = {(0.0055 * puissance)/10, PP_K2000.getDistance()};
		//Renvoie de la vitesse en m/s
		try {
			byte[] byt = toByteArray(tab);

			this.dos.write(byt, 0, byt.length);
			this.dos.flush();
			
		} catch (Exception e) {
			LCD.clear();
			PP_K2000.setIsrunning(false);
			
			//Arrête les moteurs de façon ralentie
			Motor.B.flt();
			Motor.A.flt();
		}
		
	}	
	
	public byte[] toByteArray(double[] values) {
		//Ecrit un string pour la serialisation
		String msg = new String(values[0] + ";" + values[1]);
	    byte[] bytes = msg.getBytes();
	    return bytes;
	}
	
	private void soundNyanCat(){
		Sound.setVolume(volume);				
		   
	    int si = 987;
	    int d1 = 1108;
	    int re1 = 1244;
	    int fa1 = 1479;
	    int sol1 = 1661;
	    int reb = 1174;
	    int c = 227;
	    int d = 113;
	    try{
			Sound.playNote(Sound.PIANO, fa1 ,c);
		    Sound.playNote(Sound.PIANO, sol1 ,c);
		    Sound.playNote(Sound.PIANO, reb ,d);Thread.sleep(10);
		    Sound.playNote(Sound.PIANO, re1 ,c);
		    Sound.playNote(Sound.PIANO, si ,d);
	        Sound.playNote(Sound.PIANO, reb ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, si ,c);Thread.sleep(10);
	        Sound.playNote(Sound.PIANO, si ,c);
	        Sound.playNote(Sound.PIANO,  d1,c);
	
	        Sound.playNote(Sound.PIANO, reb ,c);Thread.sleep(10);
	        Sound.playNote(Sound.PIANO, reb ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, si ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, re1 ,d);
	        Sound.playNote(Sound.PIANO, fa1 ,d);
	        Sound.playNote(Sound.PIANO, sol1 ,d);
	        Sound.playNote(Sound.PIANO, re1 ,d);
	        Sound.playNote(Sound.PIANO, fa1 ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, re1 ,d);
	        Sound.playNote(Sound.PIANO, si ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, si ,d);
	
	
	
	        Sound.playNote(Sound.PIANO, re1 ,c);
	        Sound.playNote(Sound.PIANO, fa1 ,c);
	        Sound.playNote(Sound.PIANO, sol1 ,d);
	        Sound.playNote(Sound.PIANO, re1 ,d);
	        Sound.playNote(Sound.PIANO, fa1 ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, re1 ,d);
	        Sound.playNote(Sound.PIANO, si ,d);
	        Sound.playNote(Sound.PIANO, reb ,d);
	        Sound.playNote(Sound.PIANO, re1 ,d);
	        Sound.playNote(Sound.PIANO, reb ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, si ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	
	        Sound.playNote(Sound.PIANO, reb ,c);
	        Sound.playNote(Sound.PIANO, si ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, reb ,d);
	        Sound.playNote(Sound.PIANO, fa1 ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, reb ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, si ,d);
	        Sound.playNote(Sound.PIANO, d1 ,c);
	        Sound.playNote(Sound.PIANO, si ,c);
	        Sound.playNote(Sound.PIANO, d1 ,c);
	
	        Sound.playNote(Sound.PIANO, fa1 ,c);
	        Sound.playNote(Sound.PIANO, sol1 ,c);
	        Sound.playNote(Sound.PIANO, reb ,d);
	        Sound.playNote(Sound.PIANO, re1 ,c);
	        Sound.playNote(Sound.PIANO, si ,d);
	        Sound.playNote(Sound.PIANO, reb ,d);
	        Sound.playNote(Sound.PIANO, d1 ,d);
	        Sound.playNote(Sound.PIANO, si ,c);Thread.sleep(10);
	        Sound.playNote(Sound.PIANO, si ,c);
	        Sound.playNote(Sound.PIANO, d1 ,c);
	    }catch(Exception e){
	    	
	    }
	    
	Sound.setVolume(0);

	}
		
	private void soundPapaNoel(){
		Sound.setVolume(volume);
		try{
			int re = 587;
		    int mi = 659;
		    int fa = 740;
		    int sol = 784;
		    int la = 880;
		    int si = 988;
		    int d = 1046;

	        Sound.playNote(Sound.PIANO,sol,500);
	        Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,sol,500);
	        Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,sol,500);
	        Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,la,500);
	        Sound.playNote(Sound.PIANO,sol,1500);
	        Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,sol,250);
	        Sound.playNote(Sound.PIANO,la,250);
	        Sound.playNote(Sound.PIANO,si,500);
	        Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,si,500);
	        Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,si,500);
	        Sound.playNote(Sound.PIANO,d,500);
	        Sound.playNote(Sound.PIANO,si,1500);
	        Thread.sleep(250);
	        Sound.playNote(Sound.PIANO,la,250);
	        Sound.playNote(Sound.PIANO,sol,750);Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,sol,250);Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,sol,250);Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,sol,250);
	        Sound.playNote(Sound.PIANO,fa,250);
	        Sound.playNote(Sound.PIANO,mi,250);
	        Sound.playNote(Sound.PIANO,re,1500);Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,re,250);Thread.sleep(50);
	        Sound.playNote(Sound.PIANO,re,250);
	        

            Sound.playNote(Sound.PIANO,sol,1000);Thread.sleep(50);
            Sound.playNote(Sound.PIANO,sol,250);Thread.sleep(50);
            Sound.playNote(Sound.PIANO,sol,250);
            Sound.playNote(Sound.PIANO,fa,250);
            Sound.playNote(Sound.PIANO,sol,250);
            Sound.playNote(Sound.PIANO,la,1500);
            Sound.playNote(Sound.PIANO,re,500);

		}catch(Exception e){
			
		}

		Sound.setVolume(0);
	}

	private void soundTetris(){
		Sound.setVolume(volume);
		 int ssol = 392;
		 int la = 440;
		 int si =  466;     //bemol
		 int doo = 523;
		 int re = 587;
		 int mi =  622;       //bemol
		 int fa = 698;
		 int sol = 783;

		 int n = 250;
		 int c = n/2;
		 int d = n/4;
		 int b = 2*n ;
		 int s = 10;

		try{
		    //ligne 1

		    Sound.playNote(Sound.PIANO, re,n);
		    Sound.playNote(Sound.PIANO, la,c);
		    Sound.playNote(Sound.PIANO, si,c);
		    Sound.playNote(Sound.PIANO, doo,c);
		    Sound.playNote(Sound.PIANO, re,d);
		    Sound.playNote(Sound.PIANO, doo,d);
		    Sound.playNote(Sound.PIANO, si,c);
		    Sound.playNote(Sound.PIANO, la,c);

		    Sound.playNote(Sound.PIANO, ssol,n);Thread.sleep(s);
		    Sound.playNote(Sound.PIANO, ssol,c);
		    Sound.playNote(Sound.PIANO, si,c);
		    Sound.playNote(Sound.PIANO, re,n);
		    Sound.playNote(Sound.PIANO, doo,c);
		    Sound.playNote(Sound.PIANO, si,c);

		    Sound.playNote(Sound.PIANO, la,n+c);
		    Sound.playNote(Sound.PIANO, si,c);
		    Sound.playNote(Sound.PIANO, doo,n);
		    Sound.playNote(Sound.PIANO, re,n);

		    Sound.playNote(Sound.PIANO, si,n);
		    Sound.playNote(Sound.PIANO, ssol,n);Thread.sleep(s);
		    Sound.playNote(Sound.PIANO, ssol,b);

		    Thread.sleep(c);
		    Sound.playNote(Sound.PIANO, doo,n);
		    Sound.playNote(Sound.PIANO, mi,c);
		    Sound.playNote(Sound.PIANO, sol,n);
		    Sound.playNote(Sound.PIANO, fa,c);
		    Sound.playNote(Sound.PIANO, mi,c);

		    //ligne 2

		    Sound.playNote(Sound.PIANO, re,n+c);
		    Sound.playNote(Sound.PIANO, si,c);
		    Sound.playNote(Sound.PIANO, re,n);
		    Sound.playNote(Sound.PIANO, doo,c);
		    Sound.playNote(Sound.PIANO, si,c);

		    Sound.playNote(Sound.PIANO, la,n);Thread.sleep(s);
		    Sound.playNote(Sound.PIANO, la,c);
		    Sound.playNote(Sound.PIANO, si,c);
		    Sound.playNote(Sound.PIANO, doo,n);
		    Sound.playNote(Sound.PIANO, re,n);

		    Sound.playNote(Sound.PIANO, si,n);
		    Sound.playNote(Sound.PIANO, ssol,n);
		    Sound.playNote(Sound.PIANO, ssol,n);
		    
		}catch(Exception e){
	    	
	    }
		Sound.setVolume(0);
	}
		
}
