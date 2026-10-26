# weather_logger.py
def summarize(temps):
    return {
        "minimum": min(temps) if temps else 0,
        "maximum": max(temps) if temps else 0,
        "average": sum(temps) / len(temps) if temps else 0.0
    }

temps = []
while True:
    val = input("Enter temperature ('done' to stop): ")
    if val.strip().lower() == "done":
        break
    temps.append(float(val))

print(summarize(temps))