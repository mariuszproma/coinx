package pl.forexat.nbpscraper.utils;

public class SlowDown {
	private long slowTime;

	public void setSlowTime(long slowTime) {
		this.slowTime = slowTime;
	}

	public Object slow() throws Exception {
		Thread.sleep(this.slowTime);
		return null;
	}
}
