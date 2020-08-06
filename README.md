# CityPathFinder
To find connectivity between two cities

Install and Run (windows 10)
Prerequisite: Java 8

Upload SpringProject\citypathfinder Folder in Spring Tool Suite 3 as a Maven Project
run CityPathFinder by using run->spring boot Application
Logs get written to SpringProject\citypathfinder\citypathfinder  file . Overrides to default log levels are set in src/main/resources/application.properties

Usage
Send HTTP GET requests to localhost port 8080, naming origin and destination cities, thus:

http://localhost:8080/connected?origin=city1&destination=city2

If city1 is connected to city2 by any path along known roads, the response will be:

yes

If not, or if the request is not formatted properly, the response will be:

no

Design Choices
   whenever request is done,file data (i.e city.txt) is loaded in HashMap where key as source city  
   and Value as set of cities directly connected to source city
   city1 of request will be passed to method searchCityMap of CityPathFinderController as Source city 
   and city2 as destination city.
   Breadth-first search algorithm  is used to check connection between source and destination cities.
   Queue (Linked List ) is used to store all connected cities of source city and its connected cities.
   This search loop will break for any of following base conditions
      1. If destination city is found 
      2. If All nodes are visited and no City is available in Queue

Future Improvements
  Loading of data from file can be done only once i.e. at start of application and not everytime when request is hit.
  It could be further improved on UI. More customized error message can be displayed.

Implementation Details
cityhop was written and tested on a windows 10 with open jdk 1.8. 
The project skeleton was created by using Spring Tool Suite 3 on 5 Aug 2020

