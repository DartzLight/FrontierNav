package cardinal.xenoblade.frontiernav.site;

import java.util.Arrays;
import java.util.Optional;

public enum PreciousResource {
	ARC_SAND_ORE,
	AURORITE,
	BOILED_EGG_ORE,
	BONJELIUM,
	CIMMERIAN_CINNABAR,
	DAWNSTONE,
	ENDURON_LEAD,
	EVERFREEZE_ORE,
	FOUCAULTIUM,
	INFERNIUM,
	LIONBONE_BORT,
	MARINE_RUTILE,
	OUROBOROS_CRYSTAL,
	PARHELION_PLATINUM,
	WHITE_COMETITE;

	public static Optional<PreciousResource> of(String name) {
		return Arrays.stream(values())
				.filter(resource -> resource.name().equalsIgnoreCase(name))
				.findFirst();
	}
}
