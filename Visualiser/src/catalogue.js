var films_inquiry = [];
var films_shown = [];
var catalogue_scroll = 0;
var max_catalogue_scroll = 0;

const CATALOGUE_FILM_HEIGHT = 100;
const CATALOGUE_SLIDER_SIZE = 48;

function draw_catalogue() {
	background(0);
	frame_offset_y = catalogue_scroll;
	//Draw all buttons in films_shown
	for(let i = 0; i < films_shown.length; i++) {
		films_shown[i].update();
	}
}

function show_all_films_in_catalogue() {
	films_inquiry = films_json.films;
	update_shown_films();
}

function update_shown_films() {
	films_shown.splice(0, films_shown.length);
	for(let i = 0; i < films_inquiry.length; i++) {
		let year = ' (' + films_inquiry[i].year + ')';
		films_shown.push(new FilmSelectButton(0, i * CATALOGUE_FILM_HEIGHT, DEFAULT_WIDTH - 120, CATALOGUE_FILM_HEIGHT, null, 1, films_inquiry[i].key, films_inquiry[i].title.concat(year), films_inquiry[i].translated.concat(year)));
	}
	max_catalogue_scroll = films_shown.length * -CATALOGUE_FILM_HEIGHT + DEFAULT_HEIGHT;
}