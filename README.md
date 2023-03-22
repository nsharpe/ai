#SharpeAI
By Neil Sharpe
___

* [Introduction](#Introduction)
* [Assumptions](#Assumptions)
* [Requirements](#Requirements)

## Introduction
This application is intended to be a personal learning tool in how a Neural Network (and evolution in general) works.   You man not copy, distribute, or modify the code shared in this repo.

Currently, the application tests how a neural network would move an object from left to right in a competative environment where individual Neural Networks try to acheive the same goal.

## Assumptions
These assumptions state intended interactions for this code base but may not reflect the reality.  The reason for this is some sections were written before this project was taken more seriously.  Any piece of code that does not follow these assumptions should be considered tech debt to be resolved.
* All Nodes activate one at a time in a consistent order.  For example a node with id 3 can activate with input from node 1 and 2 and can be used as input by any node **including itself** with an id of 3 or higher
* All code commits should come with the goal of generalizing input and output
* The Neural Networks will be generated via a [Evolutionary Algorithm](https://en.wikipedia.org/wiki/Evolutionary_algorithm).  As such the networks generated from this project will work best with short quick actions.
* There will be no jars required to run this application

## Requirements
* Java 16