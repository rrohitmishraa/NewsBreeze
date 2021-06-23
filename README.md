# NewsBreeze
<ul>
<li> This app retrieves the Latest Breaking News from an API and displays it in an activity (home screen) with the help of recyclerview. Every new fetched has an image, a title, date on which the news was published and a short description to give the gist of the news. </li>

<li> The screen contains a search bar using which user can search for the article by its title. </li>

<li> With other details, it also has a bookmark button which helps the user save the news to their local device to read it later. </li>

<li> To store the news on local device, I have used SQLite database. </li>
	
<li> Clicking on the news takes you to the other activity that contains the detaild news. Everything on this screen is retreived from the API again. This screen also has a button to save or bookmark the news that performs the same action of storing the news data in the SQLite database. </li>

<li> I have tried to make the UI look as similar as possible </li>
<li> I have also added Lottie ANimations in the UI Elements to make it look more attractive and interactive. </li>
<li> The home screen has on "saved news" button on the top-right that takes you to the screen where all the saved news appear. It also has a search bar to search the news from the database but this data is retreived from the local database instead of the API. </li>
<li> Cllicking on any news will take you to another screen that will display the detailed news. This data is not fetched from the API but from the local database. </li>
</ul>
