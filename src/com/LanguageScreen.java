package com;

final class LanguageScreen extends Thread {

	private final SplashScreen s;

	LanguageScreen(SplashScreen s) {
		this.s = s;
	}

	public final void run() {
		try {
			Thread.sleep(3000L);
			SplashScreen.langSelection(this.s, 2);
		} catch(Exception var2) {
			var2.printStackTrace();
		}
	}
}
