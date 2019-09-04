from django.urls import path
from . import views

app_name = 'quotes'

urlpatterns = [
	path('', views.index, name='index'),
	path('search/quotes', views.search_quotes, name='search_quotes'),
	path('add/author', views.add_author, name='add_author'),
	path('add/quote', views.add_quote, name='add_quote'),
	path('search/form', views.search_form, name='search_form'),
	path('<str:author_name>/', views.detail, name='detail'),
]
