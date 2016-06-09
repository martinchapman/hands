Search Games
===========

A plugin for the JGraphT library facilitating the running of Search Game simulations 

Search games are adversarial interactions between two competing agents, in which one agent attempts to obscure itself or some object(s) on a network, and the other attempts to find this agent or their hidden item(s). In their most basic form, search games are akin to the childhood game of hide and seek; objects are hidden, and remain so until a seeker locates them. To maximise her personal utility, a seeker will wish to locate all hidden items with lowest effort.

Due to their simplicity, games such as these serve as useful abstractions for many real world problems. The challenges in a game of hide and seek, for example, are akin to (but certainly not limited to) those in search and rescue, counter terrorism and cyber security. Using the tools of abstraction, we are able to control closely which elements of these problems we include in our model of game, and it leaves us with highly generalisable solutions, free of low-level concerns.

This platform extends the JGraphT library to introduce the ability to run such games in realtime. Taking a number of different strategies for both hiding and seeker, as well as a number of different environmental parameters (topology, number of hidden objects, etc.), the platform runs strategies against one another and records their performance, for an insight into their relative merits in various scenarios.

The platform is designed to eventually be evolved into a distributed game, in which participants can submit strategies to a central controller that runs them and evaluates their performance.

An introduction to Search Games, and in particular, Hide-and-Seek from a simulation perspective, is available here: http://martinchapman.co.uk/papers/acyse2014.pdf
