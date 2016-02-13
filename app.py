from flask import Flask, render_template, send_from_directory

app = Flask(__name__)
app.debug = True

@app.route("/")
def hello():
    return render_template('home.html')

if __name__ == "__main__":
    app.run()