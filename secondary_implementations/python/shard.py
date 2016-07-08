import argparse
import sys, getopt, os.path
sys.path.append(os.path.join(os.path.dirname(__file__), '..'))

from modules.FacebookModule import facebook
from modules.InstagramModule import instagram
from modules.LinkedinModule import linkedin
from modules.RedditModule import reddit
from modules.TwitterModule import twitter

modules = [facebook, instagram, linkedin, reddit, twitter]

def main():
	username = ""
	password = ""

	parser = argparse.ArgumentParser(description='Shard reimplemented in Python')
	parser.add_argument('-l', action='store_true', help='Show list of items')
	parser.add_argument('-u', help='Username to check')
	parser.add_argument('-p', help='Password to check')

	args = parser.parse_args()
	if args.l:
		print "Available Modules:"
  		for module in modules:
  			print "\t" + module.__class__.__name__ 
  		sys.exit()
	else:
		username = args.u
		password = args.p

		if username != "" and password != "":
			attempt_login(username, password)

def attempt_login(uname, password):
	results = []

	for module in modules:
		results.append(module.try_login(uname, password))

	print "Results:"
	for i in range(0, len(results)):
		print "\t" + modules[i].__class__.__name__ + ": " + ("VALID_CREDENTIALS" if results[i] else "INVALID_CREDENTIALS")

if __name__ == "__main__":
	main()