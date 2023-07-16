package org.mirrors;

/**
 * Class representing a progress bar for tracking the progress of a task.
 */
public class ProgressBar {
    public static final int BAR_LENGTH = 30;

    public int totalProgress;
    public int currentProgress;

    private StringBuilder progressBar;
    public long startTime;
    public int flushFrequence;

    /**
     * Constructor for the ProgressBar class.
     *
     * @param totalProgress   the total progress value
     * @param flushFrequency  the frequency at which to update and flush the progress bar
     */
    public ProgressBar(int totalProgress, int flushFrequency) {
        this.totalProgress = totalProgress;
        this.currentProgress = 0;
        this.progressBar = new StringBuilder();
        this.startTime = System.currentTimeMillis();
        this.flushFrequence = flushFrequency;
    }

    /**
     * Updates the progress of the task.
     */
    public void updateProgress() {
        currentProgress++;
        float progress = (float) currentProgress / totalProgress;
        if (currentProgress % flushFrequence != 0) {
            return;
        }

        // Calcola il tempo trascorso
        long elapsedTime = System.currentTimeMillis() - startTime;

        // Calcola la velocità di avanzamento media
        float averageSpeed = (float) elapsedTime / currentProgress;

        // Calcola il tempo stimato rimanente
        long estimatedRemainingTime = (long) (averageSpeed * (totalProgress - currentProgress));


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

        System.out.print(progressBar);
        System.out.flush();
    }

    /**
     * Marks the progress as complete.
     */
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

    /**
     * Appends the time value to the string builder in the format "hh:mm:ss".
     *
     * @param sb     the string builder to append the time value to
     * @param millis the time value in milliseconds
     */
    private void appendTime(StringBuilder sb, long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60));

        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        if (seconds > 0) {
            sb.append(seconds).append("s    ");
        }
    }


}
