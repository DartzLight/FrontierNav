package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.FrontierNavResult;

import java.util.List;

public interface Selection {

	List<FrontierNavResult> elitism(List<FrontierNavResult> population, int selectionByElitismCount);

	List<FrontierNavResult> tournament(List<FrontierNavResult> population, int selectionByTournamentCount, int tournamentSize);
	
}
