

import java.io.IOException;

import br.ufrn.lets.stacktraceminer.stackoverflow.StackoverflowDumpXmlManager;
 
/**
 * 
 * @author Teresa Fernandes
 */
public class Main{
 
	public static void main(String[] args) throws IOException  {
		long tempoInicial = System.currentTimeMillis();

		StackoverflowDumpXmlManager.getInstance().startExtraction();
		
		System.out.println("Tempo de execução:" + (System.currentTimeMillis() - tempoInicial) / 1000d+"s");
	}
   
}
