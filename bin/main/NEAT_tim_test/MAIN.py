import numpy as np
import nnfs

nnfs.init()


inputs = [[1, 2, 3, 2.5],
          [2, 5, -1, 2],
          [-1.5, 2.7, 3.3, -0.8]] # More inputs at once = more accurate line of representation


# Part 5
# Activiation function (WAY COOLER IF NON-LINEAR)
# Explained in part 5 of video why activation function should be non-linear (allows nodes to activate and deactivate)
# ReLU (Rectified Linear Unit) activation function
# for i in inputs:
#     output.append(max(0, i))

# Part 6
# Softmax activation function
# The last output should be:
# 1. Subtract the largest value from all values so that all values are less than 0 (that way, when we do y = e^x we won't get some crazy big number)
# 2. y = e^x to all values
# 3. normalization. The y = e^x is to allow for negative numbers to be converted to positive without losing their meaning
# Instead of a negative number, it will be really close to 0 instead. The normalization allows for a probability distribution

# Part 7
# Loss
# softmax_output = [0.7, 0.1, 0.2] = s
# Target_output = [1, 0, 0] = t
# Loss = -(ln(s[0]*t[0]) + ln(s[1]*t[1]) + ln(s[2]*t[2]))
# Calculates how far off we were from the ideal categorical values
# With each batch, we want to calculate the mean of the losses (NEED TO DECIDE BATCH SIZE (probably via number of birds))
# Since ln(0) = infinity, we need to pick an arbitray low number instead of 0 like 1e-7 (maybe also 'clip' upper bound 9:34 Part 8)

# How do we calculate loss for Flappy Bird? How can we tell it that it was incorrect to jump at this point in time compared to this point in time?
# Idea: if the jump is a fail (i.e. hits something), then we treat it as a fail, but if it was successful before the next jump, we treat it as a success


class Layer_Dense:
    def __init__(self, n_inputs, n_neurons): # Initialize weights/biases as random values between -1 and 1 (we want these values to be small in a neural network)
        self.weights = 0.1 * np.random.randn(n_inputs, n_neurons)
        self.biases = np.zeros((1, n_neurons)) # Each neuron will have a bias
    def forward(self, inputs):
        self.output = np.dot(inputs, self.weights) + self.biases


class Activation_ReLU:
    def forward(self, inputs):
        self.output = np.maximum(0, inputs)


layer1 = Layer_Dense(4, 5)
print(layer1.weights)
print("----------------------------------")
layer2 = Layer_Dense(5, 2)
layer1.forward(inputs)
layer2.forward(layer1.output)
print(layer2.output)


# Parts 1 - 4.5

# weights = [[0.2, 0.8, -0.5, 1.0], 
#            [0.5, -0.91, 0.26, -0.5],
#            [-0.26, -0.27, 0.17, 0.87]]

# biases = [2, 3, 0.5]

# layer1_outputs = np.dot(inputs, np.array(weights).T) + biases

# weights2 = [[0.1, -0.14, 0.5], 
#            [-0.5, 0.12, -0.33],
#            [-0.44, 0.73, -0.13]]

# biases2 = [-1, 2, - 0.5]

# layer2_outputs = np.dot(layer1_outputs, np.array(weights2).T) + biases2

# # .T = transpose
# print(layer2_outputs)