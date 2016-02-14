from flask import Flask, render_template, send_from_directory, request
from flask.ext.cors import CORS

app = Flask(__name__)
app.debug = True
CORS(app)

@app.route("/")
def home():
    return render_template('home.html')

@app.route("/dashboard")
def dashboard():
    return render_template('dashboard.html')

@app.route('/test', methods=['GET', 'POST'])
def add_message():
    print request.json
    return "ehll"

if __name__ == "__main__":
    app.run()