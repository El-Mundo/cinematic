# cinematic

This project preserves the information about 732 Chinese films produced in the "Seventeen Years Period" (1949-1966).

## Documents

[Design Report](https://github.com/El-Mundo/cinematic/blob/master/quantitative_film_analysis_github.pdf)

[Video Document](https://github.com/El-Mundo/cinematic/blob/master/VideoDocument.pdf)

As the project consists of several parts focusing on different technical frameworks, please refer to the video below and [the video document](https://github.com/El-Mundo/cinematic/blob/master/VideoDocument.pdf) for a full demonstration of its functions.

https://github.com/user-attachments/assets/2451f37f-54b3-4b92-9559-7d2b2d0c0110



## Data

### Sources

[Modelled data of the 656 entries](metadata.csv) in *The Catalogue of Chinese Artistic Films* (*中国艺术影片编目*; China Film Archive, 1982) with romanised title, original title, translated title, release year, production, colour, length in film reels, and recorded special aspects

[The staff information and plot summary](metadata-staff_plot.csv) of the 656 entries, separated from the main file due to the large file sizes

[An extra collection](metadata-extra.csv) of 76 entries not included by the book

Some [formatted csv files](Network/csv) that can be directly imported to **Gephi** for social network analysis (7293 nodes and 259622 edges)

[OCR source data](OCR/source) scanned from *The Catalogue of Chinese Artistic Films* (the source data may contain some missing attributes, which have been fixed in [the main metadata file](metadata.csv))

[Results of Topic Modelling](Topic/Topics_Summarised.csv) generated with a Gensim model trained with 9312 stopwords (including 7150 names of fictional characters from the OCR results) and 16 topics

### Visualisation

[A filmography visualiser in JavaScript](https://el-mundo.github.io/cinematic/Visualiser/main.html) for previewing the dataset

[A Plotly scatter map](https://htmlpreview.github.io/?https://github.com/El-Mundo/cinematic/blob/master/GIS/Plotly/filmmaker_map-scatter2d.html) of filmmakers/actors/actresses' geographical movement

[A simplified 2d graph Jupyter Notebook](https://colab.research.google.com/drive/1b_-976J_37duFKahJvevNPuxODw_EX6a) of film counts by year and region

[A Gephi file](Network/Gephi-all.gephi) of a social network generated based on the staff information of all 732 entries

[A Jupyer Notebook of topic modelling restuls for plot summaries](https://colab.research.google.com/drive/1kcM3bt-m_UX2wd59r-H6gbRlHBp6OFfJ?usp=sharing) with PyLDAvis graph

[Visualisation of Crowd Size Distribution](https://colab.research.google.com/drive/1s0PyRhnhscRNEoKJm6X6eIH4Us4S4Now?usp=sharing) in All Films
