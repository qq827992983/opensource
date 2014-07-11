require('base64')

str = "hello£¬ÖĞ¹ú"

print(str)

base64str = to_base64(str)

print(base64str)

s = from_base64(base64str)

print(s)
