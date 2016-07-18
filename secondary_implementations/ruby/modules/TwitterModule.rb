require_relative 'AbstractModule'  

class TwitterModule < AbstractModule  
  def try_login(username, password)  
    return false 
  end  
end  