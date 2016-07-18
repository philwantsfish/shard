require 'rest-client'
require 'json'
class AbstractModule
	def user_agent
		@agents = [ "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"]
		return @agents.sample
	end

	def try_login(username, password)
		return false
	end

	# StackOverflow Magic for Making Cookies Capable
	# http://stackoverflow.com/a/36330054/2467731
	def parse_set_cookie(all_cookies_string)
  	cookies = Hash.new

 		#if all_cookies_string.present? - Removed because of lacking support. Assume cookies are returned always.
    	# single cookies are devided with comma
    	all_cookies_string.split(',').each {
      	# @type [String] cookie_string
        	|single_cookie_string|
      	# parts of single cookie are seperated by semicolon; first part is key and value of this cookie
      	# @type [String]
      	cookie_part_string  = single_cookie_string.strip.split(';')[0]
      	# remove whitespaces at beginning and end in place and split at '='
      	# @type [Array]
      	cookie_part         = cookie_part_string.strip.split('=')
      	# @type [String]
      	key                 = cookie_part[0]
      	# @type [String]
      	value               = cookie_part[1]

      	# add cookie to Hash
      	cookies[key] = value
    	}
  	#end
	cookies
	end

	# Some more magic, derived from the Stack Overflow one above
	def extract_cookie_content(all_cookies_string, target)
		all_cookies_string.split(',').each {
				|single_cookie_string|

			cookie_part_string  = single_cookie_string.strip.split(';')[0]
			cookie_part         = cookie_part_string.strip.split('=')
			key                 = cookie_part[0]
			value               = cookie_part[1]

			if target == key
				return value
			end
		}
	end
end