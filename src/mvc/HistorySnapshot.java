package mvc;

import java.io.PrintWriter;

public final class HistorySnapshot {
    private final String operation;
    private final String positionOfModifiedCounter;
    private final int valueOfLeftCounter;
    private final int valueOfRightCounter;

    public HistorySnapshot(String operation, String positionOfModifiedCounter, int valueOfLeftCounter, int valueOfRightCounter){
        this.operation = operation;
        this.positionOfModifiedCounter = positionOfModifiedCounter;
        this.valueOfLeftCounter = valueOfLeftCounter;
        this.valueOfRightCounter = valueOfRightCounter;
    }

    public void print(PrintWriter printStream) {
        printStream.println("----------");
        printStream.println(this.operation + " " + this.positionOfModifiedCounter);
        printStream.println("RESULTAT COMPTEUR GAUCHE " + this.valueOfLeftCounter);
        printStream.println("RESULTAT COMPTEUR DROIT " + this.valueOfRightCounter);
    }

    public String getPositionOfModifiedCounter() {
        return positionOfModifiedCounter;
    }

    public int getValueOfLeftCounter() {
        return valueOfLeftCounter;
    }

    public int getValueOfRightCounter() {
        return valueOfRightCounter;
    }
}
