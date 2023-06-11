package org.mirrors;

public class ProgressBar {
    public static final int BAR_LENGTH = 30;

    public int totalProgress;
    public int currentProgress;

    private StringBuilder progressBar;
    public long startTime;

    public ProgressBar(int totalProgress) {
        this.totalProgress = totalProgress;
        this.currentProgress = 0;
        this.progressBar = new StringBuilder();
        this.startTime = System.currentTimeMillis();
    }

    public void updateProgress() {
        currentProgress++;
        float progress = (float) currentProgress / totalProgress;
        if (currentProgress % 2000 != 0) {
            return;
        }

        // Calcola il tempo trascorso
        long elapsedTime = System.currentTimeMillis() - startTime;

        // Calcola il tempo stimato rimanente
        long estimatedRemainingTime = (long) ((elapsedTime / progress) * (1 - progress));

        int completedChars = (int) (progress * BAR_LENGTH);
        int remainingChars = BAR_LENGTH - completedChars;

        progressBar.setLength(0);
        progressBar.append('\r');
        progressBar.append('[');
        for (int i = 0; i < completedChars; i++) {
            progressBar.append("■");
        }
        for (int i = 0; i < remainingChars; i++) {
            progressBar.append(" ");
        }
        progressBar.append(']');
        progressBar.append(String.format(" %.1f%%", progress * 100));

        // Aggiungi la stima del tempo residuo
        progressBar.append(" Remaining time: ");
        appendTime(progressBar, estimatedRemainingTime);

        if (currentProgress == totalProgress) {
            progressBar.append('\n');
        }

        System.out.print(progressBar);
        System.out.flush();
    }

    public void completeProgress() {
        currentProgress = totalProgress;
        float progress = (float) currentProgress / totalProgress;

        int completedChars = (int) (progress * BAR_LENGTH);
        int remainingChars = BAR_LENGTH - completedChars;

        progressBar.setLength(0);
        progressBar.append('\r');
        progressBar.append('[');
        for (int i = 0; i < completedChars; i++) {
            progressBar.append("■");
        }
        for (int i = 0; i < remainingChars; i++) {
            progressBar.append(" ");
        }
        progressBar.append(']');
        progressBar.append(String.format(" %.1f%%", progress * 100));

        if (currentProgress == totalProgress) {
            progressBar.append('\n');
        }

        System.out.print(progressBar);
        System.out.flush();
    }

    private void appendTime(StringBuilder sb, long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60)) % 24;

        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        if (seconds > 0) {
            sb.append(seconds).append("s");
        }
    }


}
