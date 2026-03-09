package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.optimization.Fitness;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticSelection implements Selection {

	private final Random random;
	private final Fitness fitness;

	public GeneticSelection(Random random, Fitness fitness) {
		this.random = random;
		this.fitness = fitness;
	}

	@Override
	public List<FrontierNavResult> elitism(List<FrontierNavResult> population, int selectionByElitismCount) {
		return population.stream()
				.sorted(fitness.descendingComparator())
				.limit(selectionByElitismCount)
				.toList();
	}

	@Override
	public List<FrontierNavResult> tournament(List<FrontierNavResult> population, int selectionByTournamentCount, int tournamentSize) {
		List<FrontierNavResult> input = new ArrayList<>(population);
		List<FrontierNavResult> output = new ArrayList<>();
		for (int i = 0; i < selectionByTournamentCount; i++) {
			List<FrontierNavResult> tournament = random.ints(0, input.size())
					.distinct()
					.limit(tournamentSize)
					.mapToObj(input::get)
					.sorted(fitness.descendingComparator())
					.toList();
			FrontierNavResult selected = tournament.getFirst();
			input.remove(selected);
			output.add(selected);
		}
		return output;
	}

}
