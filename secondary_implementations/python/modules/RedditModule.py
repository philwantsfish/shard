import requests
from AbstractModule import AbstractModule

class RedditModule(AbstractModule):
	def try_login(self, uname, password):
		payload = {'user': uname, 'passwd': password, 'api_type': 'json'}
		headers = { 'user-agent': AbstractModule().user_agent, 'Content-Type': 'application/x-www-form-urlencoded'}
		loginResp = requests.post("http://www.reddit.com/api/login/", data= payload, headers= headers)

		if 'wrong password' in loginResp.content:
			return False
		else:
			return True

reddit = RedditModule()