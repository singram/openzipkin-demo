# -*- coding: utf-8 -*-
require 'sinatra'
require 'json'
require 'zipkin-tracer'

# Unbind requests from localhost only
set :bind, '0.0.0.0'

use ZipkinTracer::RackHandler, {
      :service_name => 'quote-server',
      :service_port => 4567,
      :json_api_host => ENV['ZIPKIN_QUERY_URL'] || 'http://zipkin-query:9411',
      :log_tracing => true,
      :sample_rate => ENV['ZIPKIN_SAMPLE_RATE'].to_f || 1
   }

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
 {'tag_line' => tags[r.rand(TAG_COUNT)]}.to_json
end
