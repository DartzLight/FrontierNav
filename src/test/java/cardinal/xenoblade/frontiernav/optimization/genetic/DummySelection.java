package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.FrontierNavResult;

import java.util.List;

public class DummySelection implements Selection {

	@Override
	public List<FrontierNavResult> elitism(List<FrontierNavResult> population, int selectionByElitismCount) {
		return List.of(population.getFirst());
	}

	@Override
	public List<FrontierNavResult> tournament(List<FrontierNavResult> population, int selectionByTournamentCount, int tournamentSize) {
		return List.of(population.getLast());
	}

}
