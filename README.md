Language : Kotlin
Architecture: MVVM
DI : Hilt Dependency Injection
UI Design: Jetpack Compose and Constraint layout
Here used Both Activity and Fragment 

**Here we have 3 pages**
1. User Login page
2. Post Listing
3. Profile page

**User Login Page**

Here 1st we load all the users using the given API, 
i) show loading progress bar
ii) If success will save the user details in viewmodel
iii) If there is any Error in API will show the simple error Dialog

Once API gets success will get the userName from the user, 
If user name matches from the UserList what we have then will save the userDetails in SharedPreference and navigate to postListing screen

**Post Listing**

Here we'll load 2 API's 
i) Get all the Posts available
ii) Get Post which are posted by the LoggedIn user

Here will have Filter option to help user to view only the post posted by the user and 
all the post which other users also posted
also there have Profile button to navigate to Profile page to see the User profile details


**Profile page**

Here will show the LoggedIn user Profile details, 
Here user have EmailId, Website and Location Latitude and Longitude

If user click on EmailId will navigate to Email screen
If user click Website will navigate to Website in browser
If user click locate me will navigate to Map Location
