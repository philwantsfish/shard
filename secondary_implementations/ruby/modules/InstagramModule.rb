require_relative 'AbstractModule'  

class InstagramModule < AbstractModule  
  def try_login(username, password)  
    return false #Fails with Cookie errors (403 Forbidden)
  	@user = user_agent
    headers = { 'user-agent' => @user}
  	response = RestClient.get("https://www.instagram.com/accounts/login/", :headers => headers){|response, request, result| response }

  	payload = {'username' => username, 'password' => password}
  	headers = { 'user-agent' => @user, 'Content-Type' => 'application/x-www-form-urlencoded', 'X-CSRFToken' => response.cookies['csrftoken'], "Accept" => "*/*", "Referer" => "https://www.instagram.com"}
  	response = RestClient.post("https://www.instagram.com/accounts/login/ajax/", payload, {:headers => headers, :cookies => response.cookies, :max_redirects => 0}){|response, request, result| response }
  	response = JSON.parse(response.body)

  	return response['authenticated'] == true
  end  
end  