package org.mirrors;

class ProgressBar {
    private int total;
    private int progress;
    private int lastDisplayedProgress;

    public ProgressBar(int total) {
        this.total = total;
        this.progress = 0;
        this.lastDisplayedProgress = 0;
    }

    public synchronized void update(float progress) {
        this.progress = (int) (progress * total);
        displayProgress();
    }

    public synchronized void complete() {
        progress = total;
        displayProgress();
    }

    private void displayProgress() {
        int percent = (int) (progress * 100.0 / total);

        if (percent != lastDisplayedProgress) {
            System.out.print("\rProgress: " + percent + "%");
            lastDisplayedProgress = percent;
        }

        if (progress == total) {
            System.out.println();
        }
    }
}