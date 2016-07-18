require_relative 'modules/FacebookModule'
require_relative 'modules/InstagramModule'
require_relative 'modules/LinkedinModule'
require_relative 'modules/RedditModule'
require_relative 'modules/TwitterModule'
require 'optparse'

@targets = [FacebookModule.new, InstagramModule.new, LinkedinModule.new, RedditModule.new, TwitterModule.new]

@username = ""
@password = ""
@file = nil

OptionParser.new do |opts|
  opts.banner = "Usage: shard.rb [options]"

  opts.on("-l", "List commands") do |v|
    puts "Available Modules"
    @targets.each do |targ|
    	puts "\t#{targ.class.name}"
    end
  end

  opts.on("-uUsername", "Username to try") do |v|
    @username = v
  end

  opts.on("-pPassword", "Password to try") do |v|
    @password = v
  end

  opts.on("-fFile", "File") do |v|
    @file = v
  end
end.parse!

if @username && @username != "" && @password && @password != ""
	puts "Results:"
	@targets.each do |targ|
    if targ.try_login(@username, @password)
    	puts "\t#{targ.class.name}: VALID_CREDENTIALS"
    else
    	puts "\t#{targ.class.name}: INVALID_CREDENTIALS"
    end
  end
elsif @username && @username != "" && @file
  puts "Results:"
  File.readlines(@file).each do |line|
    @password = line.gsub("\n","")

    puts "Trying: " + @password
    @targets.each do |targ|
      if targ.try_login(@username, @password)
        puts "\t#{targ.class.name}: VALID_CREDENTIALS (" + @password + ")"
      end
    end
  end
elsif @file
  puts "Results:"
  File.readlines(@file).each do |line|
    @split = line.split(":")
    @username = @split[0].gsub!('"', '')
    @password = @split[1].gsub!('"', '')

    puts "Trying: " + @username + " " + @password
    @targets.each do |targ|
      if targ.try_login(@username, @password)
        puts "\t#{targ.class.name}: VALID_CREDENTIALS (" + @username + " => " + @password + ")"
      end
    end
  end
end

