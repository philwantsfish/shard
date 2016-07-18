# vim: ts=2 sw=2 noexpandtab
import unittest
from shard import attempt_login

from modules.FacebookModule import facebook
from modules.InstagramModule import instagram
from modules.LinkedinModule import linkedin
from modules.RedditModule import reddit
from modules.TwitterModule import twitter

modules = [facebook, instagram, linkedin, reddit, twitter]


class GenericTests(unittest.TestCase):
	def test_failed_logins(self):
		username = 'does-not-exist@gmail.com.uk'
		password = 'password'
		for m in modules:
			self.assertFalse(m.try_login(username, password))
