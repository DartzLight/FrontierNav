package cardinal.xenoblade.frontiernav.site;

import java.util.Arrays;

public enum MiraniumRank {
	A(500),
	B(350),
	C(250);

	private final int baseMiranium;

	MiraniumRank(int baseMiranium) {
		this.baseMiranium = baseMiranium;
	}

	public static MiraniumRank of(String string) {
		return Arrays.stream(values())
				.filter(value -> value.name().equals(string))
				.findFirst()
				.orElseThrow();
	}

	public int getBaseMiranium() {
		return baseMiranium;
	}
}
