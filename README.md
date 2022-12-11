# Insight: the AI visualizer

## Screenshots
![screenshot](https://github.com/Anirudh1331/Amazon-Hackathon/blob/main/my_image.jpeg?raw=true)

## How to Run?
Step-1: Download the APK for sample use.

Step-2: To run the app, go with the "skip login button" to enter the home page.

Step-3: Click on the "Upload Image" button, and a dialog box will appear stating to choose one of the three options i.e., "Choose from Gallery", "Take Picture", and "Cancel". 

Step-4: After uploading, search for the object you want to buy, and the recommendation model will provide you with various options suitable for that place.

Step-5: Add the products to your card and click on the eye icon in the cart against each product to visualize it.

Step-6: Click on the "Visualize" button to start visualizing the objects.


## Problem Statement
* _Increase in the number of returns_

  Amazon faces a whopping $50 billion (approx.) returns each financial year, 25%-30% of total e-commerce sales in 2020. The top categories for returns were apparel,     home improvement, and housewares. On average, we found that the World Average Cart Abandonment Rate of Amazon is 77.13%. Hassle-free returns have enabled customers     to return the product quickly, which has become problematic for the e-commerce industry. Our team is to the rescue and we have developed a product that can help       Amazon customers to visualize their favorite products with the help of Augmented reality/ Virtual reality in their home interior space with the exact product           dimensions, providing virtual representation, analyzing fitment, color themes sync, so that customer could be more sure about the looks of the chosen product in       their space.

* _Lack of suitable recommendations_
  
   In today’s world, most people consult home decorators for the right products that best suit their decor, but these consultations are quite expensive. Therefore, we    need an AI-based friend that could provide the most accurate choices taking into account the ambience of the user’s room. We have integrated a recommendation system    based on neural networks to provide the users with a more customized shopping experience.

## Our Solution 
Majorly our entire solution is divided into 2 components:

1. _AI Recommendation System  :_  
   When the user captures the image of his room and enters the product needed, Image segmentation is carried out using the Detectron2. Then the application displays      all the recommended products which are stored in the Amazon database by extracting feature vectors and calculating their cosine similarity to match the requirements    as well the ambience of the user’s room. 

2. _AR-VR Model  :_  
    Built using Google AR-VR core, it enables the user to visualize multiple products present in the cart all at once. It is entirely integrated into the Amazon mobile     application using Java and Android  and can be enabled just with the click of a button.

## Tech Stack 
* Java and Android for Application development
* Google AR Core for the Augmented Reality Experience
* Detectron2 for the image cropping and segmentation
* Object feature extraction and similarity measurement using ResNet trained with ImageNet in Tensorflow Hub

### Contributors
* Nakshatra
* Devansh Gupta
* Ankita Puri
