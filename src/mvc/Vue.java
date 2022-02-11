package mvc;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.EmptyStackException;

/*
 * Auteurs: Mathieu Morin et Adrian Sanchez Roy
 * Devoir 4 IFT-1025, Avril 2021
 */

public class Vue extends Application {

	private Controleur controleurGauche;
	private Controleur controleurDroit;
	private final History historique = new History();

	@Override
	public void start(Stage primaryStage) {

		try {
			BorderPane borderPane = new BorderPane();
			Scene scene = new Scene(borderPane, 600, 300);

			// Center GridPane
			GridPane grid = new GridPane();
			grid.setPadding(new Insets(30, 30, 30, 30));
			grid.setVgap(10);
			grid.setHgap(30);

			// Radio buttons
			RadioButton selecteurCompteurGauche = new RadioButton("Counter 1");
			selecteurCompteurGauche.setSelected(true);
			RadioButton selecteurCompteurDroit = new RadioButton("Counter 2");

			ToggleGroup selecteursToggleGroup = new ToggleGroup();
			selecteurCompteurGauche.setToggleGroup(selecteursToggleGroup);
			selecteurCompteurDroit.setToggleGroup(selecteursToggleGroup);

			// Textes compteurs
			Text textValeurCompteur1 = new Text("Appuyer sur un bouton");
			Text textValeurCompteur2 = new Text("Appuyer sur un bouton");

			// Undo/Redo buttons
			Button undoButton = new Button("Undo");
			Button redoButton = new Button("Redo");

			// Format & Populate Center GridPane
			GridPane.setConstraints(selecteurCompteurGauche, 0, 0);
			GridPane.setConstraints(textValeurCompteur1, 0, 1);
			GridPane.setHalignment(textValeurCompteur1, HPos.CENTER);

			GridPane.setConstraints(selecteurCompteurDroit, 1, 0);
			GridPane.setConstraints(textValeurCompteur2, 1, 1);
			GridPane.setHalignment(textValeurCompteur2, HPos.CENTER);

			GridPane.setConstraints(undoButton, 0, 2);
			GridPane.setHalignment(undoButton, HPos.CENTER);
			GridPane.setConstraints(redoButton, 1, 2);
			GridPane.setHalignment(redoButton, HPos.CENTER);

			grid.setAlignment(Pos.CENTER);
			grid.getChildren().addAll(
					selecteurCompteurGauche,
					selecteurCompteurDroit,
					textValeurCompteur1,
					textValeurCompteur2,
					undoButton,
					redoButton
			);

			// Menu bar
			Menu historiqueMenu = new Menu("Historique");
			MenuItem imprimerHistoriqueMenu = new MenuItem("Imprimer l'historique");
			final String historiqueFilePath = "./historique.txt";
			imprimerHistoriqueMenu.setOnAction(actionEvent -> historique.imprimerHistorique(historiqueFilePath));
			historiqueMenu.getItems().add(imprimerHistoriqueMenu);
			MenuBar menuBar = new MenuBar();
			menuBar.getMenus().add(historiqueMenu);

			// Buttons
			Button inc = new Button("+1");
			Button dub = new Button("*2");
			Button div = new Button("/2");
			Button dec = new Button("-1");

			// VBox for Top
			VBox topVBox = new VBox();
			topVBox.getChildren().addAll(menuBar, inc);

			// Format & populate BorderPane
			topVBox.setAlignment(Pos.CENTER);
			BorderPane.setAlignment(dub, Pos.CENTER);
			BorderPane.setAlignment(div, Pos.CENTER);
			BorderPane.setAlignment(dec, Pos.CENTER);

			borderPane.setTop(topVBox);
			borderPane.setBottom(dec);
			borderPane.setLeft(dub);
			borderPane.setRight(div);
			borderPane.setCenter(grid);

			// Controllers
			controleurGauche = new Controleur(new Counter(), textValeurCompteur1, "COMPTEUR GAUCHE");
			selecteurCompteurGauche.setUserData(controleurGauche);
			controleurDroit = new Controleur(new Counter(), textValeurCompteur2, "COMPTEUR DROIT");
			selecteurCompteurDroit.setUserData(controleurDroit);

			inc.setOnAction((action) -> {
				Controleur selectedControleur = (Controleur) selecteursToggleGroup.getSelectedToggle().getUserData();
				selectedControleur.inc();
				historique.updateHistoryStacks(
						"ADDITION",
						selectedControleur.getPosition(),
						controleurGauche.getValeur(),
						controleurDroit.getValeur());
			});

			dec.setOnAction(actionEvent -> {
				Controleur selectedControleur = (Controleur) selecteursToggleGroup.getSelectedToggle().getUserData();
				selectedControleur.dec();
				historique.updateHistoryStacks(
						"SOUSTRACTION",
						selectedControleur.getPosition(),
						controleurGauche.getValeur(),
						controleurDroit.getValeur()
				);
			});

			dub.setOnAction(actionEvent -> {
				Controleur selectedControleur = (Controleur) selecteursToggleGroup.getSelectedToggle().getUserData();
				selectedControleur.dub();
				historique.updateHistoryStacks(
						"MULTIPLICATION",
						selectedControleur.getPosition(),
						controleurGauche.getValeur(),
						controleurDroit.getValeur());
			});

			div.setOnAction(actionEvent -> {
				Controleur selectedControleur = (Controleur) selecteursToggleGroup.getSelectedToggle().getUserData();
				selectedControleur.div();
				historique.updateHistoryStacks(
						"DIVISION",
						selectedControleur.getPosition(),
						controleurGauche.getValeur(),
						controleurDroit.getValeur());
			});

			undoButton.setOnAction(actionEvent -> {
				try {
					var displayedSnapshot = historique.getDisplayedSnapshot();
					String counterToUpdate = displayedSnapshot.getPositionOfModifiedCounter();

					historique.undo();  // throws exception when no action to undo.

					updateDisplay(historique.getDisplayedSnapshot(), counterToUpdate);
				} catch (EmptyStackException e) {
					System.err.println("undoStack is empty");
				}
			});

			redoButton.setOnAction(actionEvent -> {
				try {
					historique.redo();  // throws exception when no action to redo.

					var snapshotToDisplay = historique.getDisplayedSnapshot();
					String compteurToUpdate = snapshotToDisplay.getPositionOfModifiedCounter();
					updateDisplay(snapshotToDisplay, compteurToUpdate);
				} catch (EmptyStackException e) {
					System.err.println("redoStack is empty");
				}
			});

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateDisplay(HistorySnapshot snapshotToDisplay, String positionCompteurToUpdate) {
		if (positionCompteurToUpdate.equals(controleurGauche.getPosition()))
			controleurGauche.updateCompteur(snapshotToDisplay.getValueOfLeftCounter());
		else if (positionCompteurToUpdate.equals(controleurDroit.getPosition()))
			controleurDroit.updateCompteur(snapshotToDisplay.getValueOfRightCounter());
	}

	public static void main(String[] args) {
		launch(args);
	}
}
