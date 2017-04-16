import cPickle
import os
import requests
import json
import pandas as pd
import numpy as np
import numpy
from random import randint
import time
import datetime
import pickle
import re
import unicodedata
import yaml
import ast


from flask import Flask, redirect, url_for, request
app = Flask(__name__)

@app.route('/admin')
def hello_admin():
   return 'Hello Admin'

@app.route('/guest/<guest>')
def hello_guest(guest):
   return 'Hello %s as Guest' % guest

@app.route('/list', methods=['GET', 'POST'])
def classify():
	try:
		req = request.args.get('data')
		cwd = os.getcwd()
		fName = "{}/req.pkl".format(cwd)
		print("Saving classifier to '{}'".format(fName))
		with open(fName, 'w') as f:
			pickle.dump(req, f)

		# cwd = os.getcwd()
		# fName = "{}/req.pkl".format(cwd)
		# with open(fName, 'r') as f:
		# 	req = pickle.load(f)

		#req = req.decode(encoding='UTF-8',errors='strict')
		req = unicodedata.normalize('NFKD', req).encode('ascii','ignore')
		req = req.replace('u\'','"').replace('\'','"')
		req = re.sub("(?<=[\{ ,])u'|'(?=[:,\}])", '"', req)
		req = ast.literal_eval(req)
		p1 = {}
		p1["Purpose"]=req["Purpose"]
		p1["Gender"]=req["Gender"]
		p1["uid"]=req["User"]
		p1["Destination"]=req["Destination"]
		p1["Branch"]=req["Branch"]
		p1["Year"]=req["Year"]
		p1["Hostel"]=req["Hostel"]
		p1["State"]=req["State"]
		p1["Degree"]=req["Degree"]


		req = json.dumps(req)
		#re.sub("(?<=[\{ ,])u'|'(?=[:,\}])", '"', req)
		req_json=json.loads(req)
		print type(req)
		print type(req_json)
		#req_json=json.dumps(req)
		#data=pd.read_json(req_json)

		#print "\n\n\n" + req + "\n\n\n"
		print req
		#print req_json['data']
		#req=req.encode(encoding='UTF-8',errors='strict')
		#return str(req)
		#return req

	except ValueError:
		print "Json request not formatted properly"
		g="{\n\tStatus: 401 - Bad Request\n\tMessage: Json request not formatted properly\n}\n"
		return g

	mandi_people = req_json['data']
	#print mandi_people

	rec_obj = Recommender()
	res = {}
	for mp in mandi_people:
		#print mp
		mp = str(mp)
		mp = mp.replace('u\'','"').replace('\'','"')
		mp = re.sub("(?<=[\{ ,])u'|'(?=[:,\}])", '"', mp)
		#mp = mp.replace('u\'','').replace('\'','')
		#a_dict = dict([mp.strip('{}').split(":"),])
		#mp = yaml.load(mp)
		mp = ast.literal_eval(mp)
		#mp = json.loads(mp)
		#print mp['Gender']

		#print mp
		# l1 = [mp['Gender'], mp['Destination'], mp['Time_In'], mp['Time_Out'], mp['State'], mp['Purpose'], mp['Branch'], mp['Year'], mp['Hostel']] 
		# l2 = ['Gender', 'Destination', 'Time_In', 'Time_Out', 'State', 'Purpose', 'Branch', 'Year', 'Hostel']
		# print l1
		# print l2
		#df = pd.DataFrame([mp['Gender'], mp['Destination'], mp['Time_In'], mp['Time_Out'], mp['State'], mp['Purpose'], mp['Branch'], mp['Year'], mp['Hostel']], columns=['Gender', 'Destination', 'Time_In', 'Time_Out', 'State', 'Purpose', 'Branch', 'Year', 'Hostel'])
		#mp=json.dumps(mp)
		# mp = str(mp)
		# mp = mp.replace('u\'','').replace('\'','')
		# mp = dict(mp)
		# print mp
		# #mp = str(mp)
		#re.sub("(?<=[\{ ,])u'|'(?=[:,\}])", '"', mp)
		mp = json.dumps(mp)
		mp = json.loads(mp)
		#mp = json.dumps(mp)
		#print mp['Degree']
		#data_mp = pd.read_json(mp, typ='series', orient='records')
		#print data_mp['Destination']
		#data_mp=pd.read_json(str(mp)) #, typ='series')
		#data_mp = pd.DataFrame(mp)
		print "\n"

		# mp=json.loads(mandi_people)
		# mp=json.dumps(mp)
		# data_mp=pd.read_json(mp)

		#print "\n\nmp\n\n"
		#print data_mp

		#self.pre_process_data(data)

		out = rec_obj.predict(p1,mp)
		res[mp['uid']] = str(out[0])

		print "\n"
		print out
		print "\n"

	res = str(res)
	res = res.replace('u\'','"').replace('\'','"')
	res = re.sub("(?<=[\{ ,])u'|'(?=[:,\}])", '"', res)
	res = ast.literal_eval(res)
	
	tmp = []
	for u in res:
		tmp1={}
		tmp1["uid"]=u
		tmp1["score"]=res[u]
		tmp.append(tmp1)

	result = {}
	result["data"]=tmp

	result = json.dumps(result)
	print result
	return result

class Recommender:

    # Global - pickle_obj so we load pickle only once and not for every request
    pickleName = './nn_classifier.pkl'
    with open(pickleName, "rb") as input_file:
    	pickle_obj = cPickle.load(input_file)

    
    def __init__(self):
        self.pickle = Recommender.pickle_obj

                
    def calculate_distance(self,a,b):
        places = ["Mandi Campus", "Admin Block", "Indira Market", "Gandhi Chowk", "Raman Bakery", "Mandav Hospital", "Zonal Hospital", "Victoria Bridge"]
        dist_mat = [[00,05,10,12,15,17,17,8],
                    [05,00,15,17,20,22,22,13],
                    [10,15,00,02,05,07,07,8],
                    [12,17,02,00,03,10,10,8],
                    [15,20,5,3,0,12,12,5],
                    [17,22,7,10,12,0,5,15],
                    [17,22,7,10,12,5,0,15],
                    [8,13,8,8,5,15,15,0]]
        MAX = numpy.max(dist_mat)+1
        ind_a = places.index(a)
        ind_b = places.index(b)
        return float(MAX - dist_mat[ind_a][ind_b])/MAX


    def calculate_vector(self,p1, p2):
        purpose = (p2["Purpose"] == p1["Purpose"])*7.5
        destination = self.calculate_distance(p2["Destination"],p1["Destination"])*7.5
        year = (p2["Year"] == p1["Year"])*6
        time_in = datetime.datetime.strptime(p2["Time_In"], '%H:%M').time()
        time_out = datetime.datetime.strptime(p2["Time_Out"], '%H:%M').time()
        th = (time_out.hour-time_in.hour)%12
        tm = ((float(time_out.minute) - float(time_in.minute))%60)/60
        spare_time =  ((float(th)+tm)/8.5)*5.5
        degree = (p1["Degree"] == p2["Degree"])*5
        branch = (p1["Branch"] == p2["Branch"])*4
        gender = (p1["Gender"] == p2["Gender"])*2.5
        hostel = (p1["Hostel"] == p2["Hostel"])*2.5
        state = (p1["State"] == p2["State"])*1

        return [purpose, destination, year, spare_time, degree, branch, gender, hostel, state]

    
    def predict(self,p1,p2):
        """
        :parameter : None        
        :return : type - pandas.Series , 
                  desc - Predictions based on average probabilities from stacking models

        """
        #self.user_id=data.uid
        vect = self.calculate_vector(p1,p2);

        #predicted_model = self.pickle.predict()
        # Probability of action
        probab = self.pickle.predict_proba(vect)[:,1]

        # predicted['Prediction']=predicted['Prediction'].astype(int)
        # predicted['Customer_id']=self.customer_id
        # predicted['Default_score']=avg_probab.round(2)
        #predicted=pd.DataFrame(columns=['uid','score'])
        #predicted

        return probab


if __name__ == '__main__':
	#classify()
	app.run(host='0.0.0.0',debug=True)



