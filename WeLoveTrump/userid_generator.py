import secrets
import string

user_ids = [i for i in range(1, 11, 1)]
print(user_ids)
f = open("udpate_trump.sql", "w")

for i in user_ids:
    alphabet = string.ascii_letters + string.digits
    userid = ''.join(secrets.choice(alphabet) for i in range(15)) # for a 12 digit user id
    f.write('UPDATE users SET id = "{}" WHERE id = "{}";\n'.format(userid, i))
    f.write('UPDATE carddetail SET id = "{}" WHERE id = "{}";\n'.format(userid, i))
    f.write('UPDATE posts SET id = "{}" WHERE id = "{}";'.format(userid, i))
    f.write("\n")
f.close()