#SharpeAI
By Neil Sharpe
___

* [Introduction](#Introduction)
* [Assumptions](#Assumptions)
* [Definitions](#Definitions)
* [Requirements](#Requirements)
* [Running The Application](#Running-The-Application)

## Introduction
This application is intended to be a personal learning tool in how a Neural Network (and evolution in general) works.   You may not copy, distribute, use, scan, or modify the code shared in this repo.

Currently, the application tests how a neural network would move an object from left to right in a competative environment where individual Neural Networks try to acheive the same goal.

## Assumptions
These assumptions state intended interactions for this code base but may not reflect the reality.  The reason for this is some sections were written before this project was taken more seriously.  Any piece of code that does not follow these assumptions should be considered tech debt to be resolved.
* All Nodes activate one at a time in a consistent order.  For example a node with id 3 can activate with input from node 1 and 2 and can be used as input by any node **including itself** with an id of 3 or higher
* The Neural Networks will be generated via a [Evolutionary Algorithm](https://en.wikipedia.org/wiki/Evolutionary_algorithm).  As such the networks generated from this project will work best with short quick actions.
* There will be no jars, except jars for serialization, required to run this application
* Number of inputNodes and output are consistent for all Neural Networks running in a given simulation.  Specifically if there is an input with id 1,2,3 at the begining of the simulation, 1 2 and 3 should never be removed or altered, and 4th input should not be introduced.  This is to ensure that the neural network can mutate properly.
* A tick for one neural network is threadsafe as long as not input or output occurs while the tick is occuring

## Definitions

#### Tick
All nodes of a given neural network being activated.  There is probably a better word.  To be reviewed.
#### Step
All neural networks in a given simultation activating a tick along with any pre or post step actions. For example, inserting an input and then obtaining an output
#### Generation
A neural network that has had x number of parents would be the x+1 generation

#### Simulation
A series of generations.  The initial generation may be random or predetermined.  Subsequent generations are populated by neural networks that satisfy some condition.  When moving from one generation to another any neural networks offspring have a chance to be slightly and randomly modified.  


## Requirements
* Java 17

## Running The Application
From the root directory you can run

`./gradlew clean build run`

## Creating a release
run

`./gradlew release`