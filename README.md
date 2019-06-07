ABN AMERO Coding assignment
===========================================================

Requirement :

create an app that shows 10 venues around a given place (e.g. Rotterdam) with a radius
of 1000 meters.

The App is composed of 2 screens, list and its detail screen.

For the list screen uses the following call to search the venues
 https://developer.foursquare.com/docs/api/venues/search

For the detail screen
https://developer.foursquare.com/docs/api/venues/details



#### VenuesSearchListFragment
Allows you to search near by venues on foursquare.
Each search result is kept in the database in `VenueSearchResult` table where
the list of repository IDs are denormalized into a single column.
The actual `Venue` instances live in the `Venue` table.

Each time a new page is fetched, the same `VenueSearchResult` record in the
Database is updated with the new list of repository ids.

**NOTE**
The UI currently loads all `venue` items at once.

#### VenueDetailFragment
This fragment displays the details of a Venue and its Photos.


### Building
You can open the project in Android studio and press run.

### Testing
The project uses both instrumentation tests that run on the device
and local unit tests that run on the computer.

To run both of them and generate a coverage report, you can run:
`./gradlew fullCoverageReport` (requires a connected device or an emulator)


##### UI Tests
The projects uses Espresso for UI testing. Since each fragment
is limited to a ViewModel, each test mocks related ViewModel to
run the tests.

##### Database Tests
The project creates an in memory database for each database test but still
runs them on the device.

#### Local Unit Tests
##### ViewModel Tests
Each ViewModel is tested using local unit tests with mock Repository
implementations.

##### Repository Tests
Each Repository is tested using local unit tests with mock web service and
mock database.

##### Webservice Tests
The project uses [MockWebServer][mockwebserver] project to test REST api interactions.


### Libraries
* Android Support Library, support-lib
* Android Architecture Components
* Android Data Binding data-binding
* Dagger 2 for dependency injection
* Retrofit for REST api communication
* Glide for image loading
* Espresso for UI tests
* mockito for mocking in tests


Below are the questions to be prepared -

1. Difference between MVP, MVVM ?
2. View Model - difference between

View models are objects that provide data for UI components and survive configuration changes.
it is also just general good software design, one common pit fall when developing android applications is putting a lot of variables,
logic, data into your activities and fragments. this creates large unmaintainable mess of a class. that violates the single responsibility
principle.

you can use view model to easily divide out responsibility

View model is responsible for holding all of the data that you're going to show in your UI . and the activity is only responsible for
knowing how to draw that data to the screen and receiving user interactions but not for processing them.


LiveData - Live data is simple life cycle aware observable data holder,
Two component communicate each other using this data holder, Activity / Fragment will be holding
reference of View Model. this is the one of the advantage MVVM in compare to MVP pattern where each presenter
is holding reference of view to present data.

Here live data will not hold reference of activity rather activity will hold the reference of ViewModel

for the reason we uses observables called live data.

How do we do this, in the view model we will expose our live data and in the activity we will do actual
subscription

it basically subscribe and unsubscribe data based on life cycle of activity/fragment.

if you are trying to process stream of events , live data is not the one which should be chosen

combining live data :
Library provides Map, Switch Map, mediator live data.

we know that live data is great communicator between view and view model, that communicates using life cycle of activity and fragment.
what if we have third component called repository that also exposing live data , how do we make this subscription from the view model ?
we do not have life cycle there .
How do we bridge between view and repository, how do we get to that to live data ? we uses a transformations map, which is one to one
static transformation .

its bridge of live data, it converts live data of X, which is come from repository, to the live data of Y which will be consumed by UI
model.

in the middle we have transformation function from X to Y.

Live data which you hold on when some one subscribe to it, that life cycle is automatically propagated to the inner live data elements
without you do anything.

when do we need switch map ?
let assume that you logged in with one user ID and user ID details available from the disk(local cache) and you need to talk to user
repository to get the actual user object. and that probably goes to the database and also the server to return you this user object.
Repository may returns your LiveData as it updates from server, you are in the situation where you have a live data of a logged in User ID
and live data of a user from server. that you need to chain these things , so map is needed if you are changing from an ID to a user.

how do we change from an ID to a live data of a user ? that's where switch map is needed

Every time user ID changes, it calls your function, you give it to a new live data, it unsubscribe from the previous Livedata and
subscribe new live data.

Mediator data :
Lets say you have more than one live data and you want to derive data out of all these. An example like you want to find length
of all the live data by looking into each on of the live data. if there one live data changes , total length of string should be varying.




By observer, Activity
   Map,
   Switch Map,
   LiveData,
   Mediator Live data,
   Mutable Live data and Immutable Live data.

3 Dependency Injections
difference type of dependency injections.

4. Data binding
   BindingAdapters
   One way binding, two way binding

5. Room
TypeConverters
Room @Relation annotation
Room @Embedded
Room @Ignore

Rx Java

OpenForTesting


Fragment

 UI components or behavior that you’re going to use across multiple Activities.
When your users would benefit from seeing two different layouts side-by-side



https://www.codementor.io/blog/android-interview-questions-3ey9hu32ut


STAR


Data binding interview questions
The Data Binding Library allows you to bind UI components in your layouts to data sources in your app using a declarative format rather than programmatically
Two types of data binding
Static data binding
Observable data binding


Live data interview questions
Rx java interview questions
Room interview questions
paging library interview questions
work manager interview questions
dependency injection questions
Expresso test cases questions


https://github.com/MindorksOpenSource/android-interview-questions/blob/master/README.md#design-problem


https://blog.mindorks.com/what-are-android-architecture-components





