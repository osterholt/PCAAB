# Predictive Classification of Aircraft Approach Behaviors

Written for USC's CSCE 581 - Trusted Artificial Intelligence with Dr. Biplav Srivastava

Please feel free to reach out to me via [LinkedIn](https://www.linkedin.com/in/camosterholt/) or [email](mailto:usc@osterholt.us).

## About

This project tracks the location of a variety of aircrafts among two airports. The locations are provided by the FAA using their SWIM Terminal Data Distribution System (STDDS) in the Swift Portal. To get more data at different airports please apply at their [site](https://portal.swim.faa.gov/). The environment variables provided can be inputted at [reference.conf](jumpstart-latest/src/main/resources/reference.conf.)

More information can be found in my summary report and final presentation.

Possible Behaviors:

1. no behavior (standing still)
2. standard landing
3. going around
4. switching sides
5. flyby
6. Unknown

## Running Training Scripts

Make sure you have conda installed. I reccomend [miniconda](https://www.anaconda.com/docs/getting-started/miniconda/install). Once installed, run:

`conda env create -f environment.yml`

Once created, activate the environment with

`conda activate pcaab_env`
