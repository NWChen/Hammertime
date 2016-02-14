from flask import Flask, request
import json

my_ip = "160.39.166.246"
current_time, end_time = [], []

# initialize app
app = Flask(__name__)

# alarm settings route
@app.route('/alarm_time', methods=['POST', 'GET'])
def alarm_time():
	payload = request.json
	print payload
	
	print payload['current_time'], payload['alarm_time'] 
	return "Successful request."

if __name__ == '__main__':
	app.run(
		host="0.0.0.0",
		port=int("8000")
	)

