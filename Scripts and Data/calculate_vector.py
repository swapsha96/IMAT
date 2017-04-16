import datetime
import numpy
def calculate_vector(p1, p2):
    purpose = (p2["Purpose"] == p1["Purpose"])*7.5
    destination = calculate_distance(p2["Destination"],p1["Destination"])*7.5
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
    score = purpose+destination+year+spare_time+degree+branch+gender+hostel+state
    label = (score >= 13)*1
    return [purpose, destination, year, spare_time, degree,\
    branch, gender, hostel, state, score, label]

def calculate_distance(a,b):
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


import csv
with open('VTD2.csv') as csvfile:
    with open('vectors.csv','w') as writefile:
        spamreader = csv.DictReader(csvfile)
        fieldnames = ["src_uid", "trn_uid","purpose","destination","year"\
                        , "spare_time", "degree", "branch", "gender", "hostel", "state", "score","label"]
        writer = csv.writer(writefile)
        writer.writerow(fieldnames)
        for row in spamreader:
            obj = row
            for i in range(3):
                train = spamreader.next()
                vector = calculate_vector(obj,train)
                store = [obj["Uid"], train["Uid"]] + vector
                writer.writerow(store)

