from flask import Flask, render_template, jsonify
from flask_socketio import SocketIO, emit
from config import DevConfig
from gaoranger import RAD
import os

rad = RAD()

app = Flask(__name__)
app.config.from_object(DevConfig)
socketio = SocketIO(app)

@app.route('/')
def index():
    return render_template('index.html')


@app.route('/action/reset', methods=["GET"])
def do_reset():
    return jsonify(rad.reset())

@app.route('/action/<action>/<param>', methods=["GET"])
def do_action(action, param):
    if rad.action(action, param):
        emit("refresh", namespace="/", broadcast=True)
        return jsonify({"result": "ok"})
    else:
        return jsonify({"result": "error"})

@app.route('/probe/<part>', methods=["GET"])
def monitor(part):
    return jsonify(rad.probe(part))

@socketio.on('connected')
def handle_my_custom_event(json):
    print('received json: ' + str(json))

if __name__ == '__main__':
    try:
        port = os.environ['PORT']
    except:
        port = 8080
    socketio.run(app, port=port)