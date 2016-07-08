import requests
from lxml import html
from AbstractModule import AbstractModule

class TwitterModule(AbstractModule):
	def try_login(self, uname, password):
		agent = AbstractModule().user_agent() 
		headers = { 'user-agent': agent }
		resp = requests.get("https://twitter.com/", headers= headers)

		tree = html.fromstring(resp.content)
		payload = tree.forms[4].fields
		payload = { k : v for k, v in payload.iteritems() }
		payload['session[username_or_email]'] = uname
		payload['session[password]'] = password
		headers = { 'user-agent': agent, 'Content-Type': 'application/x-www-form-urlencoded'}
		loginResp = requests.post("https://twitter.com/sessions", data= payload, headers= headers, cookies=resp.cookies, allow_redirects=False)

		if 'login/error?username_or_email' in loginResp.headers['Location']:
			return False
		else:
			return True

twitter = TwitterModule()