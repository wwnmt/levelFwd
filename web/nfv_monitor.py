from flask import Flask, render_template, jsonify, request
import requests
import json
from flask_sqlalchemy import SQLAlchemy
import csv
app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:root@labserver:3306/sdn'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True
db = SQLAlchemy(app)

def getHosts():
    r = requests.get("http://labserver:8181/onos/levelFwd/level/hosts", auth=('onos', 'rocks'))
    return r.content

def getMiddleBox():
    r = requests.get("http://labserver:8181/onos/levelFwd/level/middlebox", auth=('onos', 'rocks'))
    return r.content

class Record(db.Model):
    __tablename__ = 'sdn'

    no = db.Column(db.Integer, primary_key=True)
    dl_src = db.Column(db.String(50), nullable=False)
    dl_dst = db.Column(db.String(50))
    vlan_id = db.Column(db.Integer)
    nw_src = db.Column(db.String(30))
    nw_dst = db.Column(db.String(30))
    nw_proto = db.Column(db.String(10))
    nw_length= db.Column(db.Integer)
    src_port = db.Column(db.String(10))
    dst_port = db.Column(db.String(10))
    tcp_ack = db.Column(db.Integer)
    tcp_seq = db.Column(db.Integer)
    tcp_flags = db.Column(db.Integer)

    def __repr__(self):
        return '<Record %r>' % self.no

@app.route("/")
def hello():
    # return ''.join(json.loads(getHosts())['hosts'])
    return render_template('index.html')

@app.route("/middlebox")
def middlebox():
    return render_template('middlebox.html')

@app.route("/stat")
def stat():
    return render_template('stat.html')

@app.route("/alert")
def alert():
    return render_template('alert.html')

@app.route("/data")
def data():
    hosts = json.loads(getHosts())['hosts']
    newhosts = []
    for host in hosts:
        if host['ip'] != '0.0.0.0':
            newhosts.append(host)
    return jsonify({'total': len(newhosts), 'rows': newhosts})

@app.route("/data2")
def data2():
    hosts = json.loads(getMiddleBox())['middleBox']
    return jsonify({'total': len(hosts), 'rows': hosts})

@app.route('/data3')
def data3():
    records = Record.query.all()[:300]

    datas = []
    for record in records:
        datas.append({
            'timestamp': '2018-06-04 15:50',
            'dl_src': record.dl_src,
            'dl_dst': record.dl_dst,
            'vlan_id' : record.vlan_id,
            'nw_src' : record.nw_src,
            'nw_dst' : record.nw_dst,
            'nw_proto' : record.nw_proto,
            'nw_length': record.nw_length,
            'src_port' : record.src_port,
            'dst_port' : record.dst_port,
            'tcp_ack' : record.tcp_ack,
            'tcp_seq' : record.tcp_seq,
            'tcp_flags' : record.tcp_flags
        })
    return jsonify({'total': len(datas), 'rows': datas})

@app.route('/data4')
def data4():
    datas = []

    with open('output.csv', 'rb') as rb:
        r = csv.DictReader(rb)
        for line in r:
            datas.append(line)

    return jsonify({'total': len(datas), 'rows': datas})

@app.route("/levelup")
def levelup():
    r = requests.get("http://labserver:8181/onos/levelFwd/level/host/up/" + request.args.get('id'), auth=('onos', 'rocks'))
    return r.content

@app.route("/leveldown")
def leveldown():
    r = requests.get("http://labserver:8181/onos/levelFwd/level/host/down/" + request.args.get('id'), auth=('onos', 'rocks'))
    return r.content

@app.route("/test")
def test():
    return getMiddleBox()

@app.route("/sqltest")
def sqltest():
    records = Record.query.all()[:20]

    datas = []
    for record in records:
        datas.append({
            'no': record.no,
            'dl_src': record.dl_src,
            'dl_dst': record.dl_dst,
            'vlan_id' : record.vlan_id,
            'nw_src' : record.nw_src,
            'nw_dst' : record.nw_dst,
            'nw_proto' : record.nw_proto,
            'nw_length': record.nw_length,
            'src_port' : record.src_port,
            'dst_port' : record.dst_port,
            'tcp_ack' : record.tcp_ack,
            'tcp_seq' : record.tcp_seq,
            'tcp_flags' : record.tcp_flags
        })
    # print datas
    return json.dumps(datas)

