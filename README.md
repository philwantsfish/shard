# shard

A command line tool to detect shared passwords

## Example Usage

List available modules:

``` bash
$ java -jar shard-1.0.jar -l
Available modules:
        Facebook
        LinkedIn
        Reddit
        Twitter
        Instagram
```

Given a username and password shard will attempt to authenticate with multiple sites:

``` bash
$ java -jar shard-1.0.jar -u <redacted> -p <redacted>
- Tried credentials on 5 sites
- Discovered 2 failed authentications:
-      Reddit
-      Instagram
- Discovered 3 successful authentications:
-      Facebook
-      LinkedIn
-      Twitter
```

## Installation

Grab the latest release from the release tab, which was built as a fat jar using sbt assembly.

or

Build it yourself using sbt
 

## Developing a new module

Adding a new module is easy. Create a new class that inherits from AbstractModule in the module package and add the module to the ModuleFactory.

The AbstractModule has one abstract method:
``` scala
  def tryLogin(creds: Credentials): LoginResult
```

This method takes a Credentials object and returns either a SuccessfulLogin or FailedLogin object. I recommend using the TwitterModule as an template.

Dependencies:
- JSoup is used for HTTP communication and HTML parsing 
- spray-json is used for handling json

## Bugs, Requests, and Feedback

Contact me or use this GitHub project











