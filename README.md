# FrontierNav Optimizer

A tool to find a near-optimal probe layout for FrontierNav in *Xenoblade Chronicles X* using a genetic algorithm.
It optimizes layouts to maximize metrics such as Miranium production and/or revenue.

## Usage

### Prerequisites

- **Java 25** or higher
- **Maven**

OR

- **Docker**

### Configuration

Program use the following tab-separated-values files in the `input/` directory:

| File                 | Description                                | TSV structure                                                                                                         |
|----------------------|--------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `sites.tsv`          | List of unlocked FN sites                  | `id` ; `miranium grade` ; `revenue grade` ; `unlocked sightseeing spots` (unexplored territories + scenic viewpoints) |
| `inventory.tsv`      | List of unlocked probes                    | `probe id` ; `quantity`                                                                                               |
| `network.tsv`        | Edges of the site network                  | pairs of `site id` connected in the graph                                                                             |
| `genetic.properties` | Tuning parameters of the genetic algorithm | pairs of `key`=`value`                                                                                                |

You can use the `.tsv` files from `input/default/` directory as a starting point, and then :

- modify the `sites.tsv` file to fit your progression on Mira (remove sites you have not already unlocked + change the number of unlocked sightseeing spots)
- modify the `inventory.tsv` file to match your probes inventory in game
- you can leave the `network.tsv` file as-is (the sites not in `sites.tsv` will be ignored)
- you can also modify the `genetic.properties` file to fine-tune the algorithm

### Running the Optimizer

Program will display the progression in the console.
At the end, the best found layout will be printed to the console.

#### Via Docker (recommended)

The easiest way to run the optimizer is using Docker Compose:

```
docker-compose up --build
```

#### Via Java/Maven

Build the project:

```
mvn clean package
```

Run the generated JAR:

```
java -jar target/frontiernav.jar
```