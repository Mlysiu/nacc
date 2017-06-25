## Overview
nacc is an application that will calculate a average clustering coefficient for a given dataset input. Please note that currently supported way of providing the data is trough a file containing:

* 4 initial rows of comment data
* list of (id, id) pairs separated by a **tab**

## How to run it

In order to run nacc a user should have installed sbt tool.

To obtain the average clustering coefficient one parameter needs to be provided:

* direct path to file containing the data

run command example:

 ```
 sbt "run C:\Users\Mlysiu\workspace\nacc\src\main\resources\soc-Epinions1.txt"
 ```

which will look for board configurations on a 4 by 4 board with two Rooks and 4 four Knights.

## Configuration
Currently there are no support for a configuration file.

## Network Average Cluster Coefficient for Epinions social network

The challenge was to provide an answer for the question of average cluster coefficient for the data containing 75879 nodes with 508837 edges.

nacc Application has calculated network average cluster coefficient to be equal **0,1254**.

Now the score is different than the one presented on the site (https://snap.stanford.edu/data/soc-Epinions1.html) containing the data. There might be a few reason of such situation.
The most obvious one would be a bug in the code. However I've analyzed the code and did not find any. Also the unit tests are providing correct answer for the given (relatively small) data.
The second one might be the problem with calculating NACC itself. During my research on the web I've found at least two ways in calculating this value. (https://stackoverflow.com/questions/41926514/average-clustering-coefficient-for-graph)

Unfortunately I could not have found any information on the stanford site on the subject of used algorithm.

Console output:
```
$ sbt "run C:\Users\Mlysiu\workspace\nacc\src\main\resources\soc-Epinions1.txt"

[warn] Executing in batch mode.
[warn]   For better performance, hit [ENTER] to switch to interactive mode, or
[warn]   consider launching sbt without any commands, or explicitly passing 'shell'
[info] Loading global plugins from C:\Users\Mlysiu\.sbt\0.13\plugins
[info] Loading project definition from C:\Users\Mlysiu\workspace\nacc\project
[info] Set current project to nacc (in build file:/C:/Users/Mlysiu/workspace/nacc/)
[info] Running com.mlysiu.nacc.NACCApp C:\Users\Mlysiu\workspace\nacc\src\main\resources\soc-Epinions1.txt
21:52:05.811 [run-main-0] INFO com.mlysiu.nacc.dataprovider - Starting reading the file from [C:\Users\Mlysiu\workspace\nacc\src\main\resources\soc-Epinions1.txt]
21:52:05.908 [run-main-0] INFO com.mlysiu.nacc.dataprovider - Finished reading the file
21:52:06.643 [run-main-0] INFO com.mlysiu.nacc.parser - Finished parsing the data with [508837] Links
21:52:06.853 [run-main-0] INFO com.mlysiu.nacc.engine - Found [15538] isolated nodes
21:52:07.031 [run-main-0] INFO com.mlysiu.nacc.engine - Calculating Average Cluster Coefficient for total number of [75879] nodes
Success(0,1254)
[success] Total time: 29 s, completed 2017-06-25 21:52:34
```

## Improvements
* Some TODOs are left in the code indicating what can be improved.
* Some integration test can be introduced with a subset of soc-Epinions1.txt.
* More configuration when running app from the commmand line.
* Introduce more 3-party library (like akka-streams to more efficiently read an input file - in batches or scalaz for ability to use Validation type in parsing)

