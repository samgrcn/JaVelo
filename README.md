# BA2 Project – JaVelo
The aim of this project, called JaVelo, was to create a bicycle route planner in Switzerland.

The interface of JaVelo is similar to that of online planners like Google Maps. However, 
JaVelo is not a web application - which runs partly in the browser and partly on a remote server - 
but a Java application that runs exclusively on the user's computer.

The image below shows the graphical interface of JaVelo,  
which will be familiar to anyone who has used a route planner before. 
The upper part shows the map of the route, while the lower part shows its longitudinal profile.

<img src="resources/javelo-gui.png">

As usual in such programs, the map can be moved, enlarged or reduced with the mouse.

The planning of a route is done by placing at least two waypoints - the start and end point - 
by clicking on the map. Once two of these points have been placed, 
JaVelo determines the route between these two points that it considers ideal for a person travelling by bicycle. 
In doing so, it takes into account not only the type of roads used - favouring minor roads, cycle paths and the like - 
but also the terrain - avoiding steep climbs.

Once a route has been calculated, its longitudinal profile is displayed at the bottom of the interface, 
along with some statistics: total length, 
positive and negative gradients, etc. When the mouse pointer is over a point on the profile, 
the corresponding point on the route is highlighted on the map, and vice versa.

Finally, it is possible to modify an existing route by adding, deleting or moving waypoints. 
Each change causes the recalculation of the ideal route and its longitudinal profile.

JaVelo is limited to the Swiss territory, as there is currently no digital elevation model 
covering the entire Earth that is accurate enough for our needs and available free of charge. 
For Switzerland, such a model does exist, as the Swiss Federal Office of Topography 
(<a href="https://www.swisstopo.admin.ch/">swisstopo</a>) has recently started to offer free access to all its data, 
including the very accurate <a href="https://www.swisstopo.admin.ch/fr/geodata/height/alti3d.html">SwissALTI3D</a>
elevation model, which we will use.