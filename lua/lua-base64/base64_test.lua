require('base64')

str = "hello���й�"

print(str)

base64str = to_base64(str)

print(base64str)

s = from_base64(base64str)

print(s)
