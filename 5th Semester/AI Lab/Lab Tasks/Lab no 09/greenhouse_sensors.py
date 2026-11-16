import numpy as np

np.random.seed(7)
readings = np.random.randint(20, 41, size=(6, 3))
print(readings)

sensor_means = readings.mean(axis=0)
print(f"Mean per sensor: {sensor_means}")

sensor_stds = readings.std(axis=0)
standardized = (readings - sensor_means) / sensor_stds
print(f"Normalized column means: {standardized.mean(axis=0).round(1)}")

hour_of_max = readings.argmax(axis=0)
print(f"Hour of max per sensor: {hour_of_max}")

stacked_readings = np.vstack([readings, sensor_means])
print(stacked_readings.shape)

np.random.seed(7)
readings_reproduced = np.random.randint(20, 41, size=(6, 3))
print(f"Reproducible: {np.array_equal(readings, readings_reproduced)}")

print(f"Overall hottest reading: {readings.max()}")