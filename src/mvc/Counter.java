package mvc;

/*
 *	Notez que cette classe est completement indépendante de la representation.
 *	On pourraît facilement definir un interface complètement different.
 */
public class Counter {

	private int value;

	public Counter() {
		this.value = 0;
	}

	public Counter(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void ajouter(int montant) {
		this.value = this.value + montant;
	}

	public void supprimer(int montant) {
		this.value = this.value - montant;
	}

	public void multiplier(int fois) {
		this.value = this.value * fois;
	}

	public void diviser(int fois) {
		this.value = this.value / fois;
	}

}
