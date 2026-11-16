contact = {"name": "Hamza",
           "phone": "0300-1223123",
           "city": "Islamabad" 
          }

#a
print(contact)

#b
contact["email"] = "245187@aack.au.edu.pk"
print("After adding email:" , contact)

#c
contact["city"]= "Rawalpindi"
print("After modifying city:" , contact["city"])

#d
removed_phone = contact.pop("phone")
print("Removed phone:", removed_phone)

#e
print("Company:", contact.get("company","Freelancer"))

#f 
for key, value in contact.items():
    print(key, "-->",value)