class SessionsController < ApplicationController
	def new
		user = User.find_by_remember_token(cookies[:remember_token])
		redirect_to '/domains' and return unless user.nil?
	end
	def create
	 	user = User.find_by_name(params[:session][:name])
		puts user
	  	if user && user.authenticate(params[:session][:password])
			sign_in user
			redirect_to '/domains'
		else
			flash[:error] = 'Invalid username/password combination' # Not quite right!
      			redirect_to :root
		end
	end
	def destroy
	    sign_out
	    redirect_to root_url
	end
end
