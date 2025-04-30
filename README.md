# Predictive Classification of Aircraft Approach Behaviors

Written for USC's CSCE 581 - Trusted Artificial Intelligence with Dr. Biplav Srivastava

c. Cam Osterholt ('25)

Please feel free to reach out to me via [LinkedIn](https://www.linkedin.com/in/camosterholt/) or [email](mailto:usc@osterholt.us).

## About

This project tracks the location of a variety of aircrafts among two airports. The locations are provided by the FAA using their SWIM Terminal Data Distribution System (STDDS) in the Swift Portal. To get more data at different airports please apply at their [site](https://portal.swim.faa.gov/). The environment variables provided can be inputted at [reference.conf](https://github.com/osterholt/PCAAB/blob/main/jumpstart-latest/src/main/resources/reference.conf)

More information can be found in my [summary report](https://github.com/osterholt/PCAAB/blob/main/Final%20Report%20-%20Predictive%20Classification%20of%20Aircraft%20Approach%20Behaviors.pdf) and [final presentation](https://github.com/osterholt/PCAAB/blob/main/Final%20Presentation.pptx).

Possible Behaviors:

1. no behavior (standing still)
2. standard landing
3. going around
4. switching sides
5. flyby
6. Unknown

## Demos

I have created two demos to showcase the effective classification. They both showcase the same flight into Atlanta with an approach that switches sides. The [first](https://youtu.be/4EWAxd-LDcs) showcases the simple Random Forest model and [second](https://youtu.be/hoQqRULs9rQ) is a Multilayer Perceptron Classifier Neural Network.

## Running Training Scripts

Make sure you have conda installed. I reccomend [miniconda](https://www.anaconda.com/docs/getting-started/miniconda/install). Once installed, run:

`conda env create -f environment.yml`

Once created, activate the environment with

`conda activate pcaab_env`
