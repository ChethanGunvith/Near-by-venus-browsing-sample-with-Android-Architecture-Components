Android Architecture Blueprints is a project to showcase different architectural approaches to developing Android apps. 

***NOTE*** It is a relatively more complex and complete example so if you are not familiar with Architecture Components, you are highly recommended to check other examples in this repository first.

# Introduction
##### Functionality

The app is composed of 3 main screens.

Requirement :

create an app that shows 10 venues around a given place (e.g. Rotterdam, utrecht) with a radius
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


Libraries
- Android Support Library
- Android Architecture Components
- Android Data Binding
- Dagger 2 for dependency injection
- Retrofit for REST api communication
- Glide for image loading
- Timber for logging
- espresso for UI tests
- mockito for mocking in tests

