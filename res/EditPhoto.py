from PIL import Image


img = Image.open('C:/Users/bblah/OneDrive/Desktop/Flap for AI/res/Final/LowerPipe.png')


# ---------------------- FOR CROPPING ------------------------------------

# print(img.width)
# print(img.height)
# cropped = img.crop((0, 350, 321, 400))
# cropped.save("res/Final/UpperBackground.png", "PNG")

# ----------------------- FOR TRANSPERENCY --------------------------------

# rgba = img.convert("RGBA")
# data = rgba.load()
# width = rgba.size[0]
# height = rgba.size[1]

# # making pixels on the left transparent
# for j in range(height):
#     for i in range(width):
#         if data[i, j][0] < 200 or data[i, j][1] < 200 or data[i, j][2] < 200:
#             break
#         else:
#             data[i, j] = (0, 0, 0, 0)  
     
# # making pixels on the right transparent
# for j in reversed(range(height)):
#     for i in reversed(range(width)):
#         if data[i, j][0] < 200 or data[i, j][1] < 200 or data[i, j][2] < 200:
#             break
#         else:
#             data[i, j] = (0, 0, 0, 0)  

# rgba.save("res/Final/Pipe.png", "PNG")
