# My Personal Project

<h2>Project Proposal</h2>

**DESCRIPTION:** One of the most common issues a person has is not knowing what is inside their pantry. This
leads to unnecessary purchases of ingredients already in the household, or starting a culinary project only to discover
fundamental pieces were missing. Moreover, it is also quite easy to lose track of what is in your pantry/ fridge which
results miscellaneous pieces of fruit or leftovers from a month ago slowly rotting in the back of your fridge. This
application aims to solve all of these problems for people all over the world. Through a set of very simple inputs
after shopping for groceries, this program will be able to keep track of what you currently have in your pantry,
how much of it is currently left and its expiration date. Moreover, it will notify users when to throw out expired
food and when they are running low on certain items. The last function is that users are able to input ingredients
to a recipe they would like to make, and the program will return what they are missing/ what they need more of.

**USER STORIES**
- As a user, I would like to be able to update my pantry after each purchase
- As a user, I would like to categorize the food I am storing (Produce, Meat, Dairy etc.)
- As a user, I would like to be able to view what is currently in my pantry and their expiration dates
- As a user, I would like to be able to view how much of certain ingredients is currently in my pantry
- As a user, I would like to be notified in advance what food is expiring soon
- As a user, I would like to be notified when certain foods are running low
- As a user, I would like to be input the ingredients to a recipe, and be notified which foods I still need
- As a user, I would like to save recipes I use often to access later


**<h2>Phase 4: Task 2</h2>**

- Tue Apr 02 00:11:43 PDT 2024
- Added Steak to the pantry
- Tue Apr 02 00:11:43 PDT 2024
- Added 1 of Steak to the pantry.
- Tue Apr 02 00:12:07 PDT 2024
- Added Bread to the pantry
- Tue Apr 02 00:12:07 PDT 2024
- Added 12 of Bread to the pantry.
- Tue Apr 02 00:12:31 PDT 2024
- Added Cheetos to the pantry
- Tue Apr 02 00:12:31 PDT 2024
- Added 14 of Cheetos to the pantry.
- Tue Apr 02 00:12:38 PDT 2024
- Used 4 Cheetos
- Tue Apr 02 00:12:49 PDT 2024
- Used 1 Steak

**<h2>Phase 4: Task 3</h2>**

My UML diagram at its core is extremely simple. There was not a heavy use of abstract classes or anything of that
nature, it was just 3 class "hierarchy" where the top-level pantry had a list of ingredients which had a list of
purchases. There was definitely quite a bit of shared functionality when it came to adding and removing items, but
none were similar enough to extract one superclass out of everything. If I had more time, the most improvements I would
make is refactoring my PantryApp class. A lot of the functionality of the pantry was coded in an incredibly inefficient
way as it parses through the entire list of Ingredients and Purchases, before finding what it needs. Things I could do 
is change the ArrayList into perhaps a set, as I believe storing the exact location of each Purchase and Ingredient is 
not incredibly important if I am already parsing through each step one by one. I could also potentially factor out a
class for my Pantry, Ingredient and Purchase to deal with getting expired food, using food, etc. to ensure each class
only has one job as they currently all act as "managers". Apart from that, there isn't a lot of coupling in my design,
the main things to change are increasing efficiency and potentially separating more tasks to ensure everything only has
one job.