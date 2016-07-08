import requests
from AbstractModule import AbstractModule

class InstagramModule(AbstractModule):
	def try_login(self, uname, password):
		agent = AbstractModule().user_agent() 
		headers = { 'user-agent': agent }
		resp = requests.get("https://www.instagram.com/accounts/login/", headers= headers)

		payload = {'username': uname, 'password': password}
		headers = { 'user-agent': agent, 'Content-Type': 'application/x-www-form-urlencoded', 'X-CSRFToken': resp.cookies["csrftoken"], "Accept": "*/*", "Referer": "https://www.instagram.com"}
		loginResp = requests.post("https://www.instagram.com/accounts/login/ajax/", data= payload, headers= headers, cookies=resp.cookies, allow_redirects=False)
		
		if loginResp.json()["authenticated"] == True:
			return True
		else:
			return False
		
instagram = InstagramModule()