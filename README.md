# Original App Design Project - Omar Guajardo
Android application that will generate and outfit for you based on your current clothing items
===

# Fit Generator

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
 Allows users to input their closet into three categories: Tops,Bottoms and Shoes. The app will generate and outfit either when the user wants to generate an outfit or when it's schedule to. The app will use a color matching alogirthm and take into account the weather,season and occation to make an outfit for the user.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Lifestyle
- **Mobile:** Mobile is essential for the efficency, when a user sets a scheduled Outfit, for exmaple in the morning before work or school, the app will send a push notification that the user can check right away after waking up.
- **Story:** Fit Generator will become an essential part in the life of a busy user who allows the app to take over their morning decision of what to wear, a task that takes way too much time for little reward.
- **Market:** Any person from any kind of proffession can use this app, from lawyers to college students the Fit Generator will remove the hastle of having to choose an outfit for the day
- **Habit:** Everytime the users wants to generate a new non-scheduled outfit the user has to go into the app to generate the new outfit.
- **Scope:** V1 will simply allow users to input their closet and click a button and generate a random outfit. V2 will introduce the feature of push notifications with schedule outfits and a color matching algorithm. V3 will be full re design of the UI. V4 will introduce the ability to keep track of what outfits have been wore and what items of clothing are currenlty dirty.


## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Log in and Register Screen
* Logout
* App must be able to input an item of clothing with respective criterias (fit,color,style,picture of item,name identifier,etc.)
* Every item of clothing has to be categorized as Top,Bottom or Shoe in a *Closet*
* App must be able to delete an item of clothing
* App must be able to edit an item of clothing
* App generates *endless* outfits
* App must keep track of items that have been worn


**Optional Nice-to-have Stories**

* Algorithm that generates outfits based on color/occassion/season
* Use of push notifications
* Ability to set Scheduled Outfits, if you the user is a student and wants an outfit generated every morning they should be able to set when they want the outfit generated and app will send a push notification
* Introduce a new item of *accessories*
* Some feature that lets the user know when they should do laundry
* Machine learning algorithm that takes into account the rejected outfites versus the accepted ones


### 2. Screen Archetypes

* Login - User signs up or logs into their account
    *  Log in and Register Screen
* Register - User signs up or logs into their account
    * Log in and Register Screen
* Stream - User can scroll through important resources in a list
    * Every item of clothing has to be categorized as Top,Bottom or Shoe in a *Closet*
    * In the stream there will be an *endless* amounts of possible fit options
* Detail - User can view the specifics of a particular resource
    * App must be able to delete an item of clothing
    * App must be able to edit an item of clothing
    * App must keep track of items that have been worn
* Creation - User can create a new resource
    * App must be able to input an item of clothing with respective criterias (fit,color,style,picture of item,name identifier,etc.)
    

* Settings - User can configure app options
    * Logout

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Stream (Closet)
* Stream (Generated Outfits)
* Stream (Dirty Clothes)

**Flow Navigation** (Screen to Screen)

* Login
    * Stream(Closet)
* Register
    * Stream (Closet)
* Stream(Closet)
    * Creation
    * Detail (Item Clothing)
* Stream(Generated Outfits)
   * Detail (Outfit)
* Stream (Dirty Clothes)
    * Detail (Item Clothing)


### Digital Wireframes & Mockups
I made a Wireframe of the application using Figma, click [here](https://www.figma.com/file/k7flYFR0zYg0Eca1OBwvSB/Fit-Generator?node-id=0%3A1) to view.
### Interactive Prototype
Figma based [prototype](https://www.figma.com/proto/k7flYFR0zYg0Eca1OBwvSB/Fit-Generator?node-id=1%3A2&scaling=scale-down)
## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

---

# Week by Week


## Week 4 Project Week 1

### Build Skeleton of Application
- Meaning that all the layout files should be completed wiht minimal styling and the buttons simply navigate the user to various screens
    - [ ] Import all necessary libraries
        - [ ] Material Design
        - [ ] Parse
        - [ ] View Binding
        - [ ] Parcel
    - [ ] Choose basic color scheme
    - [ ] Build all of the activities 
        - [ ] Welcome Activity
        - [ ] LogIn Activity
        - [ ] Register Activity
        - [ ] Main Activity
        - [ ] Item Details Activity
        - [ ] Add New Item Activity
    - [ ] Build all of the fragments
        - [ ] Closet Fragment
        - [ ] Generator Fragment
        - [ ] Laundry Fragment
    - [ ] Build all of the items for the Recycler Views
        - [ ] Clothing Item
     - [ ] User Registration and Login
        - [ ] Ability to Register
        - [ ] Ability to Login
        - [ ] Ability to LogOut

## Week 4 Project Week 2 (Hardest Week)

### Integrate Backend
- Connect the application to the Parse server and be able to read and write. An Object model for the Closet and Items of clothing should be defined.
   
    - [ ] Database and Object Models for Closet and Items
        - [ ] Create Parse Backend
        - [ ] Ability to create User
        - [ ] Define how data will be stored based on closet and user
        - [ ] Define how data will be retrieved based on Java objects
        - [ ] Ability to GET items from DB
            - [ ] Should be displayed accordingly in the Recycler Views
        - [ ] Ability to PUT new items 
        - [ ] Ability to edit items and be reflected onto DB
        - [ ] Ability to DELETE items
        - [ ] Ability to take a Picture
    - [ ] Create a crude algorithm to generate outfits
        - [ ] Randomizer, if time allows next week the alogirthm should be improved

## Week 4 Project Week 3 

### Material Design Implementation

- Finish anything from last week that didn't get done
- Implement Material Design Library 
    - [ ] Incorporating animations 
    - [ ] Make a loading page
    - [ ] Create a logo
    - [ ] Implement colo scheme
    - [ ] Bring in Icons
    - [ ] Use different fonts
    - [ ] Include background images for landing page/top bar

## Week 4 Project Week 4

### Code Clean Up, Testing and Additional Features

- Finish anything from last week that didn't get done
- Testing
    - [ ] Is the app able to handle the following:
        - [ ] Delete an item of clothing
        - [ ] Edit an item of clothing
        - [ ] "Wash" an item of clothing
        - [ ] Have unique closets per user
        - [ ] Every outfit must be unique every generation

