from flask import Flask, request

my_ip = "160.39.166.246"
start_time, end_time = 0, 0


# initialize app
app = Flask(__name__)

# alarm settings route
@app.route('/alarm_time', methods=['POST', 'GET'])
def alarm_time():
	payload = request.json	
	print payload[start_time], payload[end_time] 
	return "Successful request."

if __name__ == '__main__':
	app.run(
		host="0.0.0.0",
		port=int("80")
	)

