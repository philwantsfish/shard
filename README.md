# shard

A command line tool to detect shared passwords

## Usage

List options:

```
Shard (1.5) can run in 3 modes:

1) Single user single password          - Use -u and -p
2) Single user multiple passwords       - Use -u and -f
3) Multiple users and multple passwords - Use -f only

For more detailed usage examples see the wiki.
    
Usage: java -jar shard-1.5.jar [options]

  -u, --username <value>  Username to test
  -p, --password <value>  Password to test
  -f, --file <value>      A path to a file containing a set of credentials or passwords
  --format <value>        The format of the credentials. Must be a regular expression with 2 capture groups. The first capture group for the username and the second capture group for the password. Defaults to a regex that will match:
        "username":"password"
  -l, --list              List available modules
  -v, --version           Print the version
  --modules <value>       Only run specific modules. A comma separated list
  --help                  Prints this usage text
```

List available modules:

``` bash
$ java -jar shard.jar -l
Available modules:
        Facebook
        LinkedIn
        Reddit
        Twitter
        Instagram
        GitHub
        BitBucket
        Kijiji
        DigitalOcean
        Vimeo
        Laposte
        DailyMotion

```


## Examples

Given a username and password shard will attempt to authenticate with multiple sites:

``` bash
$ java -jar shard.jar -u username-here -p password-here
21:16:25.950 [+] Running in single credential mode
21:16:30.302 [+] username-here:password-here - Reddit, Instagram
```

To test multiple credentials supply a filename. By default this expects one credential per line in the format `"username":"password"`. Custom formats can be supplied with the `--format` option

```
$ java -jar shard.jar -f /tmp/creds.txt
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

This method takes a Credentials object and returns a boolean indicating a successful login. I recommend using the TwitterModule as an template. For an indepth explanation of adding a new module see the [example on the wiki](https://github.com/philwantsfish/shard/wiki/Logging-into-Twitter-example)

Dependencies:
- JSoup is used for HTTP communication and HTML parsing 
- spray-json is used for handling json

If Scala is not your thing check out the secondary_implementations, these are rewrites of shard in other languages. If you add a module to one of these implementations I will rewrite in Scala and add it to the main project as well.

## Bugs, Requests, and Feedback

Contact me, join the [Gitter](https://gitter.im/philwantsfish/shard) room, or use this GitHub project











