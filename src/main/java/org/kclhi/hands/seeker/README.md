Seeker Strategies
===========

Search strategies effectively denote which outgoing edge a traverser chooses when leaving their current node. In their most basic form, such strategies select edges at random. Better strategies will consider payoffd game metrics such as cost, when selecting an edge.

More complex strategies attempt to utilise some knowledge of the opponent, such as assumptions about their behaviour, and this will lead to a sequence of strategic edge choices. These strategies are likely to need more holistic knowledge of the search graph or topology, and although such information is not afforded to players, agents extending the SeekingAgent class are able to construct information on the topology over continued games. 
