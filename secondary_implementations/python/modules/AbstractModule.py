# vim: ts=2 sw=2 noexpandtab
import random
from abc import ABCMeta, abstractmethod


class AbstractModule:
	__metaclass__ = ABCMeta

	def user_agent(self):
		agents = ['Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 '
							'(KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36',
							'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) '
							'AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 '
							'Safari/537.36']
		return random.choice(agents)

	def try_login(self, uname, password):
		return False
