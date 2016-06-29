# -*- coding: utf-8 -*-
require 'sinatra'
require 'json'
require 'zipkin-tracer'

# Unbind requests from localhost only
set :bind, '0.0.0.0'

zipkin_config = {
  :service_name => 'quote-server',
  :service_port => 4567,
  :json_api_host => ENV['ZIPKIN_QUERY_URL'] || 'http://zipkin-query:9411/',
#  :log_tracing => true,
  :sample_rate => (ENV['ZIPKIN_SAMPLE_RATE'] || 1 ).to_f
}

puts zipkin_config

use ZipkinTracer::RackHandler, zipkin_config

print "Loading tag lines ..."
tags = []
File.open("taglines.txt").each do |line|
  tags << line.strip!.gsub(/x/,'x')
end
TAG_COUNT = tags.size.freeze
$stdout.puts " done"

r = Random.new(Time.now.to_i)

get '/' do
  content_type 'application/json'
  puts outbound_test
 {'tag_line' => tags[r.rand(TAG_COUNT)]}.to_json
end


require 'faraday'
def outbound_test
  conn = Faraday.new(:url => 'http://www.google.com') do |faraday|
    faraday.use ZipkinTracer::FaradayHandler, 'mygoogle' # 'service_name' is optional (but recommended)
    faraday.request :url_encoded
    faraday.adapter Faraday.default_adapter
  end
  response = conn.get '/'
  response.body
end
