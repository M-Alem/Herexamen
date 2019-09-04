from django.shortcuts import render

from .models import Author
from .models import Quote
import string

def index(request):
    author_names = [a.author_name for a in Author.objects.all()]

    return render(request, 'quotes/index.html', {'author_names': author_names})


def add_author(request):
    if request.method == 'POST':
        a = Author(author_name=request.POST['author_name'])
        a.save()
        return index(request)
    if request.method == 'GET':
        return render(request, 'quotes/add_author.html', {})


def detail(request, author_name):
    author = Author.objects.filter(author_name=author_name).first()
    quote_list = author.quote_set.all()

    return render(request, 'quotes/detail.html', {'quote_list': quote_list})

def add_quote(request):
        if request.method == 'POST':
            name = Author(author_name=request.POST['author_name'])
            q = Quote(quote_text = request.POST['quote_text'], quote_author = name)
            return detail(request, name)

def search_form(request):
    return render(request, 'quotes/search_form.html', {})

def search_quotes(request):
    if request.method == 'POST':
        word = request.POST['search_term']
        quote_list = Quote.objects.all()
        result = [q.quote_text for q in quote_list if q.search_quote(word)]

    return render(request, 'quotes/detail.html', {'quote_list': result})



