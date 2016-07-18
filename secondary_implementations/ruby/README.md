# Shard For Ruby 3.0+


## Install Instructions

1. Install Ruby 3.0+
2. Download this folder
3. Install dependencies with `sudo bundle install`
4. Use with `ruby shard.rb -l` for checking loaded modules
5. Use with `ruby shard.rb -u <USERNAME> -p <PASSWORD>` for a password check.
6. Use with `ruby shard-rb -u <USERNAME> -f plist.txt` for a targetted password file check.
7. Use with `ruby shard-rb -f uplist.txt` for a username:password combo check.
Sample files for the text files have been committed.
Compatible with SecList password lists (https://github.com/danielmiessler/SecLists/tree/master/Passwords)

## Dependencies
RestClient for making HTTP requests

Implements all of the original shard modules in Ruby (straight rewrites)
There's an AbstractModule added that can be used for implementing additional modules.