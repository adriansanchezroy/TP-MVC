package mvc;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public final class History {
    private HistorySnapshot snapshotDisplayed = new HistorySnapshot("DEBUT", "", 0, 0);
    private final Stack<HistorySnapshot> undoStack = new Stack<>();
    private final Stack<HistorySnapshot> redoStack = new Stack<>();

    public void updateHistoryStacks(String operation, String positionControleur, int valueOfLeftCounter, int valueOfRightCounter) {
        redoStack.clear();
        undoStack.push(snapshotDisplayed);
        snapshotDisplayed = new HistorySnapshot(operation, positionControleur, valueOfLeftCounter, valueOfRightCounter);
    }

    public void imprimerHistorique(String FilePath) {
        try (var outputFile = new FileWriter(FilePath)) {
            var printWriter = new PrintWriter(outputFile);
            var historique = unstackHistory();

            printWriter.println("----------");

            for (var snapshot : historique)
                snapshot.print(printWriter);

            printWriter.println("----------");
            printWriter.println("FIN");
            printWriter.println("RESULTAT COMPTEUR GAUCHE " + historique.get(historique.size()-1).getValueOfLeftCounter());
            printWriter.println("RESULTAT COMPTEUR DROIT " + historique.get(historique.size()-1).getValueOfRightCounter());
            printWriter.println("----------");
            printWriter.println("----------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<HistorySnapshot> unstackHistory() {
        var history = new ArrayList<>(undoStack);
        history.add(snapshotDisplayed);

        return history;
    }

    public void undo() throws EmptyStackException {
        HistorySnapshot lastAction = undoStack.pop();
        redoStack.push(snapshotDisplayed);
        snapshotDisplayed = lastAction;
    }

    public void redo() throws EmptyStackException {
        HistorySnapshot nextAction = redoStack.pop();
        undoStack.push(snapshotDisplayed);
        snapshotDisplayed = nextAction;
    }

    public HistorySnapshot getDisplayedSnapshot() {
        return snapshotDisplayed;
    }
}
