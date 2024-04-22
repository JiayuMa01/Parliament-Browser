package org.ppr.abschlussprojekt.helper;

import java.text.DecimalFormat;


/**
 * Show the progress bar
 * @author Jiayu Ma
 */
public class ProgressBar {

    private String progressName;
    private int totalLength;
	private DecimalFormat formater = new DecimalFormat("#.##%");

	public ProgressBar(String progressName, int totalLength) {
        this.progressName = progressName;
        this.totalLength = totalLength;
	}

    /**
     * Show the actual progress
     * @param value The actual progress
     * @author Jiayu Ma
     */
	public void show(int value) {
		if (value < 0 || value > totalLength) { return; }
        System.out.print('\r');
		float rate = (float)(value*1.0 / totalLength);
		draw(rate, value);
		if (value==totalLength) { System.out.print('\n'); }
	}

    /**
     * Draw the actual progress, when new progress been made.
     * @param rate The rate of actual progress
     * @param value The actual progress
     * @author Jiayu Ma
     */
	private void draw(float rate, int value) {
		int len = (int) (rate * 50);
		System.out.print(this.progressName + ": ");
		for (int i = 0; i < len; i++) { System.out.print("#"); }
		for (int i = 0; i < 50-len; i++) { System.out.print(" "); }
		System.out.print(" |" + formater.format(rate));
        System.out.print("    ");
        System.out.print(value + "/" + totalLength);
	}


    public static void main(String[] args) throws InterruptedException {
        int total = 50000;
		ProgressBar cpb = new ProgressBar("Updating all Abgeordnete", total);
        for (int i = 1; i <= total; i++) {
            cpb.show(i);
            Thread.sleep(100);
        }
    }
}