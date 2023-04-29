//Fonts
var font_chn, font_eng;
const FONT_CHN_PATH = './res/font/SourceHanSerifSC-Regular.otf';
const FONT_ENG_PATH = './res/font/SourceHanSerifSC-Regular.otf';
//Data
var films_json, names_csv;
const FILMS_JSON_PATH = '../metadata-all.json';
const NAMES_CSV_PATH = '../OCR/names-full.csv';
const RESOURCE_NUM = 4;
var filmMap = new Map();
//Count loaded resources
var loaded_resource = 0;

function loadFonts() {
	font_chn = loadFont(FONT_CHN_PATH, add_loaded_source, resource_error);
	font_eng = loadFont(FONT_ENG_PATH, add_loaded_source, resource_error);
}

function loadData() {
	films_json = loadJSON(FILMS_JSON_PATH, init_film_map, resource_error);
	names_csv = loadTable(NAMES_CSV_PATH, "csv", "header", add_loaded_source, resource_error);
}

function init_film_map() {
	for (var i = 0; i < films_json.films.length; i++) {
		film = films_json.films[i];
		filmMap.set(film.key, film);
	}
	loaded_resource++;
}
function add_loaded_source() {
	loaded_resource++;
}

function resource_error() {
	print("Error loading resource.");
}