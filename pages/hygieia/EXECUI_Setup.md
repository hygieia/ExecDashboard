---
title: The Hygieia Executive Dashboard UI
tags:
keywords:
summary:
sidebar: hygieia_sidebar
permalink: EXECUI_Setup.html
---

The UI Layer represents **The Hygieia Executive Dashboard's** front-end and contains GUI elements for users to view and configure the DevOps tools on the dashboard.

The Hygieia dashboard requires installation of:

- nvm
- Node JS
- npm

*	**Step 1: Install nvm**

	Download and install the [nvm package](http://npm.github.io/installation-setup-docs/installing/using-a-node-version-manager.html) for your platform.

	Close and reopen your terminal to start using nvm.

*	**Step 2: Install node**

	To install Node, open terminal and execute the following command:

	```bash
	nvm install 8.9.1
	```

	Test whether Node is installed by running the following in the terminal:

	```bash
	node -e "console.log('Running Node.js ' + process.version)"
	```

*	**Step 3: Configure the UI**

	a. In the `ui/package.json`, add the following lines in the `scripts` section:
	
		   ```properties
		   # Run the UI on <port_number>, and bind it to the all the interfaces (IPs):

		   "local": "ng serve --host=0.0.0.0 --port=<port_number> --proxy-config=proxy.config.json --env=local",
		   "prod": "ng serve --host=0.0.0.0 --port=<port_number> --proxy-config=proxy.config.json --env=prod"
			
		   # --port - the ui comes up on this port
		   ```

    b. Update `ui/proxy.config.json` as follows:
	
    	{
  			"/api" : {
    		"target": "http://<host_ip>:<port of the server running the api>",
    		"changeOrigin": true,
    		"secure": false,
    		"logLevel": "debug",
    		"pathRewrite": {"^/api": "http://<host_ip>:<port of the server running the api>"}
  			}
		}
	
	c. Update the `apiUrl` property in the environment.ts, environment.local.ts and environment.prod.ts file at `/ui/src/environments/` as follows:
		
		```bash
		apiUrl: '/api'
		```

*	**Step 4: Run the UI**

	In the terminal, navigate to `/UI` and execute the following commands to install and run npm:
	
	```bash
	npm install
	npm run local
    ```	
	
	The dashboard will serve up on the port you configured in Step 3a.
	
	
