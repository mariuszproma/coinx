package pl.forexat.nbpscraper;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import pl.forexat.nbpscraper.driver.DriverFactory;
import pl.forexat.nbpscraper.model.Coin;
import pl.forexat.nbpscraper.operations.Purchase;

public class MainFlow {
	private static final Logger log = Logger.getLogger(MainFlow.class);
	public static void main(String[] args) throws MalformedURLException{
		AccountConfiguration accountConfiguration = new AccountConfiguration();
		if(args.length>0)
		accountConfiguration.setUsername(args[0]);
		if(args.length>1)
		accountConfiguration.setPassword(args[1]);
		log.info("Credentials: "+accountConfiguration);
		log.info("Setting coin page");
		Coin coin = new Coin();
		coin.setCoinPage();
		
		Purchase purchase = new Purchase();
		purchase.setCoin(coin);
		purchase.setConfig(accountConfiguration);
		purchase.setDriver(DriverFactory.getGridDriver("chrome"));
		
		//while(true){
			try {
				purchase.buyCoin();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
}
