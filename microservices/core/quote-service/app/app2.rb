# -*- coding: utf-8 -*-
require 'sinatra'

set :bind, '0.0.0.0'
puts "WOOT a puts statment was HERE"

get '/' do
"hello world"
end
