import plotly.express as px
import plotly.graph_objects as go
import pandas as pd
from plotly.subplots import make_subplots

from PIL import Image

GROUP_BY_WHETHER_DEBUT_FROM_PRIVATE_STUDIOS = False

image_filename = "GIS/Plotly/res/map.png"
world_map = Image.open(image_filename)

df = pd.read_csv("GIS/source/people_plots(pixel-axis).csv")

if(GROUP_BY_WHETHER_DEBUT_FROM_PRIVATE_STUDIOS == False):
	fig = px.scatter(df, y="lat", x="long",
						color_continuous_scale="matter",
						color="dg",
						color_discrete_map={
							"Shanghai (state)": "#68d162",#green
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
						hover_name="name", range_x=[1000,2000], range_y=[-900,-400],
						animation_frame="yr", animation_group="id")
else:
	fig = px.scatter(df, y="lat", x="long",
						color_continuous_scale="matter",
						color="pri",
						color_discrete_map={
							"true" : "#10ce5e",#green
							"false" : "#f5a623" #orange
						},
						hover_name="name", range_x=[1000,2000], range_y=[-900,-400],
						animation_frame="yr", animation_group="id")

fig.update_layout(height = 800, width = 1280,
				margin={"r": 0, "t": 0, "l": 0, "b": 0})
fig.update(layout_coloraxis_showscale=False)
fig.add_layout_image(
        dict(
            source=world_map,
            xref="x",
            yref="y",
            x=0,
            y=0,
            sizex=2410,
            sizey=1169,
            #sizing="stretch",
            opacity=1,
            layer="below")
)
fig.update_yaxes(
    scaleanchor="x",
    scaleratio=1,
)
fig.show()