# Data Analysis on Neo4j and Visualization

This project's aim is to make some of the data analysis methods easy. 
Project contains a server (Spring Boot Application) and a UI which offers several data visualization techniques.

What you need to run the project:
(A Linux distro is obligator)

1)You need to have a local Neo4j Database. 

Dataset used in this project is the Movie Database, which includes 12k movies, 50k actors.  

You can either use the existing "neo4j-community-3.0.3" from this repository.

OR

You need to download a Neo4j database from here:
https://neo4j.com/download/
(Community edition is used in this project)

And for project to run you need to download the dataset from here:
https://neo4j.com/developer/example-data/
and put in into the "neo4j-community-YOURVERSION/data/databases/" and swap it with the initial graph.db if there exists.

And please disable Neo4j authentication from neo4j.conf file that is in conf folder of neo4j, by uncommenting the line:"dbms.security.auth_enabled=false"

2)You need to have a running Sprint Tool Suite(STS) application which can be downloaded from here:
https://spring.io/tools/sts/all

3) In the "jhipster" folder in this repository, you need to run the project as follows: "./mvnw". (A detailed explanation on what is needed to run the project can be found in the README.md file in the "jhipster" folder.) For JHipster to run you need to install Java, Git, Node.js, Bower, Yeoman and Gulp first.

How to Use the Application

1) Go to localhost:8080. You can see the initial screen.

2) To see the graph of a movie, click on the "Movie Analysis" button and select a movie.

3) To find the shortest path between a movie and a person, a movie and a movie, a person and a person, click on the "Show Analysis Results" button and select the two inputs for the analysis.

4) To see the result of the Degree Centrality analysis, click on the "Show Charts" buttons and select movie or person degree centrality.

To Start The Server

Open STS and chose to import an existing Maven project. Choose "SocialNetwork" that is downloaded from the repository. Run the project as Maven Install. (Please report any build failures to tuterbatuhan@gmail.com).

Note: Do not forget to change the database path from "Operation.java" file from the project.

And run the project as Spring Boot Application after the build finishes.

