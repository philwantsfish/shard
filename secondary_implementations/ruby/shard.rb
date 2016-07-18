require_relative 'modules/FacebookModule'
require_relative 'modules/InstagramModule'
require_relative 'modules/LinkedinModule'
require_relative 'modules/RedditModule'
require_relative 'modules/TwitterModule'
require 'optparse'

@targets = [FacebookModule.new, InstagramModule.new, LinkedinModule.new, RedditModule.new, TwitterModule.new]

@username = ""
@password = ""

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
end

