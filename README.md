# Search Games

A plugin for the JGraphT library facilitating the running of Search Game simulations 

Search games are adversarial interactions between two competing agents, in which one agent attempts to obscure itself or some object(s) on a network, and the other attempts to find this agent or their hidden item(s). In their most basic form, search games are akin to the childhood game of hide and seek; objects are hidden, and remain so until a seeker locates them. To maximise her personal utility, a seeker will wish to locate all hidden items with lowest effort.

Due to their simplicity, games such as these serve as useful abstractions for many real world problems. The challenges in a game of hide and seek, for example, are akin to (but certainly not limited to) those in search and rescue, counter terrorism and cyber security. Using the tools of abstraction, we are able to control closely which elements of these problems we include in our model of game, and it leaves us with highly generalisable solutions, free of low-level concerns.

This platform extends the JGraphT library to introduce the ability to run such games in realtime. Taking a number of different strategies for both hiding and seeker, as well as a number of different environmental parameters (topology, number of hidden objects, etc.), the platform runs strategies against one another and records their performance, for an insight into their relative merits in various scenarios.

Currently available strategies:

- hRandomSet
- sBacktrackGreedy
- hFirstK
- sLinkedPath
- hNotConnected
- hLeastConnected
- hMaxDistance
- sLeastConnectedFirst
- sMaxDistanceFirst
- sHighProbability
- sInverseHighProbability
- hDeceptive
- hUniqueRandomSet
- sMetaProbability
- hMetaConnected
- hMetaRandom
- sGreedy
- sDFS
- sBFS
- sRandomTarry
- sRandomWalk

The platform is designed to eventually be evolved into a distributed game, in which participants can submit strategies to a central controller that runs them and evaluates their performance.

An introduction to Search Games, and in particular, Hide-and-Seek from a simulation perspective, is available [here](http://martinchapman.co.uk/papers/acyse2014.pdf).

### Running

`cd <repo path>`

`AWT_FORCE_HEADFUL=true /Library/Java/JavaVirtualMachines/jdk1.8.0_25.jdk/Contents/Home/bin/java -XX:-UseGCOverheadLimit -Xmx5g -Dfile.encoding=US-ASCII -classpath <repo path>/bin:<repo path>/lib/epsgraphics.jar:<repo path>/lib/jcommon-1.0.21.jar:<repo path>/lib/jfreechart-1.0.17.jar:<repo path>/lib/jgrapht-core-0.9.0.jar:<repo path>/lib/jgraph-sna.jar:<repo path>/lib/java-plot.jar:<repo path>/lib/commons-math3-3.4.1.jar:<repo path>/lib/commons-math3-3.4.1.jar:<repo path>/lib/bsh-2.0b4.jar Utility.Runner`

or

`cd <repo path>`

`AWT_FORCE_HEADFUL=true /Library/Java/JavaVirtualMachines/jdk1.8.0_25.jdk/Contents/Home/bin/java -XX:-UseGCOverheadLimit -Xmx5g -Dfile.encoding=US-ASCII -classpath <repo path>/bin:<repo path>/lib/epsgraphics.jar:<repo path>/lib/jcommon-1.0.21.jar:<repo path>/lib/jfreechart-1.0.17.jar:<repo path>/lib/jgrapht-core-0.9.0.jar:<repo path>/lib/jgraph-sna.jar:<repo path>/lib/java-plot.jar:<repo path>/lib/commons-math3-3.4.1.jar:<repo path>/lib/bsh-2.0b4.jar Utility.Runner`

or

`AWT_FORCE_HEADFUL=true /Library/Java/JavaVirtualMachines/jdk1.8.0_25.jdk/Contents/Home/bin/java -XX:-UseGCOverheadLimit -Xmx5g -Dfile.encoding=US-ASCII -classpath <repo path>/bin:<repo path>/lib/epsgraphics.jar:<repo path>/lib/jcommon-1.0.21.jar:<repo path>/lib/jfreechart-1.0.17.jar:<repo path>/lib/jgrapht-core-0.9.0.jar:<repo path>/lib/jgraph-sna.jar:<repo path>/lib/java-plot.jar:<repo path>/lib/commons-math3-3.4.1.jar:<repo path>/lib/bsh-2.0b4.jar Utility.Runner`
