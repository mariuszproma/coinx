package pl.forexat.nbpscraper.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class Coin {
	private static final String URL_DIR="C:\\nbpproject\\url.txt";
	private static final Logger log = Logger.getLogger(Coin.class);
	private String coinPage;
	public static final int SHOP_LIMITS=2;

	public String getCoinPage() {
		log.info("Using coin page: " + this.coinPage);
		return this.coinPage;
	}
	
	public int getCoinLimits() {
		return SHOP_LIMITS;
	}

	public void setCoinPage() {
		while(StringUtils.isBlank(this.coinPage)){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readCoinPage();
		}
	}
	
	private void readCoinPage(){
		File file = new File(URL_DIR);
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			//BufferedReader br = new BufferedReader(new FileReader(file));
			this.coinPage=br.readLine();
			br.close();
			log.info("Set coin page to: "+coinPage);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
	}

	public boolean isCoinPageSet() {
		return StringUtils.isNotBlank(this.coinPage);
	}
}
