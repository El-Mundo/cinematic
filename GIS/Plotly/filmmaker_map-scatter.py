import plotly.express as px
import plotly.graph_objects as go
import pandas as pd
from plotly.subplots import make_subplots

from PIL import Image

GROUP_BY_WHETHER_DEBUT_FROM_PRIVATE_STUDIOS = False

image_filename = "GIS/Plotly/res/map.png"
world_map = Image.open(image_filename)

df = pd.read_csv("GIS/source/people_plots(pixel-axis).csv")
df2 = pd.read_csv("GIS/source/studios_geo(pixel)(year_isolated).csv")

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

if(GROUP_BY_WHETHER_DEBUT_FROM_PRIVATE_STUDIOS == False):
	fig = go.Figure(px.scatter(df, y="lat", x="long",
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
	)
else:
	fig = go.Figure(px.scatter(df, y="lat", x="long",
						color_continuous_scale="matter",
						color="pri",
						color_discrete_map={
							"true" : "#10ce5e",#green
							"false" : "#f5a623" #orange
						},
						hover_name="name", range_x=[1000,2000], range_y=[-900,-400],
						animation_frame="yr", animation_group="id")
	)

fig2 = go.Figure(px.scatter(df2, y="latitude", x="longitude", size="films_in_year",
					#color="colour_film_ratio_in_year", color_continuous_scale="matter", range_color=[0, 1],
                    size_max=75, text="city", hover_name="geo_category",
                    animation_frame="year", animation_group="geo_category", range_x=[1000,2000], range_y=[-900,-400],opacity=0.2,
		    		color="geo_category",
					color_discrete_map = {
						"Shanghai (all)": "#68d162",#green
						"Beijing": "#D063fb",#hliotrope
						"Northeast": "#72d2ed",#sky blue
						"Xi'an": "#Fb9f68",#tan orange
						"Canton": "#Fb94cc",#lavender pink
						"Xinjiang": "#C83b3b",#dark red
						"Sichuan" : "#2c6398",#tropaz blue
						"Hubei": "#C1C4A6",#yellow grey
						"Anhui": "#9DA1C2",#blue grey
						"Shandong": "#B2BEAB",#green grey
						"Tianjin": "#AFC8C3",#cyan grey
						"Zhejiang": "#BFBAD0"})#purple grey
		)

frames = [
    go.Frame(data=f.data + fig.frames[i].data, name=f.name)
    for i, f in enumerate(fig2.frames)
]

updmenus = [{"args": [None, {"frame": {"duration": 2000}}],"label": "&#9654;","method": "animate",},
            {'args': [[None], {'frame': {'duration': 0}, 'mode': 'immediate', 'fromcurrent': True, }],
                  'label': '&#9724;', 'method': 'animate'} ]

fig3 = go.Figure(data = fig2.data + fig.data, frames=frames, layout=fig.layout)

fig3.update_layout(height = 800, width = 1280,
				margin={"r": 0, "t": 0, "l": 0, "b": 0},
				sliders = [sliders_dict])
fig3.update(layout_coloraxis_showscale=False)
fig3.add_layout_image(
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
fig3.update_yaxes(
    scaleanchor="x",
    scaleratio=1,
)
fig3.show()