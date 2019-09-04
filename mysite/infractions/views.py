from django.shortcuts import render

import json



def index(request):
    json_data = open('../infractions.json')
    data1 = json.load(json_data)
    data2 = json.dumps(data1)
    json_data.close()


    for key in data2:
        value = data2[key]
        return HttpResponse("The key and value are ({}) = ({})".format(key, value))
    



