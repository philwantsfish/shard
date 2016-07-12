# shard

[![Join the chat at https://gitter.im/philwantsfish/shard](https://badges.gitter.im/frohoff/ysoserial.svg)](https://gitter.im/philwantsfish/shard?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

A command line tool to detect shared passwords

## Usage

List options:

```
$ java -jar shard-1.2.jar --help
Shard 1.2
Usage: java -jar shard-1.2.jar [options]

  -u, --username <value>  Username to test
  -p, --password <value>  Password to test
  -f, --file <value>      File containing a set of credentials
  --format <value>        The format of the credentials. Must be a regular expression with 2 capture groups. The first capture group for the username and the second capture group for the password. Defaults to a regex that will match:
	"username":"password"
  -l, --list              List available modules
  -v, --version <value>   Print the version
  --help                  prints this usage text
```

List available modules:

``` bash
$ java -jar shard-1.2.jar -l
Available modules:
        Facebook
        LinkedIn
        Reddit
        Twitter
        Instagram
```
## Examples

Given a username and password shard will attempt to authenticate with multiple sites:

``` bash
$ java -jar shard-1.2.jar -u username-here -p password-here
21:16:25.950 [+] Running in single credential mode
21:16:30.302 [+] username-here:password-here - Reddit, Instagram
```
To test multiple credentials supply a filename. By default this expects one credential per line in the format `"username":"password"`. Custom formats can be supplied with the `--format` option

```
$ java -jar shard-1.2.jar -f /tmp/creds.txt
21:16:39.501 [+] Running in multi-credential mode
21:16:39.516 [+] Parsed 2 credentials
21:16:42.794 [+] username1:password1 - Reddit, Instagram
21:16:45.189 [+] username2:password2 - Facebook, LinkedIn, Twitter
```

## Installation

Grab the latest release from the [release tab](https://github.com/philwantsfish/shard/releases), which was built as a fat jar using sbt assembly.

or

Build it yourself using sbt, `sbt assembly`
 

## Developing a new module

Adding a new module is easy. Create a new class that inherits from `AbstractModule` in the module package and add the module to the `ModuleFactory`.

The AbstractModule has one abstract method:
``` scala
  def tryLogin(creds: Credentials): Boolean
```

This method takes a Credentials object and returns a boolean indicating a successful login. I recommend using the TwitterModule as an template.

Dependencies:
- JSoup is used for HTTP communication and HTML parsing 
- spray-json is used for handling json

## Bugs, Requests, and Feedback

Contact me or use this GitHub project











