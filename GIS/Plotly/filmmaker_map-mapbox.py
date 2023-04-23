import plotly.express as px
import pandas as pd

from PIL import Image

GROUP_BY_WHETHER_DEBUT_FROM_PRIVATE_STUDIOS = False

image_filename = "GIS/Plotly/res/map.png"
world_map = Image.open(image_filename)

px.set_mapbox_access_token(open("GIS/Plotly/mapbox_token.txt").read())
df = pd.read_csv("GIS/source/people_plots(geographical).csv")

if(GROUP_BY_WHETHER_DEBUT_FROM_PRIVATE_STUDIOS == False):
	fig = px.scatter_mapbox(df, lat="lat", lon="long",
						color_continuous_scale="matter",
						color="dg",
						color_discrete_map={
							"Shanghai (state)": "#2596be",#green
							"Shanghai (private)" : "#404840",#green black
							"Shanghai (roc)" : "#96fb96",#light green
							"Shanghai (state) / Hong Kong" : "#2596be", #dark green
							"Beijing": "#D063fb",#hliotrope
							"Beijing / Hong Kong" : "#F3c0f9",#light purple
							"Northeast": "#72d2ed",#sky blue
							"Xi'an": "#Fb9f68",#tan orange
							"Canton": "#Fb94cc",#lavender pink
							"Hong Kong / Canton": "#F5aedc",#chantilly
							"Xinjiang": "#C83b3b",#dark red
							"Sichuan" : "#2c6398",#tropaz blue
							"Shanghai (state) / Xinjiang" : "#A3a3a3",#below are silver grey
							"Hubei": "#A3a3a3",
							"Anhui": "#A3a3a3",
							"Shandong": "#A3a3a3",
							"Soviet Union / Northeast": "#A3a3a3",
							"Beijing / Xi'an": "#A3a3a3",
							"Hong Kong / Northeast": "#A3a3a3",
							"Inner Mongolia / Northeast": "#A3a3a3",
							"Beijing / Xinjiang": "#A3a3a3",
							"Xi'an / Qinghai": "#A3a3a3",
							"Xi'an / Northeast": "#A3a3a3",
							"Beijing / Hubei": "#A3a3a3",
							"Beijing / Anhui": "#A3a3a3",
							"Beijing / Western Europe": "#A3a3a3",
							"Shanghai (state) / Anhui": "#A3a3a3",
							"Gansu / Northeast": "#A3a3a3",
							"Shanghai (state) / Canton": "#A3a3a3",
							"Shanghai (state) / Hunan": "#A3a3a3",
							"Tianjin": "#A3a3a3",
							"Beijing / Shanghai (state)": "#A3a3a3",
							"Zhejiang": "#A3a3a3",
							"Shanghai (roc) / Shanghai (private)": "#A3a3a3",
							"Sichuan / Northeast": "#A3a3a3",
							"Shanghai (state) / Jiangsu": "#A3a3a3",
							"Beijing / Northeast": "#A3a3a3",
							"Beijing / Tianjin": "#A3a3a3",
							"Shanghai (state) / Shanghai (private)": "#A3a3a3",
							"Shanghai (state) / Northeast": "#A3a3a3",
							"Shanghai (state) / Xi'an": "#A3a3a3",
							"Northeast / Tianjin": "#A3a3a3",
							"Canton / Northeast": "#A3a3a3",
							"Beijing / Shanghai (private) / Northeast": "#A3a3a3",
							"Beijing / Sichuan": "#A3a3a3",
							"Shanghai (state) / Shandong": "#A3a3a3",
							"Beijing / Shanghai (state) / Anhui": "#A3a3a3",
							"Beijing / Shanghai (state) / Northeast": "#A3a3a3"
						},
						hover_name="name", zoom=3,
						animation_frame="yr", animation_group="id",
						mapbox_style="mapbox://styles/el-mundo/clgse9vm7001x01p6hrli21dq")
years = ["1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958",
         "1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966"]

sliders_dict = {
    "active":0,
    "yanchor":"top",
    "xanchor":"left",
    "currentvalue":{
        "font":{"size": 20},
        "prefix":"Year:",
        "visible":True,
        "xanchor":"right"
	},
    "transition": {"duration": 2400, "easing": "cubic-in-out"},
    "pad": {"b": 10, "t": 50},
    "len": 0.9,
    "x": 0.1,
    "y": 0,
    "steps": []
}

for year in years:
    slider_step = {"args": [
        [year],
        {"frame": {"duration": 2400, "redraw": False},
         "mode": "immediate",
         "transition": {"duration": 2400}}
    ],
        "label": year,
        "method": "animate"}
    sliders_dict["steps"].append(slider_step)


fig.update_traces(cluster=dict(enabled=True))
fig.update_layout(height = 800, width = 1280, margin={"r": 10, "t": 10, "l": 10, "b": 10},sliders=[sliders_dict])
fig.show()