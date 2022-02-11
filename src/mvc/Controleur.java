package mvc;

import javafx.scene.text.Text;

public class Controleur {

	private final Counter counter;
	private final Text vue;
	private final String position;

	public Controleur(Counter counter, Text vue, String position) {
		this.counter = counter;
		this.vue = vue;
		this.position = position;
	}

	public void inc() {
		this.counter.ajouter(1);
		this.updateText();
	}

	public void dec() {
		this.counter.supprimer(1);
		this.updateText();
	}

	public void dub() {
		this.counter.multiplier(2);
		this.updateText();
	}

	public void div() {
		this.counter.diviser(2);
		this.updateText();
	}

	private void updateText() {
		this.vue.setText(String.valueOf(this.counter.getValue()));
	}

	public void updateCompteur(int v) {
		this.counter.setValue(v);
		updateText();
	}

	public int getValeur() {
		return this.counter.getValue();
	}

	public String getPosition() {
		return position;
	}
}