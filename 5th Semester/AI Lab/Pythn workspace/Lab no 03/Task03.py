catalog = [
{"title": "Introduction to AI", "author": "Stuart Russell",
"copies_available": 3, "rating": 4.8},
{"title": "Python Crash Course", "author": "Eric Matthes",
"copies_available": 0},
{"title": "Deep Learning", "author": "Ian Goodfellow",
"copies_available": 5, "rating": 4.6}
]

#a
for book in catalog: 
    print(book["title"],"by", book["author"])

#b
avaible_titles = [book["title"] for book in catalog if book["copies_available"] >= 1]
print ("Available titles:", avaible_titles)

#c
for book in catalog:
    print(book["title"], "rating:", book.get("rating", "Not Rated"))

#d
catalog[0]["copies_available"]-=1
print('Copies available:', catalog[0]["copies_available"])
print("Updated catalog:", catalog)