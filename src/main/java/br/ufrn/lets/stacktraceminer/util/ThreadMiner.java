package br.ufrn.lets.stacktraceminer.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *  * @author Teresa Fernandes
 */
public class ThreadMiner implements Runnable{
	
	/** Reader of console input.*/
    public  BufferedReader in ;
    /** flag to identify the main thread principal that will be stopped*/
    public  boolean quit=false;
    /**
     * Start an thread that inspect if the user wish stop the execution of data mining,
     * making possible the program stores the stack trace records identified until the moment.
     * */
	public void run(){
        String msg = null;
         
        // threading is waiting for the key Q to be pressed 
        while(true){
            try{
            msg=in.readLine();
            }catch(IOException e){
            	e.printStackTrace();
            }             
            if(msg.equals("Q")) {quit=true;break;}
        }
    }
	public BufferedReader getIn() {
		return in;
	}
	public void setIn(BufferedReader in) {
		this.in = in;
	}
	public boolean isQuit() {
		return quit;
	}
	public void setQuit(boolean quit) {
		this.quit = quit;
	}
	
	
}
