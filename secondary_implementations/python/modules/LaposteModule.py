import requests
from lxml import html
from AbstractModule import AbstractModule

class LaposteModule(AbstractModule):
    def try_login(self, uname, password):
        agent = AbstractModule().user_agent()
        headers = {'user-agent': agent}
        resp = requests.get("https://www.laposte.net/accueil", headers=headers)
        tree = html.fromstring(resp.content)
        payload = tree.forms[0].fields
        payload = {k: v for k, v in payload.iteritems()}
        payload['login'] = uname
        payload['password'] = password
        headers = {'user-agent': agent, 'Content-Type': 'application/x-www-form-urlencoded'}
        loginResp = requests.post("https://compte.laposte.net/login.do", data=payload, headers=headers,
                                  cookies=resp.cookies, allow_redirects=False)

        if loginResp.status_code == 302:
            return True
        else:
            return False


laposte = LaposteModule()
