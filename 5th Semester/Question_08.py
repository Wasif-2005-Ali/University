import numpy as np
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error

# Data from Q6
X = np.array([[1], [2], [3], [4], [5]])
y = np.array([3, 5, 6, 5, 7])

# Train the linear regression model
model = LinearRegression()
model.fit(X, y)

# Print slope and intercept
print("Slope:", model.coef_[0])
print("Intercept:", model.intercept_)

# Predict marks for 6 hours
prediction = model.predict(np.array([[6]]))
print("Predicted marks for 6 hours:", prediction[0])

# Predict for the original five students and calculate MSE
y_pred = model.predict(X)
mse = mean_squared_error(y, y_pred)
print("Mean Squared Error:", mse)

# Plot actual data and regression line
plt.scatter(X.ravel(), y, label="Actual data")
plt.plot(X.ravel(), y_pred, label="Regression line")
plt.xlabel("Hours studied")
plt.ylabel("Quiz marks")
plt.title("Simple Linear Regression")
plt.legend()
plt.grid(True)
plt.show()
