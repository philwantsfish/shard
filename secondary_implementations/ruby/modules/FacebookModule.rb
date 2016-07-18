require_relative 'AbstractModule'  

class FacebookModule < AbstractModule
  def try_login(username, password) 
  	headers = { 'user-agent' => user_agent}
  	response = RestClient.get("https://www.facebook.com", {:headers => headers}){|response, request, result| response }

  	payload = {'email' => username, 'pass' => password}
  	response = RestClient.post("https://www.facebook.com/login.php", payload, {:headers => headers, :cookies => response.cookies, :max_redirects => 0}){|response, request, result| response }

  	return response.code == 302
  end  
end  