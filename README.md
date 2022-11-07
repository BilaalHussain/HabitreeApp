# Habitree
This project's repository is divided into 3 folders.


## ./android
The android app code
## ./functions
The firebase functions source code.

## ./tree_rendering
The tree rendering source code.

### App Functionality 
Sign in with email/password + Google OAuth
The app gives users two options for signing in/creating their account. They could enter an email and a password into the input fields on the login screen, then tap the “sign in” button to log in. They could also simply tap the “sign in with google” button, which will prompt them to choose an existing google account on the device. Once they have chosen an account, the app will log them in with that account, and if it’s the user’s first time signing in, their account will also be registered.
### Habit CRUD
The app allows a user to create, update and delete a habit. They can customise the name, category (academic, fitness, self-help, creative or ) and the frequency they want to perform the habit (daily or a custom number of times a week). They can also add and delete a geofence requirement or tags.
### Tags 
A user can add custom tags to a habit that will display underneath a habit. They can give this tag a custom colour and custom name to visually group like habits together. These tags can later be updated or deleted and new tags can always be added.
### Notification
At the end of the day, the app will check to see if there are incomplete daily habits. If there are incomplete habits, it will send a push notification reminding them to do the specific habit. If all habits are complete, it will instead send a silent notification congratulating the user on completing their habits for the day. 
### Geofence completion
The user has the option to mark an area on a map where they want that particular habit to be completed. This registers a geofence with the android background location access API, and creates a listener to listen for event broadcasts that indicate that the phone has entered that location. It automatically marks the habit as completed for the day, and also sends a notification. Each geofence is uniquely mapped to an existing habit, and can be edited or deleted.
### Tree and Pie-chart display
Using the user’s habit completion data, the app generates a set of scores across several categories. These scores are used as input to render a procedurally generated tree (seeding by the user’s ID) that grows larger as the user’s scores increase. The colours of the tree’s leaves are determined by their respective scores in each category. A pie chart also displays the breakdown of their progress across the different categories.
### Historical trees
Each week, a user’s tree is reset and they start anew. Previous week’s trees are still viewable on the User page inside the app, allowing the user to see and compare their progress across different weeks.
Share tree feature
When viewing their tree, a user is able to press the share button to start the native android share screen and send an image of their tree to friends. 
### Follow users
The app allows the users to stay up-to-date with each other’s progress with the “following” feature. Each user of the app has a unique ID that can be shared with others by simply hitting the “share ID” button on the leaderboard page. Once you have your friend’s ID, simply hit the “follow user” button, paste in their ID, and tap follow. Your friend will now be added to your leaderboard.
### Leaderboard
The ‘Leaderboard’ page shows the name and score of those the user follows sorted by their score. Tapping on their cards on this page will open up their profile page and display their tree and pie chart.
Widget
A user is able to add a widget to their home screen that displays their current week’s tree. As the user completes their habits, this tree will grow and be updated on their home screen.
