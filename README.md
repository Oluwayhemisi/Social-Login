# Social Login for LinkedIn 

## Overview

This Application enables you Signup, Signin and use social login to Signin with LinkedIn's APIs. 

In this application, you can manage your requests to LinkedIn's APIs. It creates and stores your access token and invokes APIs upon request the application. 



The application uses the following development tools:

* Spring Boot: Used as web server framework [<https://spring.io/projects/spring-boot>]
* LinkedIn OAuth 2.0: user authorization and API authentication
* Maven: app building and management
* Java: SE 17 or later versions are required for development

## Prerequisites

* Ensure that you have an application registered in [LinkedIn Developer Portal](https://developer.linkedin.com/).
Once you have your application, note down the Client ID and Client Secret
* Add <http://localhost:8181/linkedinlogin> to the Authorized Redirect URLs under the **Authentication** section
* Configure the application build by installing MAVEN using [Installing Apache Maven](https://maven.apache.org/install.html)


**Configure the app:**

 1. Navigate to the **application.properties** file. 
 2. Edit the following properties in the file with your client credentials:

    > clientId = <replace_with_client_id>

    > clientSecret = <replace_with_client_secret>

    > redirectUri = <replace_with_redirect_url_set_in_developer_portal>

    > scope = <replace_with_api_scope>

 3. Save the changes.
  
## Start the application

To start the application:

1. Navigate to the server folder.
2. Open the terminal and run the following command to install dependencies:
`mvn install`
3. Execute the following command to run the spring-boot server:
`mvn spring-boot:run`


