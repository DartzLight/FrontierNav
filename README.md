# FrontierNav Optimizer

A tool to find a near-optimal probe layout for FrontierNav in *Xenoblade Chronicles X* using a genetic algorithm.
It optimizes layouts to maximize metrics such as Miranium production, revenue and/or precious resources.

## Usage

### Prerequisites

- **Java 25** or higher

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

If some `.tsv` files are not found in the `input/` directory, the program will use the default one in `input/default/`.  
In other words : the `input/*.tsv` files override the `input/default/*.tsv` files.

You can copy the `.tsv` files from `input/default/` to `input/` directory, and then :

- modify the `sites.tsv` file to fit your progression on Mira (remove sites you have not already unlocked + change the number of unlocked sightseeing spots)
- modify the `inventory.tsv` file to match your probes inventory in game
- you can leave the `network.tsv` file as-is (the sites not in `sites.tsv` will be ignored)
- you can also modify the `genetic.properties` file to fine-tune the algorithm
    - especially the `miraniumCoef` and `revenueCoef` parameters, representing the ratio of miranium/revenue you want to maximize
    - you can also configure precious resource targets (see [Precious resources](#precious-resources))

### Precious resources

In addition to Miranium and revenue, the algorithm can be tuned to favor layouts that produce enough of specific **precious resources**.

Two types of targets are supported and can be combined:

#### Absolute threshold

Sets a minimum amount of a precious resource that the layout should produce on average.

```properties 
threshold.BONJELIUM=1
``` 

A layout producing less than the threshold gets a proportional score penalty.

#### Ratio threshold

Sets a target as a *ratio* (between `0` and `1`) of the maximum possible production of that resource across all sites.

```properties
ratio.BONJELIUM=0.5
```

A value of 0.5 means the layout should reach at least 50% of the maximum achievable production for that resource.

_Both `threshold.*` and `ratio.*` entries can be defined for the same resource. In that case: both penalties will apply independently._

#### Available precious resources

The following precious resource names can be used as keys:

- `ARC_SAND_ORE`
- `AURORITE`
- `BOILED_EGG_ORE`
- `BONJELIUM`
- `CIMMERIAN_CINNABAR`
- `DAWNSTONE`
- `ENDURON_LEAD`
- `EVERFREEZE_ORE`
- `FOUCAULTIUM`
- `INFERNIUM`
- `LIONBONE_BORT`
- `MARINE_RUTILE`
- `OUROBOROS_CRYSTAL`
- `PARHELION_PLATINUM`
- `WHITE_COMETITE`

### Running the Optimizer

Program will display the progression in the console.

At the end, the best found layout will be printed to the console at the [frontiernav.net](https://frontiernav.net/wiki/xenoblade-chronicles-x/visualisations/maps/probe-guides) format (see [Probe codes](#probe-codes)).  
You can visualize it by copy/pasting the result at the end of this URL : https://frontiernav.net/wiki/xenoblade-chronicles-x/visualisations/maps/probe-guides/FN?map=

#### Via Docker (recommended)

The easiest way to run the optimizer is using Docker Compose:

```
docker-compose up --build
```

#### Via Java/Maven

Run the JAR from latest GitHub release:

```
java -jar frontiernav.jar
```

### Probe codes

List of probes and their respective codes in `inventory.tsv` and in frontiernav.net (console output):

| Probe                          | `inventory.tsv` code                             | frontiernav.net code |
|--------------------------------|--------------------------------------------------|----------------------|
| (site not unlocked / no probe) | (empty line with site **absent** in `sites.tsv`) | 0                    | 
| Basic                          | (empty line with site **present** in `site.tsv`) | 1                    |
| Mining G1                      | M1                                               | 2                    |
| Mining G2                      | M2                                               | 3                    |
| Mining G3                      | M3                                               | 4                    |
| Mining G4                      | M4                                               | 5                    |
| Mining G5                      | M5                                               | 6                    |
| Mining G6                      | M6                                               | 7                    |
| Mining G7                      | M7                                               | 8                    |
| Mining G8                      | M8                                               | 9                    |
| Mining G9                      | M9                                               | 10                   |
| Mining G10                     | M10                                              | 11                   |
| Research G1                    | R1                                               | 12                   |
| Research G2                    | R2                                               | 13                   |
| Research G3                    | R3                                               | 14                   |
| Research G4                    | R4                                               | 15                   |
| Research G5                    | R5                                               | 16                   |
| Research G6                    | R6                                               | 17                   |
| Booster G1                     | B1                                               | 18                   |
| Booster G2                     | B2                                               | 19                   |
| Duplicator                     | D                                                | 20                   |
| Storage                        | S                                                | 21                   |