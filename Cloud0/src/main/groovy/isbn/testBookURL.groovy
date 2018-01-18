import groovy.json.JsonSlurper

String googleBooksURL = 'https://www.googleapis.com/books/v1/volumes?q=isbn:9781405206310'

def url = googleBooksURL.toURL()
def json = url.getText()
println "json: $json "
def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText(json)
def title = object.items[0].volumeInfo.title

println "Title: $title"