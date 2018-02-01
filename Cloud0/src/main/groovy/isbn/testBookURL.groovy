import groovy.json.JsonSlurper

String googleBooksURL = 'https://www.googleapis.com/books/v1/volumes?q=isbn:9781405206310'

def url = googleBooksURL.trim().toURL()
def json = url.getText()
println "json: $json "
def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText(json)
def title = object.items[0].volumeInfo.title
def publisher = object.items[0].volumeInfo.publisher
def authors = object.items[0].volumeInfo.authors[0]

println "Title: $title  pub: $publisher author: $authors[0] "