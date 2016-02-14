import datetime
from flask import Flask, request,  jsonify
from flask.ext.mongoengine import MongoEngine
from bson import json_util
import json
from flask.ext.cors import CORS

my_ip = "160.39.166.246"
start_time, end_time = 0, 0

# initialize app
app = Flask(__name__)
app.debug = True
app.config['MONGODB_SETTINGS'] = {
  'db': 'hammertime',
  'host': 'mongodb://admin:admin@ds061355.mongolab.com:61355/hammertime'
}
CORS(app)
db = MongoEngine(app)

class Day(db.Document):
  created_at = db.DateTimeField(default=datetime.datetime.now, required=True)
  alarm_time = db.DateTimeField()
  wake_up_time = db.DateTimeField()

# alarm settings route
@app.route('/alarm_time', methods=['POST', 'GET'])
def alarm_time():
	payload = request.json	
	print payload[start_time], payload[end_time] 
	return "Successful request."

@app.route('/get_data')
def get_data():
  all_objects = [json.loads(o.to_json()) for o in Day.objects.order_by('alarm_time')]
  return jsonify(data=all_objects)
  
if __name__ == '__main__':
	app.run(
		host="0.0.0.0",
		port=int("8080")
	)

