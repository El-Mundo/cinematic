import plotly.express as px
import pandas as pd

px.set_mapbox_access_token(open("Network/Plotly/mapbox_token.txt").read())
df = pd.read_csv(
    "https://raw.githubusercontent.com/plotly/datasets/master/2011_february_us_airport_traffic.csv"
)
fig = px.scatter_mapbox(df, lat="lat", lon="long", size="cnt", zoom=3)
fig.update_traces(showscale=False, cluster=dict(enabled=True))
fig.show()