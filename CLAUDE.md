# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Test Commands

This is a Gradle multi-project build. All commands are run from the root directory.

| Command | Description |
|---------|-------------|
| `./gradlew build` | Build all projects |
| `./gradlew test` | Run tests for all projects |
| `./gradlew clean` | Clean build outputs |
| `./gradlew run` | Run the MoveLeftSimulation application |
| `./gradlew release` | Create a new release (requires main branch) |

## Project Structure

This repository contains multiple subprojects:

- **SharpeAi** (core library): Neural network implementation with mutation strategies
- **MoveLeftSimulation** (application): Main simulation demo using SharpeAi
- **Grid**: Board/coordinates utility library used by other projects
- **Chess**: Chess-specific code (depends on Grid)
- **GameOfLife**: Game of Life rule engine (depends on Grid)

## Core Architecture

### Neural Network

The neural network system is built around these key interfaces:

| Interface | Purpose |
|-----------|---------|
| `Node` | Basic neural network node with storage, capacity, and activation |
| `InputNode<I>` | Input node that accepts input of type I |
| `OutputNode<O>` | Output node that consumes output of type O |
| `Connection` | Weighted connection between nodes with ADD/SUBTRACT operations |

### Network Execution

1. `Network.increment()` activates all connections sequentially
2. Each connection moves stored values between source and destination nodes
3. `Node.depreciate()` reduces stored values after each tick
4. Nodes have an activation limit - once exceeded, they can activate

### Mutation Strategy

The system uses evolutionary algorithms with several mutation types defined in `MutationStrategy`:

- `WEIGHTS_ONLY`: Only modifies connection strengths (recommended for stability)
- `REMOVE_ONLY`: Removes nodes/connections
- `ADD_ONLY`: Adds new nodes/connections
- `MODIFY_WEIGHTS`: Randomizes weights and capacities
- `ALWAYS_ALLOW`: Allows all mutation types

The `RandomNetworkBuilder` applies mutations when copying networks between generations.

## Simulation Flow

1. Create `SimulationInput` with configuration (runTime, population size, mutation strategy)
2. Create `SimulationEnvironment` that manages elements and provides inputs/outputs
3. Run `Simulation.start()` which:
   - Creates initial random population
   - For each generation: runs simulation, applies survival criteria, mutates survivors
   - Each run: ticks through all elements, then replaces population with mutated offspring

## Key Patterns

- Jackson annotations enable JSON serialization of networks
- All components implement `Serializable` for persistence
- Node IDs determine activation order (lower IDs activate first)
- Thread-safety: Each network's tick is thread-safe if no input/output occurs during activation
