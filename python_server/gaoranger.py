class RAD():
    def __init__(self):
        self.init_states()
        self.actions = []
        self.choreography = []
        self.PROBE_SWITCH = {
            "states": self.probe_states,
            "actions": self.probe_actions,
            "choreography": self.probe_choreography
        }
        self.MOTOR = [
            "base",
            "shoulder",
            "elbow",
            "wrist",
            "rotate",
            "gripper"
        ]
        self.LIMITS = {
            "base": (0, 180),
            "shoulder": (15, 165),
            "elbow": (0, 180),
            "wrist": (0, 180),
            "rotate": (0, 180),
            "gripper": (10, 73)
        }
        
    def init_states(self):
        self.states = {
            "led": False,
            "base": 0,
            "shoulder": 40,
            "elbow": 180,
            "wrist": 0,
            "rotate": 170,
            "gripper": 73
        }

    def probe(self, part):
        return self.PROBE_SWITCH[part]()

    def probe_states(self):
        return self.states

    def probe_actions(self):
        return self.actions

    def probe_choreography(self):
        return self.choreography

    def action(self, act, param):
        if type(act) == int:
            act = self.MOTOR[act]
        if act == "led":
            return self.act_led(param)
        else:
            return self.act_motor(act, param)

    def act_led(self, arg):
        if arg == "toggle":
            self.states["led"] = not self.states["led"]
        if arg == "on":
            self.states["led"] = True
        if arg == "off":
            self.states["led"] = False
        return True
        
    def act_motor(self, act, arg):
        arg = int(arg)
        target = self.states[act] + arg
        target = self.limit_crip(act, target)
        self.states[act] = target
        return True
    
    def limit_crip(self, motor, val):
        ll, ul = self.LIMITS[motor]
        if val < ll: return ll
        if val > ul: return ul
        return val
        
    def reset(self):
        self.init_states()
        return self.states
        
    def set_states(self, angles):
        for i, angle in enumerate(angles):
            angle = int(angle)
            motor = self.MOTOR[i]
            angle = self.limit_crip(motor, angle)
            self.states[motor] = angle
        return True
        
    def record_action(self, inst, *param):
        if inst == "reset":
            self.actions.append({"inst": "reset"})
            return
        if inst == "action":
            self.actions.append({"inst": "action", "actor": param[0], "param": param[1]})
            return
        if inst == "toward":
            self.actions.append({"inst": "toward", "angles": param[0]})
            return
    
    def record_choreo(self, actions):
        self.choreography.append(actions)
            