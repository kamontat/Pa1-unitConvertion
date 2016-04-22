package Code;

/**
 * Created by kamontat on 21/4/59.
 */
public class UnitConverter {

	public UnitConverter() {
	}

	public double convert(double amount, LengthUnit from, LengthUnit to) {
		return (amount / to.getValue()) * from.getValue();
	}

	public void getUnit() {
		for (int i = 0; i < LengthUnit.values().length; i++) {
			System.out.print(LengthUnit.values()[i].getName() + " ");
		}
		System.out.println();
	}
}
