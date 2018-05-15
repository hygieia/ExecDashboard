---
title: Database Setup
tags:
keywords:
summary:
sidebar: hygieia_sidebar
permalink: EXECDatabase_Setup.html
---

Hygieia Executive uses MongoDB (versions 3.0 and above) as the database to store and retrieve data. Download the MongoDB GUI, such as Robo 3T, to create the database dashboard and successfully connect to it.

## Prerequisites

Following are the prerequisites to set up the **Hygieia Executive Dashboard**:

- Install GIT - Install Git for your platform. For installation steps, see the [Git documentation](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- Install Java - The recommended version is 1.8 

## Download and Install Instructions

If you do not already have MongoDB installed, download the Installation Package from <a href="https://www.mongodb.org/downloads#previous">MongoDB download 	page</a> and follow the installation instructions provided in the <a href="https://docs.mongodb.com/manual/">MongoDB manual</a>.

## Configuration Steps

Follow the configuration steps to run Hygieia Executive in your MongoDB installation.

*	**Step 1: Create Data Directory**

    Create a data directory on the drive from which you start MongoDB to store your data files.
	
	For example, create the data directory in the following path:
	
	```bash
	C:/Users/[usernname]/dev/data/db
	```
	
*	**Step 2: Change Directory**
    
	Change the current working directory to the <code>bin</code> directory of your MongoDB installation.
	
	In the command prompt, run th following command:
	
	```bash
	cd C:/Program Files/MongoDB/Server/3.2/bin
	```
	
*	**Step 3: Start MongoDB**

	Specify the path to use as data directory:
	
	```bash
	mongod --dbpath [path to the data directory]
	```
	
	Execute the following commands to start MongoDB, switch to analyticsdb, and then add dashboard user:

	```bash
	#Start mongo

	Command: > mongo
	Output:  MongoDB shell version: 3.2
			 connecting to: test
				
	#Switch to analyticsdb

	Command: > use analyticsdb
	Output:  switched to analyticsdb

	#Create db user

	Command: > db.createUser(
				{
				  user: "dashboarduser",
				  pwd: "dbpassword",
				  roles: [
				  {role: "readWrite", db: "analyticsdb"}
				  ]
				})
				
	Output:  Successfully added user: {
			 "user" : "dashboarduser",
			 "roles" : [
			  {
				"role" : "readWrite",
				"db" : "analyticsdb"
			  }
			  ]
			  }
	```