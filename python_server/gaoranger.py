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
        self.ACT_CPU = {
            "led": self.act_led,
            "base": self.act_base,
            "shoulder": self.act_shoulder,
            "elbow": self.act_elbow,
            "wrist": self.act_wrist,
            "rotate": self.act_rotate,
            "gripper": self.act_gripper
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
        return self.ACT_CPU[act](param)

    def act_led(self, arg):
        if arg == "toggle":
            self.states["led"] = not self.states["led"]
        if arg == "on":
            self.states["led"] = True
        if arg == "off":
            self.states["led"] = False
        return True
    
    def limit_crip(self, ll, ul, val):
        if val < ll: return ll
        if val > ul: return ul
        return val
    
    def act_base(self, arg):
        arg = int(arg)
        target = self.states["base"] + arg
        target = self.limit_crip(0, 180, target)
        self.states["base"] = target
        return True
        
    def act_shoulder(self, arg):
        arg = int(arg)
        target = self.states["shoulder"] + arg
        target = self.limit_crip(15, 165, target)
        self.states["shoulder"] = target
        return True
        
    def act_elbow(self, arg):
        arg = int(arg)
        target = self.states["elbow"] + arg
        target = self.limit_crip(0, 180, target)
        self.states["elbow"] = target
        return True
        
    def act_wrist(self, arg):
        arg = int(arg)
        target = self.states["wrist"] + arg
        target = self.limit_crip(0, 180, target)
        self.states["wrist"] = target
        return True
        
    def act_rotate(self, arg):
        arg = int(arg)
        target = self.states["rotate"] + arg
        target = self.limit_crip(0, 180, target)
        self.states["rotate"] = target
        return True
        
    def act_gripper(self, arg):
        arg = int(arg)
        target = self.states["gripper"] + arg
        target = self.limit_crip(10, 73, target)
        self.states["gripper"] = target
        return True
        
    def reset(self):
        self.init_states()
        return self.states