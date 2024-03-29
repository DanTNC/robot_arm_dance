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
    rad.record_action("reset")
    emit("refresh", namespace="/", broadcast=True)
    return jsonify(rad.reset())

@app.route('/action/clear', methods=["GET"])
def do_clear():
    rad.clear()
    emit("refresh", namespace="/", broadcast=True)
    return jsonify(rad.probe())

@app.route('/action/<action>/<param>', methods=["GET"])
def do_action(action, param):
    if rad.action(action, param):
        rad.record_action("action", action, param)
        emit("refresh", namespace="/", broadcast=True)
        return jsonify({"result": "ok"})
    else:
        return jsonify({"result": "error"})

@app.route('/probe/<part>', methods=["GET"])
def monitor(part):
    return jsonify(rad.probe(part))
    
@app.route('/choreography/<num_actions>/<actions>', methods=["GET"])
def do_choreography(num_actions, actions):
    actions = actions[:-1].split(";")
    result = True
    for action in actions:
        motor, angle = action.split(":")
        result = result and rad.action(int(motor), int(angle))
    if result:
        rad.record_choreo(actions)
        emit("refresh", namespace="/", broadcast=True)
        return jsonify({"result": "ok"})
    else:
        return jsonify({"result": "error"})
        
@app.route('/toward/<angles>', methods=["GET"])
def set_states(angles):
    angles = angles[:-1].split(";")
    if rad.set_states(angles):
        rad.record_action("toward", angles)
        emit("refresh", namespace="/", broadcast=True)
        return jsonify({"result": "ok"})
    else:
        return jsonify({"result": "error"})

@socketio.on('connected')
def handle_my_custom_event(json):
    print('received json: ' + str(json))

if __name__ == '__main__':
    try:
        port = os.environ['PORT']
    except:
        port = 8080
    socketio.run(app, port=port)